/**
 * ABSTRACTION + ABSTRACT CLASS
 * ----------------------------
 * Person hides the common details shared by every human actor in the
 * system (Citizen, RescueTeamMember) and forces subclasses to define
 * their own role-specific behaviour via the abstract method
 * describeRole(). You can never do `new Person(...)` — only concrete
 * subclasses can be created. This is "abstraction": we expose only
 * what matters (name, contact, role) and hide how each role actual
 */
public abstract class Person {

    // ENCAPSULATION + ACCESS MODIFIERS
    // private -> only visible inside this class. Outside code cannot
    // touch these fields directly; it must go through getters/setters.
    private String name;
    private String contactNumber;

    // protected -> visible to this class, subclasses (Citizen,
    // RescueTeamMember), and other classes in the same package.
    protected String personId;

    // CONSTRUCTOR + this
    // "this.name" refers to the field, "name" refers to the parameter.
    // "this" resolves the naming clash.
    public Person(String personId, String name, String contactNumber) {
        this.personId = personId;
        this.name = name;
        this.contactNumber = contactNumber;
    }

    // ENCAPSULATION: controlled access to private fields
    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    // FINAL METHOD
    // "final" here means NO subclass (Citizen, RescueTeamMember) is
    // allowed to override getPersonId(). An ID's lookup logic should
    // never change, so we lock it here on purpose. Trying to
    // override this in a subclass would cause a compile error.
    public final String getPersonId() {
        return personId;
    }

    // ABSTRACT METHOD -> no body here. Every subclass MUST implement
    // this, which is exactly how ABSTRACTION forces a contract while
    // hiding the "how".
    public abstract String describeRole();

    // A normal (non-abstract) method that subclasses inherit as-is,
    // or can override.
    public String getContactCard() {
        return name + " (ID: " + personId + ", Contact: " + contactNumber + ")";
    }
}