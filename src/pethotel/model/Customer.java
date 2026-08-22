package pethotel.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private String name;
    private String phoneNumber;
    private List<Pet> pets;

    public Customer(String name, String phoneNumber) {

        setName(name);
        setPhoneNumber(phoneNumber);
        this.pets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        this.name = name.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer phone number is required");
        }

        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        if (digitsOnly.length() < 9 || digitsOnly.length() > 10) {
            throw new IllegalArgumentException("Phone number must be between 9 and 10 digits");
        }

        this.phoneNumber = phoneNumber;
    }

}
