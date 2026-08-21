package pethotel.model;

public class DogRoom extends Room {

    public DogRoom(String roomId, String roomName, double pricePerNight, double maxWeightLimit) {
        super(roomId, roomName, pricePerNight, maxWeightLimit);
    }

    @Override
    public String getRoomType() {
        return "DOG_ROOM";
    }

    @Override
    public boolean canAccommodate(Pet pet) {
        if (pet == null) {
            return false;
        }
        
        if(!(pet instanceof Dog)) {
            return false;
        }
        
        return pet.getWeight() <= this.getMaxWeightLimit();
    }
}
