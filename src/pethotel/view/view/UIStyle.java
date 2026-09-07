package pethotel.view;

import java.awt.Color;
import java.awt.Font;

/**
 * Shared style constants so every screen in the app looks consistent
 * (same palette / fonts used in ServicePanel).
 */
public final class UIStyle {

    private UIStyle() {
    }

    public static final Color COLOR_PRIMARY = new Color(79, 70, 229);
    public static final Color COLOR_BACKGROUND = new Color(245, 246, 250);
    public static final Color COLOR_CARD = Color.WHITE;
    public static final Color COLOR_TEXT_DARK = new Color(31, 41, 55);
    public static final Color COLOR_TEXT_LIGHT = new Color(107, 114, 128);
    public static final Color COLOR_BORDER = new Color(224, 226, 232);

    // Colors requested for the two header actions (red = pick a room, yellow = login)
    public static final Color COLOR_RED = new Color(220, 38, 38);
    public static final Color COLOR_RED_HOVER = new Color(185, 28, 28);
    public static final Color COLOR_YELLOW = new Color(250, 204, 21);
    public static final Color COLOR_YELLOW_TEXT = new Color(92, 63, 0);

    public static final Color COLOR_FREE = new Color(220, 252, 231);
    public static final Color COLOR_FREE_TEXT = new Color(22, 101, 52);
    public static final Color COLOR_BOOKED = new Color(254, 226, 226);
    public static final Color COLOR_BOOKED_TEXT = new Color(153, 27, 27);

    // Colors used by the booking-date calendar (available / booked / selected)
    public static final Color COLOR_CAL_AVAILABLE = new Color(52, 199, 89);
    public static final Color COLOR_CAL_BOOKED = new Color(239, 68, 68);
    public static final Color COLOR_CAL_SELECTED = new Color(250, 204, 21);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
}
