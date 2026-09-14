import java.util.ArrayList;
import java.util.List;

/**
 * INHERITANCE
 * Citizen "is-a" Person, so it extends Person.
 */
public class Citizen extends Person {

    // COMPOSITION: a Citizen "has-a" list of emergencies they reported.
    private List<Emergency> reportedEmergencies;

    public Citizen(String personId, String name, String contactNumber) { // CONSTRUCTOR
        // super(...) -> calss Person's constructor to initialize inherited fields.
        super(personId, name, contactNumber);
        this.reportedEmergencies = new ArrayList<>();
    }

    // METHOD OVERRIDING
    // Citizen provides its own implementation of the abstract method describeRole() from Person.
    @Override
    public String describeRole() {
        return getName() + " is a Citizen who can report emergencies.";
    }

    // METHOD OVERLOADING (version 1)
    // Same method name "reportEmergency", different parameter list.
    public Emergency reportEmergency(String type, String location) {
        return reportEmergency(type, location, "Unknown", 1);
    }

    // METHOD OVERLOADING (version 2)
    // More detailed version of the same operation. Java picks the
    // right one based on the arguments you pass at the call site.
    public Emergency reportEmergency(String type, String location, String severity, int peopleAffected) {
        Emergency emergency = new Emergency( "E" + (reportedEmergencies.size() + 1) + "-" + getPersonId(), type, location, severity, peopleAffected, this);
        reportedEmergencies.add(emergency);
        return emergency;
    }

    public List<Emergency> getReportedEmergencies() {
        return reportedEmergencies;
    }
}