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

    public abstract String getRoomType();
    
    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    
}
