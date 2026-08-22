package pethotel.model;

public class CatRoom extends Room{

    public CatRoom(String roomId, String roomName, double pricePerNight, double maxWeightLimit) {
        super(roomId, roomName, pricePerNight, maxWeightLimit);
    }
  
    @Override
    public String getRoomType(){
        return "CAT_ROOM";
    }
    
    @Override
    public boolean canAccommodate(Pet pet){
        if (pet == null) {
            return false;
        }
        
        if(!(pet instanceof Cat)) {
            return false;
        }
        
        return pet.getWeight() <= this.getMaxWeightLimit();
    }
}
