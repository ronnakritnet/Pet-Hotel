package repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import model.Booking;
import model.Customer;
import model.Room;

public class DataManager {

    private static final String DATA_DIR = "resources/data/";
    private static final String BOOKINGS_FILE = DATA_DIR + "bookings.json";
    private static final String CUSTOMERS_FILE = DATA_DIR + "customers.json";
    private static final String ROOMS_FILE = DATA_DIR + "rooms.json";

    private ArrayList<Booking> bookingsList;
    private ArrayList<Customer> customersList;
    private ArrayList<Room> roomsList;

    public DataManager() {
        this.bookingsList = new ArrayList<>();
        this.customersList = new ArrayList<>();
        this.roomsList = new ArrayList<>();
    }

    public void loadAllData() {
        System.out.println("DataManager: Loading JSON files into RAM...");
        
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        this.customersList = loadListFromFile(CUSTOMERS_FILE, Customer.class);
        this.roomsList = loadListFromFile(ROOMS_FILE, Room.class);
        this.bookingsList = loadListFromFile(BOOKINGS_FILE, Booking.class);

        System.out.println("DataManager: Data loading completed.");
    }

    private <T> ArrayList<T> loadListFromFile(String filePath, Class<T> classType) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("DataManager: File " + filePath + " not found. Initializing empty collection.");
            return new ArrayList<>();
        }

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
            Type type = TypeToken.getParameterized(ArrayList.class, classType).getType();
            ArrayList<T> list = new Gson().fromJson(jsonContent, type);
            if (list == null) {
                return new ArrayList<>();
            }
            System.out.println("DataManager: Loaded content from " + filePath);
            return list;
        } catch (IOException e) {
            System.err.println("DataManager Error: Cannot read file " + filePath + " - " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public ArrayList<Booking> getBookings() {
        return this.bookingsList;
    }

    public ArrayList<Customer> getCustomers() {
        return this.customersList;
    }

    public ArrayList<Room> getRooms() {
        return this.roomsList;
    }

    public synchronized boolean saveBooking(Booking newBooking) {
        this.bookingsList.add(newBooking);
        return writeToFile(BOOKINGS_FILE, this.bookingsList);
    }

    public synchronized boolean saveCustomer(Customer updatedCustomer) {
        int index = -1;
        for (int i = 0; i < customersList.size(); i++) {
            if (customersList.get(i).getPhoneNumber().equals(updatedCustomer.getPhoneNumber())) {
                index = i;
                break;
            }
        }
        
        if (index != -1) {
            customersList.set(index, updatedCustomer);
        } else {
            customersList.add(updatedCustomer);
        }

        return writeToFile(CUSTOMERS_FILE, this.customersList);
    }

    private boolean writeToFile(String filePath, Object listToWrite) {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(listToWrite, writer);
            System.out.println("DataManager: Successfully wrote data to " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("DataManager Error: Cannot write to file " + filePath + " - " + e.getMessage());
            return false;
        }
    }
}
