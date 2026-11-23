
import java.math.BigDecimal;

public class Booking {

    private int bookingId;
    private String bookingReference;
    private int customerId;
    private int eventId;
    private BigDecimal totalAmount;
    private String bookingStatus;

    public Booking(int bookingId, String bookingReference,
                   int customerId, int eventId,
                   BigDecimal totalAmount, String bookingStatus) {

        this.bookingId = bookingId;
        this.bookingReference = bookingReference;
        this.customerId = customerId;
        this.eventId = eventId;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getEventId() {
        return eventId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }
}
