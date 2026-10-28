/**
 * BASE-CLASS EXCEPTION
 * --------------------
 * Parent of all our own exceptions. Because InvalidEmergencyException
 * and NoTeamAvailableException both extend this class, one
 * "catch (RescueSystemException e)" can catch either of them
 * (a base-class catch also catches every derived-class exception).
 */
public class RescueSystemException extends Exception {

    public RescueSystemException(String message) {
        super(message);
    }
}