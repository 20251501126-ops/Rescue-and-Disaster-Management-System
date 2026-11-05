#include <iostream>
using namespace std;

/*
 * OBJECT SLICING (basic example)
 * --------------------------------
 * OBJECT SLICING happens when you assign a DERIVED class object to a
 * BASE class object BY VALUE (not by pointer/reference). Only the
 * base-class part gets copied — the extra derived-class data is
 * "sliced off" and lost.
 *
 * Note: this is a C++-specific problem. It happens because C++ lets
 * you copy objects by value. Java does NOT have this problem, because
 * in Java, object variables are always references, not full value
 * copies — so this concept can only really be shown in C++.
 */

// BASE CLASS
class RescueTeam {
protected:
    string teamName;

public:
    RescueTeam(string teamName) {
        this->teamName = teamName;
    }

    void display() {
        cout << "Team Name: " << teamName << endl;
    }
};

// DERIVED CLASS — adds one EXTRA piece of data (equipmentType) that
// RescueTeam does not have.
class SpecializedRescueTeam : public RescueTeam {
private:
    string equipmentType;

public:
    SpecializedRescueTeam(string teamName, string equipmentType)
        : RescueTeam(teamName) {
        this->equipmentType = equipmentType;
    }

    void display() {
        cout << "Team Name: " << teamName
             << ", Equipment: " << equipmentType << endl;
    }
};

int main() {
    SpecializedRescueTeam diveTeam("Alpha Rescue Team", "Diving Gear");

    cout << "Original derived object:" << endl;
    diveTeam.display();   // shows BOTH teamName and equipmentType

    // OBJECT SLICING happens right here:
    // We are assigning a SpecializedRescueTeam object into a plain
    // RescueTeam object, BY VALUE. Only the RescueTeam part is
    // copied. "equipmentType" is sliced off and lost.
    RescueTeam basicTeam = diveTeam;

    cout << "\nAfter object slicing (copied into base class object):" << endl;
    basicTeam.display();   // only teamName remains — equipmentType is gone

    return 0;
}