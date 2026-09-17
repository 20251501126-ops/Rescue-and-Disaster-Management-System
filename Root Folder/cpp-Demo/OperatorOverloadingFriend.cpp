#include <iostream>
using namespace std;

class Emergency {
private:
    int peopleAffected;   // private data

public:
    // Constructor
    Emergency(int peopleAffected) {
        this->peopleAffected = peopleAffected;
    }

    void display() {
        cout << "People Affected: " << peopleAffected << endl;
    }

    friend Emergency operator+(Emergency e1, Emergency e2);
};

Emergency operator+(Emergency e1, Emergency e2) {
    int total = e1.peopleAffected + e2.peopleAffected;  // private data accessed directly
    return Emergency(total);
}

int main() {
    Emergency e1(12);
    Emergency e2(8);

    cout << "Emergency 1: ";
    e1.display();

    cout << "Emergency 2: ";
    e2.display();
    
    Emergency merged = e1 + e2;

    cout << "Merged Emergency: ";
    merged.display();

    return 0;
}