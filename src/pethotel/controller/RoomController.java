package pethotel.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import pethotel.model.Booking;
import pethotel.model.Room;
import pethotel.repository.DataManager;

public class RoomController {

    private DataManager dataManager;

    public RoomController(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void addRoom(Room room) {
        if (room == null) {
            return;
        }

        if (findRoom(room.getRoomId()) == null) {
            dataManager.getRooms().add(room);
            dataManager.saveRooms();
        }
    }

    public Room findRoom(String roomId) {
        if (roomId == null) {
            return null;
        }

        for (Room room : dataManager.getRooms()) {
            if (room.getRoomId().equalsIgnoreCase(roomId)) {
                return room;
            }
        }

        return null;
    }

    public ArrayList<Room> getRooms() {
        return dataManager.getRooms();
    }

    public boolean isRoomAvailable(Room room, LocalDate date) {
        if (room == null || date == null) {
            return false;
        }

        for (Booking booking : dataManager.getBookings()) {
            Room bookedRoom = booking.getRoomAllocations().get(date);

            if (bookedRoom != null
                    && bookedRoom.getRoomId().equalsIgnoreCase(room.getRoomId())) {
                return false;
            }
        }

        return true;
    }

    public String getRoomStatus(Room room, LocalDate date) {
        if (isRoomAvailable(room, date)) {
            return "AVAILABLE";
        }

        return "BOOKED";
    }

    public Map<LocalDate, Boolean> getRoomAvailability(Room room,
            LocalDate startDate, int days) {

        Map<LocalDate, Boolean> availability
                = new LinkedHashMap<LocalDate, Boolean>();

        if (room == null || startDate == null || days <= 0) {
            return availability;
        }

        LocalDate date = startDate;

        for (int i = 0; i < days; i++) {
            availability.put(date, isRoomAvailable(room, date));
            date = date.plusDays(1);
        }

        return availability;
    }

    public String getRoomSummary(LocalDate date) {
        StringBuilder text = new StringBuilder();

        for (Room room : getRooms()) {
            text.append(room.getRoomId())
                    .append(" - ")
                    .append(room.getRoomName())
                    .append(" | ")
                    .append(getRoomStatus(room, date))
                    .append("\n");
        }

        return text.toString();
    }

    public String getRoomSummary() {
        return getRoomSummary(LocalDate.now());
    }

    public void showRooms() {
        System.out.println(getRoomSummary());
    }
}