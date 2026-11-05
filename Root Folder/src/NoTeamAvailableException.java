/**
 * CUSTOM EXCEPTION (checked)
 * --------------------------
 * Thrown by the Disaster Management Authority when an emergency needs
 * a rescue team but every team is busy. This is the "no rescue team
 * available" alternate flow from our use case.
 */
public class NoTeamAvailableException extends RescueSystemException {

    public NoTeamAvailableException(String message) {
        super(message);
    }
}