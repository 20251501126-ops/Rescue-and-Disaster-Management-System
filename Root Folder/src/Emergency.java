/**
 * CLASS + ATTRIBUTES + METHODS (the basics)
 * ------------------------------------------
 * A class is a blueprint. Emergency defines the ATTRIBUTES (fields)
 * every emergency has, and the METHODS (behaviour) it can perform.
 * Each `new Emergency(...)` call in Main.java creates a separate
 * OBJECT — its own independent set of these attribute values in
 * memory.
 */
public class Emergency {

    // STATIC VARIABLE
    // Belongs to the CLASS, not to any one object. There is only ONE
    // copy of this in memory, shared by every Emergency object. Every
    // time a new Emergency is created, this counter goes up for
    // everybody at once.
    private static int totalEmergenciesReported = 0;

    // ATTRIBUTES + ENCAPSULATION (all private)
    private String emergencyId;
    private String type;
    private Location location;      // COMPOSITION: Emergency "has-a" Location
    private String severity;
    private int numberOfPeopleAffected;
    private String status;
    private Citizen reportedBy;
    private RescueTeam assignedTeam; // starts null, filled in later

    // CONSTRUCTOR OVERLOADING (version 1) — minimal info
    public Emergency(String emergencyId, String type, String locationArea) {
        // this(...) -> calls another constructor in THIS class instead
        // of repeating the same setup code. Must be the first line.
        this(emergencyId, type, locationArea, "Unknown", 1, null);
    }

    // CONSTRUCTOR OVERLOADING (version 2) — full info
    public Emergency(String emergencyId, String type, String locationArea,
                     String severity, int numberOfPeopleAffected, Citizen reportedBy) {
        this.emergencyId = emergencyId;
        this.type = type;
        // COMPOSITION in action: Emergency creates its own Location
        // internally. Nobody hands Emergency a pre-built Location.
        this.location = new Location(0.0, 0.0, locationArea);
        this.severity = severity;
        this.numberOfPeopleAffected = numberOfPeopleAffected;
        this.reportedBy = reportedBy;
        this.status = "Reported";

        // Every time ANY Emergency object is created, the shared
        // static counter increases.
        totalEmergenciesReported++;
    }

    // STATIC FUNCTION
    // Called on the CLASS itself (Emergency.getTotalEmergenciesReported()),
    // not on one object, because it works with the static variable,
    // not with any single object's data.
    public static int getTotalEmergenciesReported() {
        return totalEmergenciesReported;
    }

    // METHODS (behaviour)
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public void assignTeam(RescueTeam team) {
        this.assignedTeam = team;
        updateStatus("Assigned");
    }

    // GETTERS -> the encapsulated, controlled way to read private data
    public String getEmergencyId() { return emergencyId; }
    public String getType() { return type; }
    public Location getLocation() { return location; }
    public String getSeverity() { return severity; }
    public int getNumberOfPeopleAffected() { return numberOfPeopleAffected; }
    public String getStatus() { return status; }
    public RescueTeam getAssignedTeam() { return assignedTeam; }

    public String getSummary() {
        return "[" + emergencyId + "] " + type + " at " + location
                + " | severity=" + severity
                + " | affected=" + numberOfPeopleAffected
                + " | status=" + status;
    }
}