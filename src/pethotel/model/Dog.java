package pethotel.model;

public class Dog extends Pet {

    public Dog(String petId, String name, String breed, double weight) {
        super(petId, name, breed, weight);
    }

    @Override
    public String getPetType() {
        return "DOG";
    }
}
