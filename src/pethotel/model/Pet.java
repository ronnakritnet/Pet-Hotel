package pethotel.model;

public abstract class Pet {

    private String petId;
    private String name;
    private String breed;
    private double weight;

    public Pet(String petId, String name, String breed, double weight) {
        this.petId = petId;
        this.name = name;
        this.breed = breed;
        this.weight = weight;
    }

    public abstract String getPetType();

    public String getPetId() {
        return petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}
