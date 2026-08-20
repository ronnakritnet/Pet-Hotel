package pethotel.model;

public class CatRoom extends Room{

    public CatRoom(String roomId, String roomName, double pricePerNight) {
        super(roomId, roomName, pricePerNight);
    }
    
    @Override
    public String getRoomType(){
        return "CAT_ROOM";
    }
}
