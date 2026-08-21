package pethotel.model;

public class CatRoom extends Room {

    public CatRoom(String roomId, String roomName,
            double pricePerNight, double maxWeight) {
        super(roomId, roomName, pricePerNight, maxWeight);
    }

    @Override
    public String getRoomType() {
        return "Cat Room";
    }

    @Override
    public boolean canAccommodate(Pet pet) {
        return pet instanceof Cat && pet.getWeight() <= maxWeight;
    }
}
