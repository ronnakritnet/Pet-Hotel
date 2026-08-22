package pethotel.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import pethotel.model.Booking;
import pethotel.model.Customer;
import pethotel.model.Pet;
import pethotel.model.Room;
import pethotel.repository.DataManager;

public class BookingController {

    private DataManager dataManager;
    private RoomController roomController;

    public BookingController(DataManager dataManager,
            RoomController roomController) {
        this.dataManager = dataManager;
        this.roomController = roomController;
    }

    public Booking createBooking(Customer customer, Pet pet, Room room,
            LocalDate checkInDate, LocalTime checkInTime,
            LocalDate checkOutDate, LocalTime checkOutTime,
            boolean walking, boolean grooming) {

        LocalDateTime checkIn = LocalDateTime.of(checkInDate, checkInTime);
        LocalDateTime checkOut = LocalDateTime.of(checkOutDate, checkOutTime);

        if (!checkIn.isBefore(checkOut)) {
            System.out.println("Check-out must be after check-in.");
            return null;
        }

        if (!room.canAccommodate(pet)) {
            System.out.println("This room is not suitable for this pet.");
            return null;
        }

        if (isPetBooked(pet)) {
            System.out.println("This pet already has an active booking.");
            return null;
        }

        if (roomController.getAvailable(room, checkIn, checkOut) <= 0) {
            System.out.println("This room is full.");
            return null;
        }

        Map<LocalDate, Room> roomTable
                = new LinkedHashMap<LocalDate, Room>();
        LocalDate date = checkInDate;

        while (date.isBefore(checkOutDate)) {
            roomTable.put(date, room);
            date = date.plusDays(1);
        }

        if (roomTable.isEmpty()) {
            roomTable.put(checkInDate, room);
        }

        String bookingId = "BK" + (dataManager.getBookings().size() + 1);
        Booking booking = new Booking(bookingId, customer, pet,
                roomTable, checkInDate, checkInTime,
                checkOutDate, checkOutTime, walking, grooming);

        dataManager.getBookings().add(booking);
        return booking;
    }

    public boolean isPetBooked(Pet pet) {
        for (Booking booking : dataManager.getBookings()) {
            boolean samePet = booking.getPet().getPetId()
                    .equals(pet.getPetId());
            boolean active = booking.getStatus().equals("UPCOMING")
                    || booking.getStatus().equals("STAYING NOW");

            if (samePet && active) {
                return true;
            }
        }
        return false;
    }

    public void showAllBookings() {
        System.out.println("=== ALL BOOKINGS ===");

        if (dataManager.getBookings().isEmpty()) {
            System.out.println("No booking.");
        }

        for (Booking booking : dataManager.getBookings()) {
            booking.printInfo();
            System.out.println("--------------------");
        }
    }

    public void showPetBooking(Pet pet) {
        boolean found = false;

        for (Booking booking : dataManager.getBookings()) {
            if (booking.getPet().getPetId().equals(pet.getPetId())) {
                System.out.println("  Booking: " + booking.getBookingId()
                        + " / Room " + booking.getRoom().getRoomId()
                        + " / " + booking.getCheckInDateTime()
                        + " to " + booking.getCheckOutDateTime()
                        + " / " + booking.getStatus());
                found = true;
            }
        }

        if (!found) {
            System.out.println("  Booking: No booking");
        }
    }

    public void showPetsInRooms() {
        System.out.println("=== PETS IN ROOMS ===");

        for (Room room : roomController.getRooms()) {
            System.out.println("Room " + room.getRoomId());
            boolean found = false;

            for (Booking booking : dataManager.getBookings()) {
                boolean sameRoom = booking.getRoom().getRoomId()
                        .equals(room.getRoomId());
                boolean active = booking.getStatus().equals("UPCOMING")
                        || booking.getStatus().equals("STAYING NOW");

                if (sameRoom && active) {
                    System.out.println("- " + booking.getPet().getName()
                            + " / " + booking.getCheckInDateTime()
                            + " to " + booking.getCheckOutDateTime()
                            + " / " + booking.getStatus());
                    found = true;
                }
            }

            if (!found) {
                System.out.println("- No pets");
            }
        }
    }

    public void checkOutEarly(String bookingId) {
        for (Booking booking : dataManager.getBookings()) {
            if (booking.getBookingId().equalsIgnoreCase(bookingId)) {
                if (booking.getStatus().equals("STAYING NOW")) {
                    booking.checkOut();
                    System.out.println("Check-out success: "
                            + booking.getPet().getName());
                } else {
                    System.out.println("This pet is not staying now.");
                }
                return;
            }
        }
        System.out.println("Booking not found.");
    }

    public ArrayList<Booking> getBookings() {
        return dataManager.getBookings();
    }
}
