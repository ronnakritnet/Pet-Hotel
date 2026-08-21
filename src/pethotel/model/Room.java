package pethotel.model;

public abstract class Room {
    protected String roomId;
    protected String roomName;
    protected double pricePerNight;
    protected double maxWeight;
    protected int capacity;

    public Room(String roomId, String roomName,double pricePerNight, double maxWeight) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.pricePerNight = pricePerNight;
        this.maxWeight = maxWeight;
        this.capacity = 5;
    }

    public abstract String getRoomType();
    public abstract boolean canAccommodate(Pet pet);
    public String getRoomId() {
        return roomId;
    }
    public String getRoomName() {
        return roomName;
    }
    public double getPricePerNight() {
        return pricePerNight;
    }
    public double getMaxWeight() {
        return maxWeight;
    }
    public int getCapacity() {
        return capacity;
    }
    public void printInfo() {
        System.out.println(roomId + " - " + roomName
                + " - " + pricePerNight + " baht/day"
                + " - Capacity: " + capacity);
    }
}
