package pethotel.model;

public class Cat extends Pet {

    public Cat(String petId, String name, String breed, double weight) {
        super(petId, name, breed, weight);
    }
    @Override
    public String getPetType() {
        return "CAT";
    }
    
}