package pethotel.repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import pethotel.model.Booking;
import pethotel.model.CatRoom;
import pethotel.model.Customer;
import pethotel.model.DogRoom;
import pethotel.model.Pet;
import pethotel.model.Room;

public class DataManager {

    private static final String DATA_DIR = "resources/data/";
    private static final String BOOKINGS_FILE = DATA_DIR + "bookings.json";
    private static final String CUSTOMERS_FILE = DATA_DIR + "customers.json";
    private static final String ROOMS_FILE = DATA_DIR + "rooms.json";

    private static DataManager instance;

    private ArrayList<Booking> bookingsList;
    private ArrayList<Customer> customersList;
    private ArrayList<Room> roomsList;
    private Map<String, ArrayList<Pet>> customerPets;

    public DataManager() {
        bookingsList = new ArrayList<>();
        customersList = new ArrayList<>();
        roomsList = new ArrayList<>();
        customerPets = new HashMap<>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void loadAllData() {
        System.out.println("DataManager: Loading JSON data...");

        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        customersList = loadListFromFile(CUSTOMERS_FILE, Customer.class);
        roomsList = loadListFromFile(ROOMS_FILE, Room.class);
        bookingsList = loadListFromFile(BOOKINGS_FILE, Booking.class);
        customerPets.clear();

        if (roomsList.isEmpty()) {
            createDefaultRooms();
        }

        System.out.println("DataManager: Data loading finished.");
    }

    private void createDefaultRooms() {
        roomsList.add(new DogRoom("D01", "Dog Room 1", 500, 20));
        roomsList.add(new DogRoom("D02", "Dog Room 2", 650, 50));
        roomsList.add(new CatRoom("C01", "Cat Room 1", 400, 8));
        roomsList.add(new CatRoom("C02", "Cat Room 2", 500, 15));
    }

    private <T> ArrayList<T> loadListFromFile(String filePath, Class<T> classType) {
        File file = new File(filePath);

        if (!file.exists()) {
            createEmptyFile(filePath);
            return new ArrayList<>();
        }

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

            if (jsonContent.trim().isEmpty()) {
                return new ArrayList<>();
            }

            // Basic version: JSON parsing is not added yet.
            return new ArrayList<>();

        } catch (IOException e) {
            System.err.println("DataManager Error: Cannot read "
                    + filePath + " - " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void createEmptyFile(String filePath) {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
            }
        } catch (IOException e) {
            System.err.println("DataManager Error: Cannot create "
                    + filePath + " - " + e.getMessage());
        }
    }

    public ArrayList<Booking> getBookings() {
        return bookingsList;
    }

    public ArrayList<Customer> getCustomers() {
        return customersList;
    }

    public ArrayList<Room> getRooms() {
        return roomsList;
    }

    public synchronized boolean saveBooking(Booking newBooking) {
        if (newBooking == null) {
            return false;
        }

        bookingsList.add(newBooking);
        return writeToFile(BOOKINGS_FILE);
    }

    public synchronized boolean saveCustomer(Customer updatedCustomer) {
        if (updatedCustomer == null) {
            return false;
        }

        int index = -1;

        for (int i = 0; i < customersList.size(); i++) {
            if (customersList.get(i).getPhoneNumber()
                    .equals(updatedCustomer.getPhoneNumber())) {
                index = i;
                break;
            }
        }

        if (index >= 0) {
            customersList.set(index, updatedCustomer);
        } else {
            customersList.add(updatedCustomer);
        }

        return writeToFile(CUSTOMERS_FILE);
    }

    public void savePet(Customer customer, Pet pet) {
        if (customer == null || pet == null) {
            return;
        }

        String phone = customer.getPhoneNumber();
        ArrayList<Pet> pets = customerPets.get(phone);

        if (pets == null) {
            pets = new ArrayList<>();
            customerPets.put(phone, pets);
        }

        pets.add(pet);
    }

    public ArrayList<Pet> getPets(Customer customer) {
        if (customer == null) {
            return new ArrayList<>();
        }

        ArrayList<Pet> pets = customerPets.get(customer.getPhoneNumber());
        if (pets == null) {
            return new ArrayList<>();
        }

        return pets;
    }

    public ArrayList<Pet> getAllPets() {
        ArrayList<Pet> allPets = new ArrayList<>();

        for (ArrayList<Pet> pets : customerPets.values()) {
            allPets.addAll(pets);
        }

        return allPets;
    }

    private boolean writeToFile(String filePath) {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try (FileWriter writer = new FileWriter(file)) {
                // Basic repository version. JSON conversion is not added yet.
                writer.write("[]");
            }

            return true;

        } catch (IOException e) {
            System.err.println("DataManager Error: Cannot write "
                    + filePath + " - " + e.getMessage());
            return false;
        }
    }
}
