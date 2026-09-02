public class Main {
    public static void main(String[] args) {

        //STEP 1: CLASS & OBJECT  
        // "Citizen" is a CLASS (a blueprint). Using "new" creates an
        Citizen citizen = new Citizen("C101", "Rahul Sharma", "9876543210");
        System.out.println("Step 1 - Object created: " + citizen.getName());

        //STEP 2: ATTRIBUTES & ENCAPSULATION 
        // "name" is a private ATTRIBUTE inside Citizen (via Person).
        // We are NOT allowed to touch it directly (citizen.name = ...).
        // We can only read it through a public getter method.
        System.out.println("Step 2 - Reading private data safely: " + citizen.getName());

        //STEP 3: CONSTRUCTOR & this
        System.out.println("Step 3 - Built using constructor: " + citizen.getContactCard());

        // STEP 4: INHERITANCE & super.
        RescueTeamMember member = new RescueTeamMember("RTM01", "Anita Verma", "9998887771", "Paramedic");
        System.out.println("Step 4 - Inherited class object: " + member.getName());

        //STEP 5: ABSTRACTION & ABSTRACT CLASS
        // Person is an ABSTRACT CLASS. We can never write
        // "new Person(...)" directly. It only defines WHAT every
        // person must do (describeRole()), not HOW. Each subclass
        // fills in the "how". This hiding of internal detail is
        // ABSTRACTION.
        System.out.println("Step 5 - Person is abstract, so only Citizen/RescueTeamMember can be created.");

        //STEP 6: METHOD OVERRIDING & POLYMORPHISM 
        System.out.println("Step 6 - Polymorphism:");
        System.out.println("   " + citizen.describeRole());
        System.out.println("   " + member.describeRole());

        //STEP 7: METHOD OVERLOADING
        // reportEmergency() has TWO versions with different parameters.
        Emergency e1 = citizen.reportEmergency("Flood", "Vadodara");
        Emergency e2 = citizen.reportEmergency("Building Collapse", "Ahmedabad", "High", 8);
        System.out.println("Step 7 - Overloaded methods used:");
        System.out.println("   " + e1.getSummary());
        System.out.println("   " + e2.getSummary());

        // STEP 8: COMPOSITIOn
        RescueTeam team = new RescueTeam("RT07", "Alpha Rescue Team");
        team.addMember(member);
        System.out.println("Step 8 - Composition:");
        System.out.println("   Emergency's own Location object -> " + e2.getLocation());
        System.out.println("   Team's own list of members -> " + team.getSummary());

        //STEP 9: PUTTING IT ALL TOGETHER
        DisasterManagementAuthority authority = new DisasterManagementAuthority("DMA01", "District Authority");
        authority.registerTeam(team);
        authority.verifyEmergency(e2);
        authority.assignRescueTeam(e2);

        System.out.println("Step 9 - Final status after assignment:");
        System.out.println("   " + e2.getSummary());
        System.out.println("   " + team.getSummary());
    }
}