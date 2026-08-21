package pethotel.model;

public abstract class Room {

    private String roomId;
    private String roomName;
    private double pricePerNight;
    private double maxWeightLimit;

    public Room(String roomId, String roomName, double pricePerNight, double maxWeightLimit) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID is required");
        }
        this.roomId = roomId.trim();
        setRoomName(roomName);
        setPricePerNight(pricePerNight);
        setMaxWeightLimit(maxWeightLimit);
    }

    public abstract String getRoomType();

    public abstract boolean canAccommodate(Pet pet);

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        if (roomName == null || roomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name is required");
        }
        this.roomName = roomName.trim();;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        if (pricePerNight <= 0) {
            throw new IllegalArgumentException("Price per night must be > 0");
        }
        this.pricePerNight = pricePerNight;
    }

    public double getMaxWeightLimit() {
        return maxWeightLimit;
    }

    public void setMaxWeightLimit(double maxWeightLimit) {
        if (maxWeightLimit <= 0) {
            throw new IllegalArgumentException("Max weight limit must be > 0");
        }
        this.maxWeightLimit = maxWeightLimit;
    }

}
