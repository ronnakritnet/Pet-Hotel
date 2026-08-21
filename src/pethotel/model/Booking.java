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

    private boolean extraWalking;
    private boolean extraGrooming;

    private double totalPrice;

    public Booking(String bookingId, Customer customer, Pet pet, Map<LocalDate, Room> roomAllocations,
            LocalDate checkInDate, LocalDate checkOutDate, boolean extraWalking, boolean extraGrooming) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID is required");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer is required");
        }
        if (pet == null) {
            throw new IllegalArgumentException("Pet is required");
        }
        this.bookingId = bookingId.trim();
        this.customer = customer;
        this.pet = pet;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomAllocations = roomAllocations;
        this.extraWalking = extraWalking;
        this.extraGrooming = extraGrooming;

        validateDateRange();
        validateRoomAllocations();

        updateTotalPrice();
    }

    private void validateDateRange() {
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("Both check-in and check-out dates are required");
        }
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be at least 1 day after check-in date");
        }
    }

    private void validateRoomAllocations() {
        if (roomAllocations == null || roomAllocations.isEmpty()) {
            throw new IllegalArgumentException("Room allocation must be for at least 1 day in the table");
        }

        for (Map.Entry<LocalDate, Room> entry : roomAllocations.entrySet()) {
            LocalDate date = entry.getKey();
            Room room = entry.getValue();

            if (date.isBefore(checkInDate) || !date.isBefore(checkOutDate)) {
                throw new IllegalArgumentException("Room allocation date (" + date + ") must be within the stay period.");
            }

            if (room == null) {
                throw new IllegalArgumentException("Room data for " + date + " is invalid (Null)");
            }

            if (pet != null && !room.canAccommodate(pet)) {
                throw new IllegalArgumentException("Room " + room.getRoomName() + " cannot accommodate " + pet.getName()
                        + " on " + date + " (Pet type mismatch or weight exceeds " + room.getMaxWeightLimit() + " kg limit)");
            }
        }
    }

    private void updateTotalPrice() {
        double sum = 0.0;
        if (roomAllocations != null) {
            for (Room room : roomAllocations.values()) {
                sum += room.getPricePerNight();
            }
        }
        if (extraWalking) {
            sum += 100.0;
        }
        if (extraGrooming) {
            sum += 300.0;
        }
        this.totalPrice = sum;
    }
    
    // GETTERS & SETTERS

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer is required");
        }
        this.customer = customer;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        if (pet == null) {
            throw new IllegalArgumentException("Pet is required");
        }
        this.pet = pet;

        if (roomAllocations != null) {
            validateRoomAllocations();
        }
    }

    public Map<LocalDate, Room> getRoomAllocations() {
        return roomAllocations;
    }

    public void setRoomAllocations(Map<LocalDate, Room> roomAllocations) {
        this.roomAllocations = roomAllocations;
        validateRoomAllocations();
        updateTotalPrice();
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        validateDateRange();
        if (roomAllocations != null) {
            validateRoomAllocations();
        }
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        validateDateRange();
        if (roomAllocations != null) {
            validateRoomAllocations();
        }
    }

    public boolean isExtraWalking() {
        return extraWalking;
    }

    public void setExtraWalking(boolean extraWalking) {
        this.extraWalking = extraWalking;
        updateTotalPrice();
    }

    public boolean isExtraGrooming() {
        return extraGrooming;
    }

    public void setExtraGrooming(boolean extraGrooming) {
        this.extraGrooming = extraGrooming;
        updateTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

}
