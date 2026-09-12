package pethotel.model;

public abstract class Pet {

    private String petId;
    private String name;
    private String breed;
    private double weight;

    public Pet(String petId, String name, String breed, double weight) {
        if (petId == null || petId.trim().isEmpty()) {
            throw new IllegalArgumentException("Pet ID is required");
        }
        this.petId = petId.trim();
        setName(name);
        setBreed(breed);
        setWeight(weight);
    }

    public abstract String getPetType();

    public String getPetId() {
        return petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Pet name is required");
        }
        this.name = name.trim();
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        if (breed == null || breed.trim().isEmpty()) {
            throw new IllegalArgumentException("Breed is required");
        }
        this.breed = breed.trim();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Pet weight must be > 0");
        }
        this.weight = weight;
    }

    @Override
    public String toString() {
        return name + " (" + getPetType() + ", " + breed + ", " + weight + " kg)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Pet)) return false;
        Pet pet = (Pet) o;
        return petId != null && petId.equals(pet.petId);
    }

    @Override
    public int hashCode() {
        return petId != null ? petId.hashCode() : 0;
    }
}
