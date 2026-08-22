package pethotel.repository;

import java.util.ArrayList;
import pethotel.model.Booking;
import pethotel.model.Customer;
import pethotel.model.Room;

public class DataManager {

    private ArrayList<Customer> customers;
    private ArrayList<Room> rooms;
    private ArrayList<Booking> bookings;

    public DataManager() {
        customers = new ArrayList<Customer>();
        rooms = new ArrayList<Room>();
        bookings = new ArrayList<Booking>();
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }
}
