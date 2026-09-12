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

import pethotel.model.Booking;
import pethotel.model.Customer;
import pethotel.model.Room;
import pethotel.model.Pet;
import pethotel.model.Dog;
import pethotel.model.Cat;
import pethotel.model.DogRoom;
import pethotel.model.CatRoom;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;

public class DataManager {

    private static DataManager instance;

    private static final String DATA_DIR = "src/resources/data/";
    private static final String BOOKINGS_FILE = DATA_DIR + "bookings.json";
    private static final String CUSTOMERS_FILE = DATA_DIR + "customers.json";
    private static final String ROOMS_FILE = DATA_DIR + "rooms.json";

    private final Gson gson;

    private ArrayList<Booking> bookings;  
    private ArrayList<Customer> customers; 
    private ArrayList<Room> rooms;     

    public DataManager() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(Pet.class, new PetAdapter())
                .registerTypeAdapter(Room.class, new RoomAdapter())
                .setPrettyPrinting()
                .create();
        
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        loadAllData();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void loadAllData() {
        System.out.println("[DataManager] Loading all data into memory (RAM)...");
        
        this.customers = (ArrayList<Customer>) loadFromFile(CUSTOMERS_FILE, new TypeToken<ArrayList<Customer>>() {}.getType());
        System.out.println("[DataManager] Customer data loaded successfully: " + customers.size() + " records");

        this.rooms = (ArrayList<Room>) loadFromFile(ROOMS_FILE, new TypeToken<ArrayList<Room>>() {}.getType());
        System.out.println("[DataManager] Room data loaded successfully: " + rooms.size() + " records");

        this.bookings = (ArrayList<Booking>) loadFromFile(BOOKINGS_FILE, new TypeToken<ArrayList<Booking>>() {}.getType());
        System.out.println("[DataManager] Booking data loaded successfully: " + bookings.size() + " records");
    }

    private Object loadFromFile(String filePath, Type typeOfT) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[DataManager] Data file not found at " + filePath + ". Starting with an empty list.");
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Object list = gson.fromJson(reader, typeOfT);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("[DataManager] Error reading file " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveToFile(String filePath, Object dataList) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(dataList, writer);
            System.out.println("[DataManager] Data successfully saved to " + filePath + "!");
        } catch (IOException e) {
            System.err.println("[DataManager] Error saving file " + filePath + ": " + e.getMessage());
        }
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public synchronized boolean saveBooking(Booking booking) {
        this.bookings.add(booking);
        saveToFile(BOOKINGS_FILE, this.bookings);
        return true;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public synchronized boolean saveCustomer(Customer customer) {
        this.customers.add(customer);
        saveToFile(CUSTOMERS_FILE, this.customers);
        return true;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<Pet> getPets(Customer customer) {
        if (customer.getPets() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(customer.getPets());
    }

    public ArrayList<Pet> getAllPets() {
        ArrayList<Pet> all = new ArrayList<>();
        for (Customer c : customers) {
            if (c.getPets() != null) {
                all.addAll(c.getPets());
            }
        }
        return all;
    }

    public synchronized void savePet(Customer customer, Pet pet) {
        if (!customer.getPets().contains(pet)) {
            customer.addPet(pet);
        }
        saveToFile(CUSTOMERS_FILE, this.customers);
    }

    public synchronized void saveCustomers() {
        saveToFile(CUSTOMERS_FILE, this.customers);
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

    private static class PetAdapter implements JsonSerializer<Pet>, JsonDeserializer<Pet> {
        @Override
        public JsonElement serialize(Pet src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = context.serialize(src, src.getClass()).getAsJsonObject();
            obj.addProperty("type", src instanceof Dog ? "DOG" : "CAT");
            return obj;
        }

        @Override
        public Pet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String type = null;
            if (obj.has("type") && !obj.get("type").isJsonNull()) {
                type = obj.get("type").getAsString();
            } else if (obj.has("petType") && !obj.get("petType").isJsonNull()) {
                type = obj.get("petType").getAsString();
            }
            if (type != null) {
                if ("DOG".equalsIgnoreCase(type)) {
                    return context.deserialize(json, Dog.class);
                } else if ("CAT".equalsIgnoreCase(type)) {
                    return context.deserialize(json, Cat.class);
                }
            }
            throw new JsonParseException("Unknown element type for Pet: " + obj);
        }
    }

    private static class RoomAdapter implements JsonSerializer<Room>, JsonDeserializer<Room> {
        @Override
        public JsonElement serialize(Room src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = context.serialize(src, src.getClass()).getAsJsonObject();
            obj.addProperty("type", src instanceof DogRoom ? "DOG_ROOM" : "CAT_ROOM");
            return obj;
        }

        @Override
        public Room deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            if (obj.has("type")) {
                String type = obj.get("type").getAsString();
                if ("DOG_ROOM".equals(type)) {
                    return context.deserialize(json, DogRoom.class);
                } else if ("CAT_ROOM".equals(type)) {
                    return context.deserialize(json, CatRoom.class);
                }
            }
            // fallback if type is missing (e.g. from an old json)
            if (obj.has("roomName")) {
                String name = obj.get("roomName").getAsString().toLowerCase();
                if (name.contains("cat")) return context.deserialize(json, CatRoom.class);
            }
            return context.deserialize(json, DogRoom.class); // default
        }
    }
}
