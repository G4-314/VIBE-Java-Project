
public class Seat {
    private int seatId;
    private int eventId;
    private String rowLabel;
    private int seatNumber;
    private String seatStatus;

    public Seat(int seatId, int eventId, String rowLabel,
                int seatNumber, String seatStatus) {
        this.seatId = seatId;
        this.eventId = eventId;
        this.rowLabel = rowLabel;
        this.seatNumber = seatNumber;
        this.seatStatus = seatStatus;
    }

    public int getSeatId() {
        return seatId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getRowLabel() {
        return rowLabel;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public String getSeatStatus() {
        return seatStatus;
    }
}

