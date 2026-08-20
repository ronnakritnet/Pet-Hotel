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
    
    
}
