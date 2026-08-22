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
        if (findCustomerByPhone(customer.getPhoneNumber()) != null) {
            return false;
        }

        dataManager.getCustomers().add(customer);
        return true;
    }

    public Customer findCustomerByPhone(String phone) {
        for (Customer customer : dataManager.getCustomers()) {
            if (customer.getPhoneNumber().equals(phone)) {
                return customer;
            }
        }
        return null;
    }

    public void addPet(Customer customer, Pet pet) {
        customer.addPet(pet);
    }

    public String createPetId() {
        int total = 0;

        for (Customer customer : dataManager.getCustomers()) {
            total += customer.getPets().size();
        }
        return "P" + (total + 1);
    }

    public ArrayList<Customer> getCustomers() {
        return dataManager.getCustomers();
    }
}
