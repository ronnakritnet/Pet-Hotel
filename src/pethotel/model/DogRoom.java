package pethotel.model;

public class DogRoom extends Room{

    public DogRoom(String roomId, String roomName, double pricePerNight) {
        super(roomId, roomName, pricePerNight);
    }
    
    @Override
    public String getRoomType(){
        return "DOG_ROOM";
    }
}
