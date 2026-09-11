package pethotel.view;

import java.time.LocalDate;
import java.util.Map;

import pethotel.model.Customer;
import pethotel.model.Pet;
import pethotel.model.Room;

/** Plain holder passed from {@link RoomServicePanel} to {@link ReviewBookingPanel}. */
public class BookingDraft {
    public Customer customer;
    public Pet pet;
    public Map<LocalDate, Room> roomPerNight;
    public LocalDate checkIn;
    public LocalDate checkOut;
    public boolean walking;
    public boolean grooming;
    public double estimatedTotal;
}
