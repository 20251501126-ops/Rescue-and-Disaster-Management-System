/**
 * ABSTRACTION + ABSTRACT CLASS
 * Person hides the common details shared by every human actor in the
 * system (Citizen, RescueTeamMember) and forces subclasses to define
 * their own role-specific behaviour via the abstract method
 * describeRole(). You can never do `new Person(...)`  
 * This is "abstraction": we expose only
 * what matters (name, contact, role) and hide how each role actually
 * behaves internally.
 */
public abstract class Person {

    // ENCAPSULATION + ACCESS MODIFIERS
    // private -> only visible inside this class. Outside code cannot
    // protected -> visible to this class, subclasses (Citizen, RescueTeamMember), and other classes in the same package.
    private String name;
    private String contactNumber;
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
    public String getName() {  // getter
        return name;
    }

    public String getContactNumber() {  // getter
        return contactNumber;
    }

    public String getPersonId() {  // getter
        return personId;
    }

    // ABSTRACT METHOD ->Every subclass MUST implement
    // this, which is exactly how ABSTRACTION forces a contract while
    // hiding the "how".
    public abstract String describeRole();

    public String getContactCard() {
        return name + " (ID: " + personId + ", Contact: " + contactNumber + ")";
    }
}