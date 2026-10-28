/**
 * CUSTOM EXCEPTION (checked)
 * --------------------------
 * Our own exception class for bad emergency data (empty location,
 * zero/negative people affected, etc.). It extends Exception, so Java
 * FORCES every caller to either handle it (try-catch) or pass it on
 * (throws). The message explains what went wrong.
 */
public class InvalidEmergencyException extends RescueSystemException {

    public InvalidEmergencyException(String message) {
        super(message);   // pass the message up to Exception
    }
}
