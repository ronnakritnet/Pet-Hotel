package pethotel.model;

public class DogRoom extends Room {

    public DogRoom(String roomId, String roomName,double pricePerNight, double maxWeight) {
        super(roomId, roomName, pricePerNight, maxWeight);
    }
    @Override
    public String getRoomType() {
        return "Dog Room";
    }
    @Override
    public boolean canAccommodate(Pet pet) {
        return pet instanceof Dog && pet.getWeight() <= maxWeight;
    }
}
