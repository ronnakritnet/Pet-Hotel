package pethotel.model;

public abstract class Room {
    private String roomId;
    private String roomName;
    private double pricePerNight;

    public Room(String roomId, String roomName, double pricePerNight) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.pricePerNight = pricePerNight;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
    
    public abstract String getRoomType();
}
