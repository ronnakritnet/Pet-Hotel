package pethotel.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import pethotel.model.Booking;
import pethotel.model.Room;
import pethotel.repository.DataManager;

public class RoomController {

    private DataManager dataManager;

    public RoomController(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public ArrayList<Room> getRooms() {
        return dataManager.getRooms();
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
}