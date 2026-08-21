package pethotel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class Booking {

    private String bookingId;
    private Customer customer;
    private Pet pet;
    private Map<LocalDate, Room> roomAllocations;
    private LocalDate checkInDate;
    private LocalTime checkInTime;
    private LocalDate checkOutDate;
    private LocalTime checkOutTime;
    private boolean extraWalking;
    private boolean extraGrooming;
    private double totalPrice;
    private LocalDateTime actualCheckOut;

    public Booking(String bookingId, Customer customer, Pet pet,
            Map<LocalDate, Room> roomAllocations,
            LocalDate checkInDate, LocalTime checkInTime,
            LocalDate checkOutDate, LocalTime checkOutTime,
            boolean extraWalking, boolean extraGrooming) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.pet = pet;
        this.roomAllocations = roomAllocations;
        this.checkInDate = checkInDate;
        this.checkInTime = checkInTime;
        this.checkOutDate = checkOutDate;
        this.checkOutTime = checkOutTime;
        this.extraWalking = extraWalking;
        this.extraGrooming = extraGrooming;
        this.totalPrice = calculatePrice();
    }
    private double calculatePrice() {
        double total = 0;
        for (Room room : roomAllocations.values()) {
            total += room.getPricePerNight();
        }
        if (extraWalking) {
            total += 100;
        }
        if (extraGrooming) {
            total += 300;
        }
        return total;
    }

    public String getStatus() {
        LocalDateTime now = LocalDateTime.now();

        if (actualCheckOut != null) {
            return "CHECKED OUT";
        } else if (now.isBefore(getCheckInDateTime())) {
            return "UPCOMING";
        } else if (now.isBefore(getCheckOutDateTime())) {
            return "STAYING NOW";
        } else {
            return "COMPLETED";
        }
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Pet getPet() {
        return pet;
    }

    public Room getRoom() {
        return roomAllocations.values().iterator().next();
    }

    public Map<LocalDate, Room> getRoomAllocations() {
        return roomAllocations;
    }

    public LocalDateTime getCheckInDateTime() {
        return LocalDateTime.of(checkInDate, checkInTime);
    }

    public LocalDateTime getCheckOutDateTime() {
        return LocalDateTime.of(checkOutDate, checkOutTime);
    }

    public LocalDateTime getEndDateTime() {
        if (actualCheckOut != null) {
            return actualCheckOut;
        }
        return getCheckOutDateTime();
    }

    public void checkOut() {
        actualCheckOut = LocalDateTime.now();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void printInfo() {
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Phone: " + customer.getPhoneNumber());
        System.out.println("Pet: " + pet.getName()
                + " / Breed: " + pet.getBreed());
        System.out.println("Room: " + getRoom().getRoomId());
        System.out.println("Check-in: " + getCheckInDateTime());
        System.out.println("Check-out: " + getCheckOutDateTime());
        System.out.println("Status: " + getStatus());
        System.out.println("Total: " + totalPrice);
    }
}
