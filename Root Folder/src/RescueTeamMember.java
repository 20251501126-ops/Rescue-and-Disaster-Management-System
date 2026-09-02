/**
 * INHERITANCE
 * -----------
 * RescueTeamMember is also a Person (a human), so it shares the same
 * base fields/behaviour as Citizen, but plays a different role.
 */
public class RescueTeamMember extends Person {

    private String specialization; // e.g. "Paramedic", "Diver", "Firefighter"

    public RescueTeamMember(String personId, String name, String contactNumber, String specialization) {
        super(personId, name, contactNumber); // super -> reuse Person's constructor logic
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    // METHOD OVERRIDING
    @Override
    public String describeRole() {
        return getName() + " is a Rescue Team Member (" + specialization + ").";
    }

    // OVERRIDING + super. keyword together:
    // super.getContactCard() calls Person's version first, then this
    // method adds extra info on top. This shows "extend", not just
    // "replace", inherited behaviour.
    @Override
    public String getContactCard() {
        return super.getContactCard() + " [Specialization: " + specialization + "]";
    }
}