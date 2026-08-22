package pethotel.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import pethotel.model.Booking;
import pethotel.model.Room;
import pethotel.repository.DataManager;

public class RoomController {

    private DataManager dataManager;

    public RoomController(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void addRoom(Room room) {
        dataManager.getRooms().add(room);
    }

    public ArrayList<Room> getRooms() {
        return dataManager.getRooms();
    }

    public int countPets(Room room,
            LocalDateTime checkIn, LocalDateTime checkOut) {
        int count = 0;

        for (Booking booking : dataManager.getBookings()) {
            boolean sameRoom = booking.getRoom().getRoomId()
                    .equals(room.getRoomId());
            boolean sameTime = checkIn.isBefore(booking.getEndDateTime())
                    && checkOut.isAfter(booking.getCheckInDateTime());

            if (sameRoom && sameTime) {
                count++;
            }
        }
        return count;
    }

    public int getAvailable(Room room,
            LocalDateTime checkIn, LocalDateTime checkOut) {
        return room.getCapacity() - countPets(room, checkIn, checkOut);
    }

    public void showRooms() {
        LocalDateTime now = LocalDateTime.now();

        System.out.println("=== ROOM LIST ===");
        System.out.println("Current time: " + now.withSecond(0).withNano(0));

        for (Room room : dataManager.getRooms()) {
            int staying = 0;
            int upcoming = 0;

            for (Booking booking : dataManager.getBookings()) {
                if (booking.getRoom().getRoomId().equals(room.getRoomId())) {
                    if (booking.getStatus().equals("STAYING NOW")) {
                        staying++;
                    } else if (booking.getStatus().equals("UPCOMING")) {
                        upcoming++;
                    }
                }
            }

            int available = room.getCapacity() - staying;
            room.printInfo();
            System.out.println("  Staying now: " + staying);
            System.out.println("  Waiting to check in: " + upcoming);
            System.out.println("  Available now: " + available);

            if (available == 0) {
                System.out.println("  Status: FULL");
            } else {
                System.out.println("  Status: AVAILABLE");
            }
        }
    }
}
