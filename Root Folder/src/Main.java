public class Main {
    public static void main(String[] args) {

        System.out.println("=== 1-4: CLASS, OBJECT, ATTRIBUTES, METHODS ===");
        // Each `new` call below creates a separate OBJECT from a CLASS
        // blueprint, with its own copy of the ATTRIBUTES.
        Citizen citizen = new Citizen("C101", "Rahul Sharma", "9876543210");
        RescueTeamMember member1 = new RescueTeamMember("RTM01", "Anita Verma", "9998887771", "Paramedic");
        RescueTeamMember member2 = new RescueTeamMember("RTM02", "Vikram Rao", "9998887772", "Diver");

        System.out.println("=== 5-6: ENCAPSULATION + ACCESS MODIFIERS ===");
        // We never touched citizen's private fields directly — only
        // through public getters like getName().
        System.out.println("Citizen name via getter: " + citizen.getName());

        System.out.println("\n=== 7-8: CONSTRUCTOR + this ===");
        System.out.println(citizen.getContactCard()); // built using "this" inside Person's constructor

        System.out.println("\n=== 9, 14: INHERITANCE + super ===");
        System.out.println(member1.getContactCard()); // overridden version calls super.getContactCard()

        System.out.println("\n=== 10-11: ABSTRACTION + ABSTRACT CLASS ===");
        // Person p = new Person(...); // <-- would NOT compile: Person is abstract
        System.out.println("Person is abstract, so it can only be used through subclasses like Citizen/RescueTeamMember.");

        System.out.println("\n=== 12-13: POLYMORPHISM + METHOD OVERRIDING ===");
        Person[] people = { citizen, member1, member2 };
        for (Person p : people) {
            // Same reference type (Person), same method call
            // (describeRole()), different actual behaviour depending
            // on the real object type. That is POLYMORPHISM, made
            // possible by each subclass OVERRIDING describeRole().
            System.out.println(" - " + p.describeRole());
        }

        System.out.println("\n=== 16: METHOD OVERLOADING (Citizen.reportEmergency) ===");
        Emergency e1 = citizen.reportEmergency("Flood", "Vadodara"); // simple version
        Emergency e2 = citizen.reportEmergency("Building Collapse", "Ahmedabad", "High", 8); // detailed version
        System.out.println(e1.getSummary());
        System.out.println(e2.getSummary());

        System.out.println("\n=== 15: COMPOSITION (Emergency has-a Location, RescueTeam has-a Members) ===");
        RescueTeam team07 = new RescueTeam("RT07", "Alpha Rescue Team");
        team07.addMember(member1);
        team07.addMember(member2);
        System.out.println(team07.getSummary());
        System.out.println("Emergency location object: " + e2.getLocation());

        System.out.println("\n=== Full workflow using everything above ===");
        DisasterManagementAuthority authority = new DisasterManagementAuthority("DMA01", "District Disaster Management Authority");
        authority.registerTeam(team07);
        authority.verifyEmergency(e2);
        authority.assignRescueTeam(e2);

        // METHOD OVERLOADING again, this time on RescueTeam.updateStatus
        team07.updateStatus("In Action");
        team07.updateStatus("In Action", "Ahmedabad");

        System.out.println(e2.getSummary());
        System.out.println(team07.getSummary() + ", location=" + team07.currentLocation);
    }
}