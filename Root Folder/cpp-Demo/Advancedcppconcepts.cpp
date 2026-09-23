#include <iostream>
#include <string>
using namespace std;

/* =====================================================================
 * PART 1: "this" POINTER
 * =====================================================================
 * Inside any non-static member function, "this" is a POINTER to the
 * current object (its address in memory). We normally don't need to
 * write it explicitly (team.teamName = ... already means
 * this->teamName = ...), but it becomes useful when we want to
 * RETURN the current object itself, e.g. to chain method calls.
 * ===================================================================== */

/* =====================================================================
 * PART 2: VIRTUAL FUNCTION
 * =====================================================================
 * Normally in C++, if you call a function through a BASE class
 * pointer, the BASE class's version runs, even if the pointer
 * actually points to a DERIVED object. Marking a function "virtual"
 * fixes this: the CORRECT (derived) version runs instead, decided at
 * run time. This is how C++ achieves the same runtime polymorphism
 * Java gives you automatically.
 * ===================================================================== */

class RescueTeam {
protected:
    string teamName;

public:
    RescueTeam(string teamName) {
        this->teamName = teamName;   // "this" pointer used to avoid name clash
    }

    // "this" pointer used to return the CURRENT object, enabling
    // method chaining like: team.setName("Alpha").respond();
    RescueTeam& setName(string teamName) {
        this->teamName = teamName;
        return *this;   // *this = the object this pointer points to
    }

    // VIRTUAL FUNCTION: derived classes can override this, and the
    // correct version will run even when called through a base
    // class pointer.
    virtual void respond() {
        cout << teamName << " (general team) is responding to the emergency." << endl;
    }

    // Virtual destructor: good practice whenever a class has virtual
    // functions and might be deleted through a base class pointer
    // (as we do below with basicPtr and diveTeamPtr).
    virtual ~RescueTeam() {}
};

// DERIVED CLASS overriding the virtual function
class SpecializedRescueTeam : public RescueTeam {
public:
    SpecializedRescueTeam(string teamName) : RescueTeam(teamName) {}

    void respond() override {
        cout << teamName << " (specialized diving team) is responding with diving gear." << endl;
    }
};

/* =====================================================================
 * PART 3: VIRTUAL BASE CLASS
 * =====================================================================
 * Problem: if TWO classes (Citizen, RescueTeamMember) both inherit
 * from Person, and a THIRD class (VolunteerRescuer) inherits from
 * BOTH of them, then VolunteerRescuer ends up with TWO separate
 * copies of Person's data -- one through each parent. This is the
 * "diamond problem", and it causes ambiguity.
 *
 * Fix: inherit Person as "virtual" in both Citizen and
 * RescueTeamMember. Then C++ guarantees only ONE shared copy of
 * Person exists inside VolunteerRescuer.
 * ===================================================================== */

class Person {
protected:
    string name;
public:
    Person(string name) {
        this->name = name;
    }
};

// virtual inheritance -> only one shared Person will exist
class Citizen : virtual public Person {
public:
    Citizen(string name) : Person(name) {}
};

class RescueTeamMember : virtual public Person {
public:
    RescueTeamMember(string name) : Person(name) {}
};

// Inherits from BOTH Citizen and RescueTeamMember (diamond shape).
// Because both used "virtual" inheritance, there is only ONE "name"
// field here, not two conflicting ones.
class VolunteerRescuer : public Citizen, public RescueTeamMember {
public:
    // Person's constructor must be called directly here since it's
    // the shared virtual base.
    VolunteerRescuer(string name) : Person(name), Citizen(name), RescueTeamMember(name) {}

    void show() {
        cout << "Volunteer: " << name << " (works as both Citizen and Rescue Team Member)" << endl;
    }
};

int main() {
    cout << "--- PART 1 & 2: this pointer + virtual function ---" << endl;

    // "this" pointer used for chaining:
    RescueTeam team("Bravo Team");
    team.setName("Bravo Rescue Team").respond();

    cout << "\n--- PART 2: virtual function through a base class pointer ---" << endl;

    /* =================================================================
     * PART 4: POINTER TO OBJECTS & POINTER TO DERIVED CLASSES
     * ================================================================= */

    // POINTER TO OBJECT (base class)
    RescueTeam* basicPtr = new RescueTeam("Charlie Team");
    basicPtr->respond();

    // POINTER TO DERIVED CLASS OBJECT
    SpecializedRescueTeam* diveTeamPtr = new SpecializedRescueTeam("Delta Dive Team");
    diveTeamPtr->respond();

    // A BASE CLASS POINTER pointing to a DERIVED CLASS OBJECT.
    // Because respond() is virtual, the DERIVED version still runs,
    // even though the pointer's declared type is the base class.
    RescueTeam* polymorphicPtr = diveTeamPtr;
    cout << "Calling through a base-class pointer that points to a derived object:" << endl;
    polymorphicPtr->respond();   // prints the SPECIALIZED version, not the general one

    delete basicPtr;
    delete diveTeamPtr;

    cout << "\n--- PART 3: virtual base class ---" << endl;
    VolunteerRescuer volunteer("Meera Shah");
    volunteer.show();

    return 0;
}