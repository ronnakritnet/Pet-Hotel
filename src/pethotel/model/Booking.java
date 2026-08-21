package pethotel.model;

import java.time.LocalDate;
import java.util.Map;

public class Booking {

    private String bookingId;
    private Customer customer;
    private Pet pet;

    private Map<LocalDate, Room> roomAllocations;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;

    public Booking(String bookingId, Customer customer, Pet pet, Map<LocalDate, Room> roomAllocations, LocalDate checkInDate, LocalDate checkOutDate) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.pet = pet;
        this.roomAllocations = roomAllocations;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        double sum = 0.0;
        if (roomAllocations != null) {
            for (Room room : roomAllocations.values()) {
                sum += room.getPricePerNight();
            }
        }
        return sum;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Map<LocalDate, Room> getRoomAllocations() {
        return roomAllocations;
    }

    public void setRoomAllocations(Map<LocalDate, Room> roomAllocations) {
        this.roomAllocations = roomAllocations;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

}
