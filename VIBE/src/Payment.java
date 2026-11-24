
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private int paymentId;
    private int bookingId;
    private String paymentMethod;
    private BigDecimal amount;
    private String paymentStatus;
    private String transactionReference;
    private LocalDateTime paymentDateTime;

    public Payment(int paymentId, int bookingId,
                   String paymentMethod, BigDecimal amount,
                   String paymentStatus, String transactionReference,
                   LocalDateTime paymentDateTime) {

        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.transactionReference = transactionReference;
        this.paymentDateTime = paymentDateTime;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }
}
