package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import pethotel.controller.BookingController;
import pethotel.controller.CustomerController;
import pethotel.controller.RoomController;
import pethotel.model.Booking;
import pethotel.model.Cat;
import pethotel.model.Customer;
import pethotel.model.Dog;
import pethotel.model.Pet;
import pethotel.model.Room;

public class MainFrame extends JFrame {

    private BookingController bookingController;
    private CustomerController customerController;
    private RoomController roomController;

    public MainFrame(BookingController bookingController,
            CustomerController customerController,
            RoomController roomController) {

        this.bookingController = bookingController;
        this.customerController = customerController;
        this.roomController = roomController;

        setTitle("Pet Hotel System");
        setSize(520, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createMainMenu();
    }

    private void createMainMenu() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 25, 50));

        JLabel title = new JLabel("PET HOTEL SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(8, 1, 7, 7));

        JButton registerButton = new JButton("1. Register Customer & Pet");
        JButton bookingButton = new JButton("2. Book Pet Hotel");
        JButton detailsButton = new JButton("3. Show Customer & Pet Details");
        JButton roomsButton = new JButton("4. Show Rooms");
        JButton allBookingsButton = new JButton("5. Show All Bookings");
        JButton searchButton = new JButton("6. Search Customer");
        JButton petsInRoomsButton = new JButton("7. Show Pets in Rooms");
        JButton exitButton = new JButton("0. Exit");

        registerButton.addActionListener(e -> registerCustomer());
        bookingButton.addActionListener(e -> bookHotel());
        detailsButton.addActionListener(e -> showCustomerDetails());
        roomsButton.addActionListener(e -> showRooms());
        allBookingsButton.addActionListener(e -> showAllBookings());
        searchButton.addActionListener(e -> searchCustomer());
        petsInRoomsButton.addActionListener(e -> showPetsInRooms());
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(registerButton);
        menuPanel.add(bookingButton);
        menuPanel.add(detailsButton);
        menuPanel.add(roomsButton);
        menuPanel.add(allBookingsButton);
        menuPanel.add(searchButton);
        menuPanel.add(petsInRoomsButton);
        menuPanel.add(exitButton);

        mainPanel.add(menuPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void registerCustomer() {
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField petNameField = new JTextField();
        JTextField breedField = new JTextField();
        JTextField weightField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Dog", "Cat"});

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Customer name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Pet name:"));
        panel.add(petNameField);
        panel.add(new JLabel("Breed:"));
        panel.add(breedField);
        panel.add(new JLabel("Weight:"));
        panel.add(weightField);
        panel.add(new JLabel("Pet type:"));
        panel.add(typeBox);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Register Customer & Pet", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String phone = phoneField.getText().trim();
            Customer customer = customerController.findCustomerByPhone(phone);

            if (customer == null) {
                customer = new Customer(nameField.getText(), phone);
                customerController.addCustomer(customer);
            }

            double weight = Double.parseDouble(weightField.getText());
            String petId = customerController.createPetId();
            Pet pet;

            if (typeBox.getSelectedItem().equals("Dog")) {
                pet = new Dog(petId, petNameField.getText(),
                        breedField.getText(), weight);
            } else {
                pet = new Cat(petId, petNameField.getText(),
                        breedField.getText(), weight);
            }

            customerController.addPet(customer, pet);

            JOptionPane.showMessageDialog(this,
                    "Register successful.\n"
                    + "Customer: " + customer.getName() + "\n"
                    + "Pet: " + pet.getName() + "\n"
                    + "Pet ID: " + pet.getPetId());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Weight must be a number.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void bookHotel() {
        // Search customer first.
        String phone = JOptionPane.showInputDialog(this,
                "Enter customer phone number:", "Book Pet Hotel",
                JOptionPane.QUESTION_MESSAGE);

        if (phone == null || phone.trim().isEmpty()) {
            return;
        }

        Customer customer = customerController.findCustomerByPhone(phone.trim());

        if (customer == null) {
            JOptionPane.showMessageDialog(this,
                    "Customer not found. Please register first.");
            return;
        }

        ArrayList<Pet> pets = customerController.getPets(customer);

        if (pets.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No pet found for this customer.");
            return;
        }

        // Pet, date and room are in one form.
        String[] petNames = new String[pets.size()];
        for (int i = 0; i < pets.size(); i++) {
            Pet pet = pets.get(i);
            petNames[i] = pet.getName() + " - " + pet.getPetType()
                    + " (" + pet.getWeight() + " kg)";
        }

        JComboBox<String> petBox = new JComboBox<>(petNames);
        JTextField checkInField = new JTextField(
                LocalDate.now().plusDays(1).toString());
        JTextField checkOutField = new JTextField(
                LocalDate.now().plusDays(2).toString());
        JComboBox<String> roomBox = new JComboBox<>();
        JButton checkRoomsButton = new JButton("Check Available Rooms");
        JCheckBox walkingBox = new JCheckBox("Walking +100");
        JCheckBox groomingBox = new JCheckBox("Grooming +300");

        JPanel extraPanel = new JPanel(new GridLayout(2, 1));
        extraPanel.add(walkingBox);
        extraPanel.add(groomingBox);

        JPanel bookingPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        bookingPanel.add(new JLabel("Customer:"));
        bookingPanel.add(new JLabel(customer.getName()));
        bookingPanel.add(new JLabel("Pet:"));
        bookingPanel.add(petBox);
        bookingPanel.add(new JLabel("Check-in (yyyy-MM-dd):"));
        bookingPanel.add(checkInField);
        bookingPanel.add(new JLabel("Check-out (yyyy-MM-dd):"));
        bookingPanel.add(checkOutField);
        bookingPanel.add(new JLabel("Available room:"));
        bookingPanel.add(roomBox);
        bookingPanel.add(new JLabel(""));
        bookingPanel.add(checkRoomsButton);
        bookingPanel.add(new JLabel("Extra service:"));
        bookingPanel.add(extraPanel);

        Runnable updateRooms = () -> {
            roomBox.removeAllItems();

            try {
                LocalDate checkIn = LocalDate.parse(checkInField.getText().trim());
                LocalDate checkOut = LocalDate.parse(checkOutField.getText().trim());

                if (!checkOut.isAfter(checkIn)) {
                    roomBox.addItem("Check-out must be after check-in");
                    return;
                }

                Pet pet = pets.get(petBox.getSelectedIndex());
                ArrayList<Room> rooms = getAvailableRooms(pet, checkIn, checkOut);

                if (rooms.isEmpty()) {
                    roomBox.addItem("No available room");
                    return;
                }

                for (Room room : rooms) {
                    roomBox.addItem(room.getRoomId() + " - "
                            + room.getRoomName() + " ("
                            + room.getPricePerNight() + " baht/night)");
                }
            } catch (DateTimeParseException e) {
                roomBox.addItem("Invalid date");
            }
        };

        checkRoomsButton.addActionListener(e -> updateRooms.run());
        petBox.addActionListener(e -> updateRooms.run());
        updateRooms.run();

        int result = JOptionPane.showConfirmDialog(this, bookingPanel,
                "Book Pet Hotel - " + customer.getName(),
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            LocalDate checkIn = LocalDate.parse(checkInField.getText().trim());
            LocalDate checkOut = LocalDate.parse(checkOutField.getText().trim());

            if (!checkOut.isAfter(checkIn)) {
                JOptionPane.showMessageDialog(this,
                        "Check-out date must be after check-in date.");
                return;
            }

            Pet pet = pets.get(petBox.getSelectedIndex());
            ArrayList<Room> availableRooms = getAvailableRooms(pet, checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No available room for these dates.");
                return;
            }

            String roomText = (String) roomBox.getSelectedItem();
            Room selectedRoom = null;

            if (roomText != null) {
                for (Room room : availableRooms) {
                    if (roomText.startsWith(room.getRoomId() + " -")) {
                        selectedRoom = room;
                        break;
                    }
                }
            }

            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(this,
                        "Please click Check Available Rooms and select a room.");
                return;
            }

            Map<LocalDate, Room> roomTable = new LinkedHashMap<>();
            LocalDate date = checkIn;

            while (date.isBefore(checkOut)) {
                roomTable.put(date, selectedRoom);
                date = date.plusDays(1);
            }

            Booking booking = bookingController.createBooking(
                    customer, pet, roomTable, checkIn, checkOut,
                    walkingBox.isSelected(), groomingBox.isSelected());

            if (booking == null) {
                JOptionPane.showMessageDialog(this,
                        bookingController.getLastMessage());
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Booking successful.\n"
                    + "Booking ID: " + booking.getBookingId() + "\n"
                    + "Customer: " + customer.getName() + "\n"
                    + "Pet: " + pet.getName() + "\n"
                    + "Room: " + selectedRoom.getRoomId() + "\n"
                    + "Total: " + booking.getTotalPrice() + " baht");

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Date format must be yyyy-MM-dd.");
        }
    }

    private ArrayList<Room> getAvailableRooms(Pet pet,
            LocalDate checkIn, LocalDate checkOut) {

        ArrayList<Room> availableRooms = new ArrayList<>();

        for (Room room : roomController.getRooms()) {
            if (!room.canAccommodate(pet)) {
                continue;
            }

            boolean available = true;
            LocalDate date = checkIn;

            while (date.isBefore(checkOut)) {
                if (!roomController.isRoomAvailable(room, date)) {
                    available = false;
                    break;
                }
                date = date.plusDays(1);
            }

            if (available) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    private void showCustomerDetails() {
        String phone = JOptionPane.showInputDialog(this, "Phone number:");

        if (phone == null || phone.trim().isEmpty()) {
            return;
        }

        Customer customer = customerController.findCustomerByPhone(phone.trim());

        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found.");
            return;
        }

        String text = "Customer: " + customer.getName()
                + "\nPhone: " + customer.getPhoneNumber();

        ArrayList<Pet> pets = customerController.getPets(customer);
        LocalDate today = LocalDate.now();

        if (pets.isEmpty()) {
            text += "\n\nNo pets.";
        } else {
            text += "\n\nPets:";

            for (Pet pet : pets) {
                text += "\n\n- " + pet.getName()
                        + " | " + pet.getPetType()
                        + " | " + pet.getBreed()
                        + " | " + pet.getWeight() + " kg";

                Booking currentBooking = null;
                Room currentRoom = null;
                Booking upcomingBooking = null;

                for (Booking booking : bookingController.getBookings()) {
                    if (!booking.getPet().getPetId().equals(pet.getPetId())) {
                        continue;
                    }

                    Room roomToday = booking.getRoomAllocations().get(today);

                    if (roomToday != null) {
                        currentBooking = booking;
                        currentRoom = roomToday;
                        break;
                    }

                    if (booking.getCheckInDate().isAfter(today)) {
                        if (upcomingBooking == null
                                || booking.getCheckInDate().isBefore(
                                        upcomingBooking.getCheckInDate())) {
                            upcomingBooking = booking;
                        }
                    }
                }

                if (currentRoom != null) {
                    text += "\n  Room: " + currentRoom.getRoomId()
                            + " - " + currentRoom.getRoomName();
                    text += "\n  Check-out: " + currentBooking.getCheckOutDate();
                } else if (upcomingBooking != null) {
                    Room futureRoom = upcomingBooking.getRoomAllocations()
                            .get(upcomingBooking.getCheckInDate());

                    text += "\n  Room: Not staying yet";

                    if (futureRoom != null) {
                        text += "\n  Upcoming: " + futureRoom.getRoomId()
                                + " - " + futureRoom.getRoomName();
                    }

                    text += "\n  Check-in: " + upcomingBooking.getCheckInDate();
                } else {
                    text += "\n  Room: Not staying in a room";
                }
            }
        }

        JOptionPane.showMessageDialog(this, text,
                "Customer & Pet Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRooms() {
        if (roomController.getRooms().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No rooms found.");
            return;
        }

        String text = "";
        LocalDate today = LocalDate.now();

        for (Room room : roomController.getRooms()) {
            String status;

            if (roomController.isRoomAvailable(room, today)) {
                status = "AVAILABLE";
            } else {
                status = "FULL";
            }

            text += room.getRoomId() + " - " + room.getRoomName()
                    + " | " + room.getRoomType()
                    + " | " + room.getPricePerNight() + " baht/night"
                    + " | Max " + room.getMaxWeightLimit() + " kg"
                    + " | " + status + "\n";
        }

        JOptionPane.showMessageDialog(this, text, "Rooms",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAllBookings() {
        String phone = JOptionPane.showInputDialog(this,
                "Enter customer phone number:",
                "Show Bookings", JOptionPane.QUESTION_MESSAGE);

        if (phone == null || phone.trim().isEmpty()) {
            return;
        }

        Customer customer = customerController.findCustomerByPhone(phone.trim());

        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found.");
            return;
        }

        String text = "Customer: " + customer.getName()
                + "\nPhone: " + customer.getPhoneNumber()
                + "\n\nBookings:\n";
        boolean found = false;

        for (Booking booking : bookingController.getBookings()) {
            if (booking.getCustomer().getPhoneNumber()
                    .equals(customer.getPhoneNumber())) {

                text += booking.getBookingId()
                        + " | Pet: " + booking.getPet().getName()
                        + " | " + booking.getPet().getPetType()
                        + " | " + booking.getCheckInDate()
                        + " to " + booking.getCheckOutDate()
                        + " | " + booking.getTotalPrice() + " baht"
                        + " | " + bookingController.getStatus(booking) + "\n";
                found = true;
            }
        }

        if (!found) {
            text += "No bookings for this customer.";
        }

        JOptionPane.showMessageDialog(this, text,
                "Customer Bookings", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchCustomer() {
        String phone = JOptionPane.showInputDialog(this, "Phone number:");

        if (phone == null || phone.trim().isEmpty()) {
            return;
        }

        Customer customer = customerController.findCustomerByPhone(phone.trim());

        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found.");
            return;
        }

        ArrayList<Pet> pets = customerController.getPets(customer);

        String text = "Name: " + customer.getName()
                + "\nPhone: " + customer.getPhoneNumber()
                + "\nPets: " + pets.size() + "\n\n";

        if (pets.isEmpty()) {
            text += "No pets registered.";
        } else {
            for (int i = 0; i < pets.size(); i++) {
                Pet pet = pets.get(i);
                text += (i + 1) + ". " + pet.getPetType()
                        + " - " + pet.getName() + "\n";
            }
        }

        JOptionPane.showMessageDialog(this, text,
                "Customer Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showPetsInRooms() {
        String text = "";
        LocalDate today = LocalDate.now();

        for (Booking booking : bookingController.getBookings()) {
            Room room = booking.getRoomAllocations().get(today);

            if (room != null) {
                String roomType = room.getRoomType().replace("_", " ");

                text += room.getRoomId() + " - " + room.getRoomName()
                        + " | " + roomType + "\n"
                        + "Pet: " + booking.getPet().getName()
                        + " | " + booking.getPet().getPetType() + "\n"
                        + "Owner: " + booking.getCustomer().getName() + "\n"
                        + "Phone: " + booking.getCustomer().getPhoneNumber()
                        + "\n\n";
            }
        }

        if (text.isEmpty()) {
            text = "No pets staying today.";
        }

        JOptionPane.showMessageDialog(this, text, "Pets in Rooms",
                JOptionPane.INFORMATION_MESSAGE);
    }
}