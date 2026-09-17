public class Main {
    public static void main(String[] args) {

        // ---------- STEP 1: CLASS & OBJECT ----------
        // "Citizen" is a CLASS (a blueprint). Using "new" creates an
        // OBJECT (an actual instance) from that blueprint.
        Citizen citizen = new Citizen("C101", "Rahul Sharma", "9876543210");
        System.out.println("Step 1 - Object created: " + citizen.getName());

        // ---------- STEP 2: ATTRIBUTES & ENCAPSULATION ----------
        // "name" is a private ATTRIBUTE inside Citizen (via Person).
        // We are NOT allowed to touch it directly (citizen.name = ...).
        // We can only read it through a public getter method.
        // This hiding of data is called ENCAPSULATION.
        System.out.println("Step 2 - Reading private data safely: " + citizen.getName());

        // ---------- STEP 3: CONSTRUCTOR & this ----------
        // The Citizen(...) call above is the CONSTRUCTOR. Inside
        // Person's constructor, "this.name = name;" uses the "this"
        // keyword to tell Java: "this.name" is the object's own field,
        // and "name" is the value passed in.
        System.out.println("Step 3 - Built using constructor: " + citizen.getContactCard());

        // ---------- STEP 4: INHERITANCE & super ----------
        // RescueTeamMember EXTENDS Person, so it inherits name,
        // contactNumber, and getContactCard(). Inside its constructor
        // it calls super(...) to let Person set up those inherited
        // fields first.
        RescueTeamMember member = new RescueTeamMember("RTM01", "Anita Verma", "9998887771", "Paramedic");
        System.out.println("Step 4 - Inherited class object: " + member.getName());

        // ---------- STEP 5: ABSTRACTION & ABSTRACT CLASS ----------
        // Person is an ABSTRACT CLASS. We can never write
        // "new Person(...)" directly. It only defines WHAT every
        // person must do (describeRole()), not HOW. Each subclass
        // fills in the "how". This hiding of internal detail is
        // ABSTRACTION.
        System.out.println("Step 5 - Person is abstract, so only Citizen/RescueTeamMember can be created.");

        // ---------- STEP 6: METHOD OVERRIDING & POLYMORPHISM ----------
        // Both Citizen and RescueTeamMember give their OWN version of
        // describeRole() -> that is OVERRIDING.
        // Calling the SAME method name on different objects and
        // getting different results is POLYMORPHISM.
        System.out.println("Step 6 - Polymorphism:");
        System.out.println("   " + citizen.describeRole());
        System.out.println("   " + member.describeRole());

        // ---------- STEP 7: METHOD OVERLOADING ----------
        // reportEmergency() has TWO versions with different
        // parameters. Java decides which one to run based on what
        // arguments we pass. Same name, different behaviour = OVERLOADING.
        Emergency e1 = citizen.reportEmergency("Flood", "Vadodara");
        Emergency e2 = citizen.reportEmergency("Building Collapse", "Ahmedabad", "High", 8);
        System.out.println("Step 7 - Overloaded methods used:");
        System.out.println("   " + e1.getSummary());
        System.out.println("   " + e2.getSummary());

        // ---------- STEP 8: COMPOSITION ----------
        // Every Emergency object internally creates its own Location
        // object. Emergency "has-a" Location. Similarly, a RescueTeam
        // "has-a" list of RescueTeamMembers. These parts only exist
        // because their owner object created them.
        RescueTeam team = new RescueTeam("RT07", "Alpha Rescue Team");
        team.addMember(member);
        System.out.println("Step 8 - Composition:");
        System.out.println("   Emergency's own Location object -> " + e2.getLocation());
        System.out.println("   Team's own list of members -> " + team.getSummary());

        // ---------- STEP 9: PUTTING IT ALL TOGETHER ----------
        DisasterManagementAuthority authority = new DisasterManagementAuthority("DMA01", "District Authority");
        authority.registerTeam(team);
        authority.verifyEmergency(e2);
        authority.assignRescueTeam(e2);

        System.out.println("Step 9 - Final status after assignment:");
        System.out.println("   " + e2.getSummary());
        System.out.println("   " + team.getSummary());

        // ---------- STEP 10: STATIC VARIABLE & STATIC FUNCTION ----------
        // totalEmergenciesReported is ONE counter shared by every
        // Emergency object (e1, e2 both added to it). We call the
        // static function on the CLASS name, not on an object.
        System.out.println("\nStep 10 - Static variable/function:");
        System.out.println("   Total emergencies reported so far: " + Emergency.getTotalEmergenciesReported());

        // ---------- STEP 11: FINAL (variable & method) ----------
        // SYSTEM_NAME is a "public static final" constant -> fixed
        // value, cannot be changed once set.
        // getPersonId() in Person is a "final" method -> no subclass
        // can override it.
        System.out.println("\nStep 11 - Final keyword:");
        System.out.println("   System name (final constant): " + DisasterManagementAuthority.SYSTEM_NAME);
        System.out.println("   Citizen ID via final method: " + citizen.getPersonId());

        // ---------- STEP 12: NESTED CLASS ----------
        // TeamReport is a class defined INSIDE RescueTeam. We access
        // it as RescueTeam.TeamReport, and build one through the
        // outer class's own method.
        System.out.println("\nStep 12 - Nested class:");
        RescueTeam.TeamReport report = team.generateReport();
        System.out.println("   " + report.print());
    }
}