package pethotel.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import pethotel.model.Booking;
import pethotel.model.Customer;
import pethotel.model.Pet;
import pethotel.model.Room;
import pethotel.repository.DataManager;

public class BookingController {

    private DataManager dataManager;
    private RoomController roomController;
    private String lastMessage = "";

    public BookingController(DataManager dataManager,
            CustomerController customerController,
            RoomController roomController) {
        this.dataManager = dataManager;
        this.roomController = roomController;
    }

    public BookingController(DataManager dataManager,
            RoomController roomController) {
        this(dataManager, null, roomController);
    }

    public Booking createBooking(Customer customer, Pet pet, Room room,
            LocalDate checkInDate, LocalDate checkOutDate,
            boolean walking, boolean grooming) {

        if (room == null) {
            lastMessage = "Please select a room.";
            return null;
        }

        if (checkInDate == null || checkOutDate == null) {
            lastMessage = "Please enter check-in and check-out dates.";
            return null;
        }

        Map<LocalDate, Room> roomTable
                = new LinkedHashMap<LocalDate, Room>();

        LocalDate date = checkInDate;

        while (date.isBefore(checkOutDate)) {
            roomTable.put(date, room);
            date = date.plusDays(1);
        }

        return createBooking(customer, pet, roomTable,
                checkInDate, checkOutDate, walking, grooming);
    }

    public Booking createBooking(Customer customer, Pet pet,
            Map<LocalDate, Room> roomTable,
            LocalDate checkInDate, LocalDate checkOutDate,
            boolean walking, boolean grooming) {

        if (customer == null || pet == null) {
            lastMessage = "Please select customer and pet.";
            return null;
        }

        if (checkInDate == null || checkOutDate == null) {
            lastMessage = "Please enter check-in and check-out dates.";
            return null;
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            lastMessage = "Check-out date must be after check-in date.";
            return null;
        }

        if (roomTable == null || roomTable.isEmpty()) {
            lastMessage = "Please select rooms for the booking.";
            return null;
        }

        if (hasOverlappingBooking(pet, checkInDate, checkOutDate)) {
            lastMessage = "This pet already has a booking in this date range.";
            return null;
        }

        LocalDate date = checkInDate;

        while (date.isBefore(checkOutDate)) {
            Room room = roomTable.get(date);

            if (room == null) {
                lastMessage = "Please select a room for " + date + ".";
                return null;
            }

            if (!room.canAccommodate(pet)) {
                lastMessage = "Room " + room.getRoomId()
                        + " is not suitable for this pet.";
                return null;
            }

            if (!roomController.isRoomAvailable(room, date)) {
                lastMessage = "Room " + room.getRoomId()
                        + " is already booked on " + date + ".";
                return null;
            }

            date = date.plusDays(1);
        }

        String bookingId = createBookingId();

        Booking booking = new Booking(
                bookingId,
                customer,
                pet,
                roomTable,
                checkInDate,
                checkOutDate,
                walking,
                grooming);

        dataManager.getBookings().add(booking);
        dataManager.addPet(customer, pet);
        dataManager.saveBookings();

        lastMessage = "Booking success: " + bookingId;
        return booking;
    }

    public double calculateTotal(Map<LocalDate, Room> roomTable,
            boolean walking, boolean grooming) {

        double total = 0.0;

        if (roomTable != null) {
            for (Room room : roomTable.values()) {
                total += room.getPricePerNight();
            }
        }

        if (walking) {
            total += 100.0;
        }

        if (grooming) {
            total += 300.0;
        }

        return total;
    }

    private String createBookingId() {
        int number = 1;

        for (Booking booking : dataManager.getBookings()) {
            String id = booking.getBookingId();

            if (id != null && id.startsWith("BK")) {
                try {
                    int oldNumber = Integer.parseInt(id.substring(2));

                    if (oldNumber >= number) {
                        number = oldNumber + 1;
                    }
                } catch (NumberFormatException e) {
                    // Ignore IDs that are not in BK<number> format.
                }
            }
        }

        return "BK" + number;
    }

    private boolean hasOverlappingBooking(Pet pet,
            LocalDate checkInDate, LocalDate checkOutDate) {

        for (Booking booking : dataManager.getBookings()) {
            if (!booking.getPet().getPetId()
                    .equalsIgnoreCase(pet.getPetId())) {
                continue;
            }

            boolean overlap
                    = checkInDate.isBefore(booking.getCheckOutDate())
                    && checkOutDate.isAfter(booking.getCheckInDate());

            if (overlap) {
                return true;
            }
        }

        return false;
    }

    public static String getStatus(Booking booking) {
        LocalDate today = LocalDate.now();

        if (today.isBefore(booking.getCheckInDate())) {
            return "UPCOMING";
        }

        if (today.isBefore(booking.getCheckOutDate())) {
            return "STAYING NOW";
        }

        return "COMPLETED";
    }

    public Booking findBooking(String bookingId) {
        if (bookingId == null) {
            return null;
        }

        for (Booking booking : dataManager.getBookings()) {
            if (booking.getBookingId().equalsIgnoreCase(bookingId)) {
                return booking;
            }
        }

        return null;
    }

    public String getBookingText(Booking booking) {
        if (booking == null) {
            return "";
        }

        StringBuilder text = new StringBuilder();

        text.append("Booking ID: ").append(booking.getBookingId()).append("\n")
                .append("Customer: ").append(booking.getCustomer().getName())
                .append(" | Phone: ")
                .append(booking.getCustomer().getPhoneNumber()).append("\n")
                .append("Pet: ").append(booking.getPet().getName())
                .append(" | Breed: ").append(booking.getPet().getBreed()).append("\n")
                .append("Check-in: ").append(booking.getCheckInDate()).append("\n")
                .append("Check-out: ").append(booking.getCheckOutDate()).append("\n")
                .append("Rooms: ");

        for (Map.Entry<LocalDate, Room> entry
                : booking.getRoomAllocations().entrySet()) {
            text.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue().getRoomId())
                    .append(" ");
        }

        text.append("\nStatus: ")
                .append(getStatus(booking))
                .append(" | Total: ")
                .append(booking.getTotalPrice())
                .append(" baht\n");

        return text.toString();
    }

    public String getAllBookingsText() {
        if (dataManager.getBookings().isEmpty()) {
            return "No bookings.";
        }

        StringBuilder text = new StringBuilder();

        for (Booking booking : dataManager.getBookings()) {
            text.append(getBookingText(booking))
                    .append("--------------------------------\n");
        }

        return text.toString();
    }

    public String getPetsInRoomsText() {
        StringBuilder text = new StringBuilder();
        LocalDate today = LocalDate.now();

        for (Room room : roomController.getRooms()) {
            text.append("Room ").append(room.getRoomId()).append("\n");
            boolean found = false;

            for (Booking booking : dataManager.getBookings()) {
                Room bookedRoom = booking.getRoomAllocations().get(today);

                if (bookedRoom != null
                        && bookedRoom.getRoomId().equalsIgnoreCase(room.getRoomId())
                        && "STAYING NOW".equals(getStatus(booking))) {

                    text.append("- ")
                            .append(booking.getPet().getName())
                            .append(" | until ")
                            .append(booking.getCheckOutDate())
                            .append("\n");
                    found = true;
                }
            }

            if (!found) {
                text.append("- No pets\n");
            }

            text.append("\n");
        }

        return text.toString();
    }

    public ArrayList<Booking> getBookings() {
        return dataManager.getBookings();
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void showAllBookings() {
        System.out.println(getAllBookingsText());
    }
}