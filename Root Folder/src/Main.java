public class Main {

    // THROWS with a BASE-CLASS type: this method calls code that throws
    // InvalidEmergencyException (derived), but it only has to declare
    // the parent type RescueSystemException.
    static void submitReport(Citizen citizen, String location) throws RescueSystemException {
        citizen.reportEmergency("Fire", location);
    }

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
        Emergency e1 = null;
        Emergency e2 = null;
        try {
            e1 = citizen.reportEmergency("Flood", "Vadodara");
            e2 = citizen.reportEmergency("Building Collapse", "Ahmedabad", "High", 8);
        } catch (InvalidEmergencyException ex) {
            System.out.println("Could not report emergency: " + ex.getMessage());
        }
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
        try {
            authority.assignRescueTeam(e2);
        } catch (NoTeamAvailableException ex) {
            System.out.println("Assignment failed: " + ex.getMessage());
        }

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

        // ---------- STEP 13: EXCEPTION HANDLING ----------
        System.out.println("\nStep 13 - Exception handling:");

        // (a) CUSTOM EXCEPTION: missing location. try-catch stops the
        // program from crashing and shows a friendly message instead.
        try {
            citizen.reportEmergency("Fire", "");
        } catch (InvalidEmergencyException ex) {
            System.out.println("   (a) Caught custom exception: " + ex.getMessage());
        }

        // (b) CUSTOM EXCEPTION: invalid number of people.
        try {
            citizen.reportEmergency("Flood", "Surat", "High", -5);
        } catch (InvalidEmergencyException ex) {
            System.out.println("   (b) Caught custom exception: " + ex.getMessage());
        }

        // (c) CUSTOM EXCEPTION: team07 is already busy with e2, so
        // there is no free team for e1.
        try {
            authority.assignRescueTeam(e1);
        } catch (NoTeamAvailableException ex) {
            System.out.println("   (c) Caught custom exception: " + ex.getMessage());
        }

        // (d) BUILT-IN EXCEPTIONS with MULTIPLE catch blocks + finally.
        // Each catch handles one kind of error; finally ALWAYS runs.
        String[] inputs = { "12", "abc" };
        for (String input : inputs) {
            try {
                int people = Integer.parseInt(input);   // may throw NumberFormatException
                int perTeam = 24 / (people - 12);       // may throw ArithmeticException
                System.out.println("   (d) people per team = " + perTeam);
            } catch (NumberFormatException ex) {
                System.out.println("   (d) '" + input + "' is not a valid number.");
            } catch (ArithmeticException ex) {
                System.out.println("   (d) Cannot divide by zero (input " + input + ").");
            } finally {
                System.out.println("   (d) finally block runs for input '" + input + "'.");
            }
        }

        // ---------- STEP 14: BASE-CLASS AND DERIVED-CLASS EXCEPTIONS ----------
        System.out.println("\nStep 14 - Catching base-class and derived-class exceptions:");

        // (a) Derived catch placed FIRST, base catch AFTER it.
        // The exception is an InvalidEmergencyException, so the first
        // (most specific) matching catch runs and the base one is skipped.
        // Rule: always write derived catches before base catches, otherwise
        // the code will not compile (the base catch would hide the derived one).
        try {
            citizen.reportEmergency("Fire", "");   // throws InvalidEmergencyException here
            authority.assignRescueTeam(e1);        // could throw NoTeamAvailableException (never reached)
        } catch (InvalidEmergencyException ex) {
            System.out.println("   (a) Derived catch ran: " + ex.getMessage());
        } catch (RescueSystemException ex) {
            System.out.println("   (a) Base catch ran: " + ex.getMessage());
        }

        // (b) ONE base-class catch handles a DIFFERENT derived exception.
        // assignRescueTeam throws NoTeamAvailableException, but the
        // RescueSystemException catch still catches it.
        try {
            authority.assignRescueTeam(e1);
        } catch (RescueSystemException ex) {
            System.out.println("   (b) Base catch caught a " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }

        // (c) "throws" with the base type, handled by a base catch.
        try {
            submitReport(citizen, "  ");
        } catch (RescueSystemException ex) {
            System.out.println("   (c) Method declared throws RescueSystemException, caught: " + ex.getMessage());
        }

        // (d) Built-in hierarchy: ArrayIndexOutOfBoundsException (derived)
        // -> RuntimeException -> Exception (base of all).
        try {
            int[] teamSizes = new int[2];
            teamSizes[5] = 10;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("   (d) Derived built-in exception caught: index out of range");
        } catch (Exception ex) {
            System.out.println("   (d) Base Exception catch (not reached here)");
        } finally {
            System.out.println("   (d) finally block runs again.");
        }
    }
}