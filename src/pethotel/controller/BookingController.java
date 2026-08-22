package pethotel.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import pethotel.model.Booking;
import pethotel.model.Customer;
import pethotel.model.Pet;
import pethotel.model.Room;
import pethotel.repository.DataManager;

public class BookingController {

    private DataManager dataManager;
    private RoomController roomController;
    private String lastMessage = "";

    public BookingController(DataManager dataManager,
            CustomerController customerController,
            RoomController roomController) {
        this.dataManager = dataManager;
        this.roomController = roomController;
    }

    public Booking createBooking(Customer customer,
            Pet pet,
            Map<LocalDate, Room> roomAllocations,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            boolean extraWalking,
            boolean extraGrooming) {

        if (customer == null || pet == null) {
            lastMessage = "Customer and pet are required.";
            return null;
        }

        if (checkInDate == null || checkOutDate == null) {
            lastMessage = "Check-in and check-out dates are required.";
            return null;
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            lastMessage = "Check-out date must be after check-in date.";
            return null;
        }

        if (roomAllocations == null || roomAllocations.isEmpty()) {
            lastMessage = "Room is required.";
            return null;
        }

        for (Map.Entry<LocalDate, Room> entry : roomAllocations.entrySet()) {
            Room room = entry.getValue();
            LocalDate date = entry.getKey();

            if (room == null || !room.canAccommodate(pet)) {
                lastMessage = "Room is not suitable for this pet.";
                return null;
            }

            if (!roomController.isRoomAvailable(room, date)) {
                lastMessage = "Room is already booked on " + date + ".";
                return null;
            }
        }

        String bookingId = "BK" + (dataManager.getBookings().size() + 1);

        try {
            Booking booking = new Booking(
                    bookingId,
                    customer,
                    pet,
                    roomAllocations,
                    checkInDate,
                    checkOutDate,
                    extraWalking,
                    extraGrooming
            );

            if (dataManager.saveBooking(booking)) {
                lastMessage = "Booking successful.";
                return booking;
            }

            lastMessage = "Cannot save booking.";
            return null;

        } catch (IllegalArgumentException e) {
            lastMessage = e.getMessage();
            return null;
        }
    }

    public Booking findBooking(String bookingId) {
        if (bookingId == null) {
            return null;
        }

        for (Booking booking : dataManager.getBookings()) {
            if (booking.getBookingId().equalsIgnoreCase(bookingId)) {
                return booking;
            }
        }

        return null;
    }

    public String getStatus(Booking booking) {
        LocalDate today = LocalDate.now();

        if (today.isBefore(booking.getCheckInDate())) {
            return "UPCOMING";
        }

        if (today.isBefore(booking.getCheckOutDate())) {
            return "STAYING NOW";
        }

        return "COMPLETED";
    }

    public ArrayList<Booking> getBookings() {
        return dataManager.getBookings();
    }

    public String getLastMessage() {
        return lastMessage;
    }
}