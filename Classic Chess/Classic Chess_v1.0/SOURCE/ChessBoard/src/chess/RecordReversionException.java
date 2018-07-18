package chess;

public class RecordReversionException extends Exception {

    private String reason = "";

    public RecordReversionException(String desc) {
        super("An \'RecordReversionException\' had occurred... " + desc);
        reason = desc;
    }

    public String getReason() {
        return reason;
    }
}
