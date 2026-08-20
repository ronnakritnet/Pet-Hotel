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
}

