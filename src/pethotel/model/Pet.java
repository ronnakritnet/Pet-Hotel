package pethotel.model;

public abstract class Pet {
    protected String petId;
    protected String name;
    protected String breed;
    protected double weight;

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
    public String getBreed() {
        return breed;
    }
    public double getWeight() {
        return weight;
    }
    public void printInfo() {
        System.out.println(petId + " - " + name
                + " / " + getPetType()
                + " / Breed: " + breed
                + " / " + weight + " kg");
    }
}
