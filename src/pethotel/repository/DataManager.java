package pethotel.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pethotel.model.Booking;
import pethotel.model.Cat;
import pethotel.model.CatRoom;
import pethotel.model.Customer;
import pethotel.model.Dog;
import pethotel.model.DogRoom;
import pethotel.model.Pet;
import pethotel.model.Room;

public class DataManager {

    private static DataManager instance;

    private ArrayList<Customer> customers;
    private ArrayList<Room> rooms;
    private ArrayList<Booking> bookings;
    private Map<String, ArrayList<Pet>> customerPets;
    private Path dataFolder;

    public DataManager() {
        this(Paths.get("src", "resources", "data"));
    }

    public DataManager(Path dataFolder) {
        this.dataFolder = dataFolder;
        customers = new ArrayList<Customer>();
        rooms = new ArrayList<Room>();
        bookings = new ArrayList<Booking>();
        customerPets = new LinkedHashMap<String, ArrayList<Pet>>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }

        return instance;
    }

    public void loadAllData() {
        loadData();
    }

    public void loadData() {
        try {
            Files.createDirectories(dataFolder);
            createFile("customers.json");
            createFile("rooms.json");
            createFile("bookings.json");

            loadRooms();
            loadCustomers();
            loadBookings();
        } catch (Exception e) {
            System.out.println("Cannot load JSON data: " + e.getMessage());
        }
    }

    private void createFile(String fileName) throws IOException {
        Path file = dataFolder.resolve(fileName);

        if (!Files.exists(file)) {
            Files.write(file, "[]\n".getBytes(StandardCharsets.UTF_8));
        }
    }

    private void loadRooms() throws IOException {
        rooms.clear();
        String json = readFile("rooms.json");

        for (String object : splitObjects(json)) {
            String id = getString(object, "id");
            String name = getString(object, "name");
            String type = getString(object, "type");
            double price = getDouble(object, "price");
            double maxWeight = getDouble(object, "maxWeight");

            if (id == null || name == null || type == null) {
                continue;
            }

            if ("DOG_ROOM".equals(type)) {
                rooms.add(new DogRoom(id, name, price, maxWeight));
            } else if ("CAT_ROOM".equals(type)) {
                rooms.add(new CatRoom(id, name, price, maxWeight));
            }
        }
    }

    private void loadCustomers() throws IOException {
        customers.clear();
        customerPets.clear();
        String json = readFile("customers.json");

        for (String object : splitObjects(json)) {
            String name = getString(object, "name");
            String phone = getString(object, "phone");

            if (name == null || phone == null) {
                continue;
            }

            Customer customer = new Customer(name, phone);
            customers.add(customer);

            ArrayList<Pet> pets = new ArrayList<Pet>();
            String petArray = getBlock(object, "pets", '[', ']');

            for (String petObject : splitObjects(petArray)) {
                String id = getString(petObject, "id");
                String petName = getString(petObject, "name");
                String breed = getString(petObject, "breed");
                double weight = getDouble(petObject, "weight");
                String type = getString(petObject, "type");

                if (id == null || petName == null || breed == null) {
                    continue;
                }

                if ("DOG".equals(type)) {
                    pets.add(new Dog(id, petName, breed, weight));
                } else if ("CAT".equals(type)) {
                    pets.add(new Cat(id, petName, breed, weight));
                }
            }

            customerPets.put(normalizePhone(phone), pets);
        }
    }

    private void loadBookings() throws IOException {
        bookings.clear();
        String json = readFile("bookings.json");

        for (String object : splitObjects(json)) {
            Customer customer = findCustomer(getString(object, "phone"));
            Pet pet = findPet(getString(object, "petId"));

            Map<LocalDate, Room> roomTable
                    = new LinkedHashMap<LocalDate, Room>();

            String roomObject = getBlock(object, "rooms", '{', '}');
            Pattern pairPattern = Pattern.compile(
                    "\"((?:\\\\.|[^\"\\\\])*)\"\\s*:\\s*"
                    + "\"((?:\\\\.|[^\"\\\\])*)\"");
            Matcher pairMatcher = pairPattern.matcher(roomObject);

            while (pairMatcher.find()) {
                LocalDate date = LocalDate.parse(
                        unescape(pairMatcher.group(1)));
                Room room = findRoom(
                        unescape(pairMatcher.group(2)));

                if (room != null) {
                    roomTable.put(date, room);
                }
            }

            String id = getString(object, "id");
            String checkInText = getString(object, "checkInDate");
            String checkOutText = getString(object, "checkOutDate");

            if (id == null || customer == null || pet == null
                    || roomTable.isEmpty()
                    || checkInText == null || checkOutText == null) {
                continue;
            }

            Booking booking = new Booking(
                    id,
                    customer,
                    pet,
                    roomTable,
                    LocalDate.parse(checkInText),
                    LocalDate.parse(checkOutText),
                    getBoolean(object, "walking"),
                    getBoolean(object, "grooming"));

            bookings.add(booking);
        }
    }

    public void saveAll() {
        saveCustomers();
        saveRooms();
        saveBookings();
    }

    public void saveCustomers() {
        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            ArrayList<Pet> pets = getPets(customer);

            json.append("  {\"name\":\"")
                    .append(escape(customer.getName()))
                    .append("\",\"phone\":\"")
                    .append(escape(customer.getPhoneNumber()))
                    .append("\",\"pets\":[");

            for (int j = 0; j < pets.size(); j++) {
                Pet pet = pets.get(j);

                if (j > 0) {
                    json.append(",");
                }

                json.append("{\"id\":\"")
                        .append(escape(pet.getPetId()))
                        .append("\",\"name\":\"")
                        .append(escape(pet.getName()))
                        .append("\",\"breed\":\"")
                        .append(escape(pet.getBreed()))
                        .append("\",\"weight\":")
                        .append(pet.getWeight())
                        .append(",\"type\":\"")
                        .append(pet.getPetType())
                        .append("\"}");
            }

            json.append("]}");

            if (i < customers.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]\n");
        writeFile("customers.json", json.toString());
    }

    public void saveRooms() {
        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);

            json.append("  {\"id\":\"")
                    .append(escape(room.getRoomId()))
                    .append("\",\"name\":\"")
                    .append(escape(room.getRoomName()))
                    .append("\",\"type\":\"")
                    .append(room.getRoomType())
                    .append("\",\"price\":")
                    .append(room.getPricePerNight())
                    .append(",\"maxWeight\":")
                    .append(room.getMaxWeightLimit())
                    .append("}");

            if (i < rooms.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]\n");
        writeFile("rooms.json", json.toString());
    }

    public void saveBookings() {
        rememberPetsFromBookings();
        saveCustomers();

        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);

            json.append("  {\"id\":\"")
                    .append(escape(booking.getBookingId()))
                    .append("\",\"phone\":\"")
                    .append(escape(booking.getCustomer().getPhoneNumber()))
                    .append("\",\"petId\":\"")
                    .append(escape(booking.getPet().getPetId()))
                    .append("\",\"checkInDate\":\"")
                    .append(booking.getCheckInDate())
                    .append("\",\"checkOutDate\":\"")
                    .append(booking.getCheckOutDate())
                    .append("\",\"walking\":")
                    .append(booking.isExtraWalking())
                    .append(",\"grooming\":")
                    .append(booking.isExtraGrooming())
                    .append(",\"total\":")
                    .append(booking.getTotalPrice())
                    .append(",\"rooms\":{");

            int roomNumber = 0;

            for (Map.Entry<LocalDate, Room> entry
                    : booking.getRoomAllocations().entrySet()) {

                if (roomNumber > 0) {
                    json.append(",");
                }

                json.append("\"")
                        .append(entry.getKey())
                        .append("\":\"")
                        .append(escape(entry.getValue().getRoomId()))
                        .append("\"");

                roomNumber++;
            }

            json.append("}}");

            if (i < bookings.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]\n");
        writeFile("bookings.json", json.toString());
    }

    public boolean addPet(Customer customer, Pet pet) {
        if (customer == null || pet == null) {
            return false;
        }

        String phone = normalizePhone(customer.getPhoneNumber());
        ArrayList<Pet> pets = customerPets.get(phone);

        if (pets == null) {
            pets = new ArrayList<Pet>();
            customerPets.put(phone, pets);
        }

        for (Pet oldPet : pets) {
            if (oldPet.getPetId().equalsIgnoreCase(pet.getPetId())) {
                return false;
            }
        }

        pets.add(pet);
        return true;
    }

    public ArrayList<Pet> getPets(Customer customer) {
        if (customer == null) {
            return new ArrayList<Pet>();
        }

        String phone = normalizePhone(customer.getPhoneNumber());
        ArrayList<Pet> pets = customerPets.get(phone);

        if (pets == null) {
            pets = new ArrayList<Pet>();
            customerPets.put(phone, pets);
        }

        return pets;
    }

    public Pet findPetById(String petId) {
        return findPet(petId);
    }

    private void rememberPetsFromBookings() {
        for (Booking booking : bookings) {
            addPet(booking.getCustomer(), booking.getPet());
        }
    }

    private String normalizePhone(String phone) {
        if (phone == null) {
            return "";
        }

        return phone.replaceAll("[^0-9]", "");
    }

    private String readFile(String fileName) throws IOException {
        byte[] data = Files.readAllBytes(dataFolder.resolve(fileName));
        return new String(data, StandardCharsets.UTF_8);
    }

    private void writeFile(String fileName, String text) {
        try {
            Files.createDirectories(dataFolder);
            Files.write(dataFolder.resolve(fileName),
                    text.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Cannot save " + fileName + ": "
                    + e.getMessage());
        }
    }

    private Customer findCustomer(String phone) {
        String searchPhone = normalizePhone(phone);

        for (Customer customer : customers) {
            if (normalizePhone(customer.getPhoneNumber()).equals(searchPhone)) {
                return customer;
            }
        }

        return null;
    }

    private Pet findPet(String petId) {
        if (petId == null) {
            return null;
        }

        for (ArrayList<Pet> pets : customerPets.values()) {
            for (Pet pet : pets) {
                if (pet.getPetId().equalsIgnoreCase(petId)) {
                    return pet;
                }
            }
        }

        return null;
    }

    private Room findRoom(String roomId) {
        if (roomId == null) {
            return null;
        }

        for (Room room : rooms) {
            if (room.getRoomId().equalsIgnoreCase(roomId)) {
                return room;
            }
        }

        return null;
    }

    private ArrayList<String> splitObjects(String text) {
        ArrayList<String> objects = new ArrayList<String>();

        if (text == null) {
            return objects;
        }

        int start = -1;
        int level = 0;
        boolean inString = false;
        boolean escaped = false;

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);

            if (inString) {
                if (escaped) {
                    escaped = false;
                } else if (current == '\\') {
                    escaped = true;
                } else if (current == '"') {
                    inString = false;
                }
                continue;
            }

            if (current == '"') {
                inString = true;
            } else if (current == '{') {
                if (level == 0) {
                    start = i;
                }
                level++;
            } else if (current == '}') {
                level--;

                if (level == 0 && start >= 0) {
                    objects.add(text.substring(start, i + 1));
                    start = -1;
                }
            }
        }

        return objects;
    }

    private String getString(String object, String key) {
        String pattern = "\"" + Pattern.quote(key)
                + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"";
        Matcher matcher = Pattern.compile(pattern).matcher(object);

        if (matcher.find()) {
            return unescape(matcher.group(1));
        }

        return null;
    }

    private double getDouble(String object, String key) {
        String pattern = "\"" + Pattern.quote(key)
                + "\"\\s*:\\s*(-?[0-9]+(?:\\.[0-9]+)?)";
        Matcher matcher = Pattern.compile(pattern).matcher(object);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        return 0.0;
    }

    private boolean getBoolean(String object, String key) {
        String pattern = "\"" + Pattern.quote(key)
                + "\"\\s*:\\s*(true|false)";
        Matcher matcher = Pattern.compile(pattern).matcher(object);

        return matcher.find()
                && Boolean.parseBoolean(matcher.group(1));
    }

    private String getBlock(String object, String key,
            char open, char close) {

        int keyPosition = object.indexOf("\"" + key + "\"");

        if (keyPosition < 0) {
            return "";
        }

        int start = object.indexOf(open, keyPosition);

        if (start < 0) {
            return "";
        }

        int level = 0;
        boolean inString = false;
        boolean escaped = false;

        for (int i = start; i < object.length(); i++) {
            char current = object.charAt(i);

            if (inString) {
                if (escaped) {
                    escaped = false;
                } else if (current == '\\') {
                    escaped = true;
                } else if (current == '"') {
                    inString = false;
                }
                continue;
            }

            if (current == '"') {
                inString = true;
            } else if (current == open) {
                level++;
            } else if (current == close) {
                level--;

                if (level == 0) {
                    return object.substring(start, i + 1);
                }
            }
        }

        return "";
    }

    private String escape(String text) {
        if (text == null) {
            return "";
        }

        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }

    private String unescape(String text) {
        if (text == null) {
            return null;
        }

        return text.replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public Path getDataFolder() {
        return dataFolder;
    }
}