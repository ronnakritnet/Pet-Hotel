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

        return dataManager.saveCustomer(customer);
    }

    public Customer findCustomerByPhone(String phone) {
        if (phone == null) {
            return null;
        }

        String searchPhone = phone.replaceAll("[^0-9]", "");

        for (Customer customer : dataManager.getCustomers()) {
            String customerPhone = customer.getPhoneNumber()
                    .replaceAll("[^0-9]", "");

            if (customerPhone.equals(searchPhone)) {
                return customer;
            }
        }

        return null;
    }

    public void addPet(Customer customer, Pet pet) {
        dataManager.savePet(customer, pet);
    }

    public ArrayList<Pet> getPets(Customer customer) {
        return dataManager.getPets(customer);
    }

    public String createPetId() {
        int number = 1;

        for (Pet pet : dataManager.getAllPets()) {
            String id = pet.getPetId();

            if (id != null && id.startsWith("P")) {
                try {
                    int oldNumber = Integer.parseInt(id.substring(1));
                    if (oldNumber >= number) {
                        number = oldNumber + 1;
                    }
                } catch (NumberFormatException e) {
                    // Ignore IDs that are not P<number>.
                }
            }
        }

        return "P" + number;
    }

    public ArrayList<Customer> getCustomers() {
        return dataManager.getCustomers();
    }
}
