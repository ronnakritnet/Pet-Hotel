package pethotel.model;

import java.util.ArrayList;

public class Customer {

    private String name;
    private String phoneNumber;
    private ArrayList<Pet> pets;

    public Customer(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        pets = new ArrayList<Pet>();
    }

    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<Pet> getPets() {
        return pets;
    }

    public void printInfo() {
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Pets:");

        for (Pet pet : pets) {
            pet.printInfo();
        }
    }
}
