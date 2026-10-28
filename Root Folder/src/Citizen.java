import java.util.ArrayList;
import java.util.List;

/**
 * INHERITANCE
 * -----------
 * Citizen "is-a" Person, so it extends Person and automatically gets
 * name, contactNumber, personId, and getContactCard() for free.
 */
public class Citizen extends Person {

    // COMPOSITION: a Citizen "has-a" list of emergencies they reported.
    // These Emergency objects are still independently meaningful
    // (an Emergency can outlive being "owned" conceptually by one
    // citizen reference), so this list is a simple has-a relationship.
    private List<Emergency> reportedEmergencies;

    public Citizen(String personId, String name, String contactNumber) {
        // super(...) -> calls the Person constructor to initialise the
        // inherited fields. Must be the first statement in the
        // subclass constructor.
        super(personId, name, contactNumber);
        this.reportedEmergencies = new ArrayList<>();
    }

    // METHOD OVERRIDING
    // Citizen provides its own version of the abstract method defined
    // in Person. This is compile-time contract + run-time behaviour.
    @Override
    public String describeRole() {
        return getName() + " is a Citizen who can report emergencies.";
    }

    // METHOD OVERLOADING (version 1)
    // Same method name "reportEmergency", different parameter list.
    // "throws" -> warns callers that this method may throw this exception.
    public Emergency reportEmergency(String type, String location) throws InvalidEmergencyException {
        return reportEmergency(type, location, "Unknown", 1);
    }

    // METHOD OVERLOADING (version 2)
    // More detailed version of the same operation. Java picks the
    // right one based on the arguments you pass at the call site.
    public Emergency reportEmergency(String type, String location, String severity, int peopleAffected)
            throws InvalidEmergencyException {

        // EXCEPTION HANDLING: validate BEFORE creating the object.
        // "throw" actually raises the exception and stops this method.
        if (location == null || location.trim().isEmpty()) {
            throw new InvalidEmergencyException("Location is missing. Please provide the emergency location.");
        }
        if (peopleAffected <= 0) {
            throw new InvalidEmergencyException("People affected must be at least 1, but got " + peopleAffected + ".");
        }

        Emergency emergency = new Emergency(
                "E" + (reportedEmergencies.size() + 1) + "-" + getPersonId(),
                type, location, severity, peopleAffected, this);
        reportedEmergencies.add(emergency);
        return emergency;
    }

    public List<Emergency> getReportedEmergencies() {
        return reportedEmergencies;
    }
}