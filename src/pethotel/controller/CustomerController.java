package pethotel.controller;

import java.util.ArrayList;
import pethotel.model.Customer;
import pethotel.model.Pet;
import pethotel.repository.DataManager;

public class CustomerController {

    private DataManager dataManager;

    public CustomerController(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public boolean addCustomer(Customer customer) {
        if (customer == null) {
            return false;
        }

        if (findCustomerByPhone(customer.getPhoneNumber()) != null) {
            return false;
        }

        dataManager.getCustomers().add(customer);
        dataManager.saveCustomers();
        return true;
    }

    public Customer findCustomerByPhone(String phone) {
        if (phone == null) {
            return null;
        }

        String searchPhone = normalizePhone(phone);

        for (Customer customer : dataManager.getCustomers()) {
            if (normalizePhone(customer.getPhoneNumber()).equals(searchPhone)) {
                return customer;
            }
        }

        return null;
    }

    public boolean addPet(Customer customer, Pet pet) {
        if (customer == null || pet == null) {
            return false;
        }

        boolean added = dataManager.addPet(customer, pet);

        if (added) {
            dataManager.saveCustomers();
        }

        return added;
    }

    public ArrayList<Pet> getPets(Customer customer) {
        return dataManager.getPets(customer);
    }

    public String createPetId() {
        int number = 1;

        for (Customer customer : dataManager.getCustomers()) {
            for (Pet pet : dataManager.getPets(customer)) {
                String id = pet.getPetId();

                if (id != null && id.startsWith("P")) {
                    try {
                        int oldNumber = Integer.parseInt(id.substring(1));
                        if (oldNumber >= number) {
                            number = oldNumber + 1;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore IDs that are not in P<number> format.
                    }
                }
            }
        }

        return "P" + number;
    }

    public ArrayList<Customer> getCustomers() {
        return dataManager.getCustomers();
    }

    private String normalizePhone(String phone) {
        if (phone == null) {
            return "";
        }

        return phone.replaceAll("[^0-9]", "");
    }
}