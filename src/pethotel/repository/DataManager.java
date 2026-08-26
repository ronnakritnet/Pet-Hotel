package pethotel.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

 
public class DataManager {

    private static final String DATA_DIR = "src/resources/data/";
    private static final String BOOKINGS_FILE = DATA_DIR + "bookings.json";
    private static final String CUSTOMERS_FILE = DATA_DIR + "customers.json";
    private static final String ROOMS_FILE = DATA_DIR + "rooms.json";

    private final Gson gson;

    private List<Object> bookings;  
    private List<Object> customers; 
    private List<Object> rooms;     

    public DataManager() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
        
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        loadAllData();
    }

    private void loadAllData() {
        System.out.println("[DataManager] Loading all data into memory (RAM)...");
        
        this.customers = loadFromFile(CUSTOMERS_FILE, new TypeToken<ArrayList<Object>>() {}.getType());
        System.out.println("[DataManager] Customer data loaded successfully: " + customers.size() + " records");

        this.rooms = loadFromFile(ROOMS_FILE, new TypeToken<ArrayList<Object>>() {}.getType());
        System.out.println("[DataManager] Room data loaded successfully: " + rooms.size() + " records");

        this.bookings = loadFromFile(BOOKINGS_FILE, new TypeToken<ArrayList<Object>>() {}.getType());
        System.out.println("[DataManager] Booking data loaded successfully: " + bookings.size() + " records");
    }

    private <T> List<T> loadFromFile(String filePath, Type typeOfT) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[DataManager] Data file not found at " + filePath + ". Starting with an empty list.");
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            List<T> list = gson.fromJson(reader, typeOfT);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("[DataManager] Error reading file " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveToFile(String filePath, List<?> dataList) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(dataList, writer);
            System.out.println("[DataManager] Data successfully saved to " + filePath + "!");
        } catch (IOException e) {
            System.err.println("[DataManager] Error saving file " + filePath + ": " + e.getMessage());
        }
    }

    public List<Object> getBookings() {
        return bookings;
    }

    public synchronized void saveBookings(List<Object> bookings) {
        this.bookings = bookings;
        saveToFile(BOOKINGS_FILE, this.bookings);
    }

    public List<Object> getCustomers() {
        return customers;
    }

    public synchronized void saveCustomers(List<Object> customers) {
        this.customers = customers;
        saveToFile(CUSTOMERS_FILE, this.customers);
    }

    public List<Object> getRooms() {
        return rooms;
    }

    public synchronized void saveRooms(List<Object> rooms) {
        this.rooms = rooms;
        saveToFile(ROOMS_FILE, this.rooms);
    }

    private static class LocalDateAdapter extends com.google.gson.TypeAdapter<LocalDate> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public void write(com.google.gson.stream.JsonWriter out, LocalDate value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(formatter.format(value));
            }
        }

        @Override
        public LocalDate read(com.google.gson.stream.JsonReader in) throws IOException {
            if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                return LocalDate.parse(in.nextString(), formatter);
            }
        }
    }
}
