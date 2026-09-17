#include <iostream>
using namespace std;

/*
 * STREAM OPERATOR OVERLOADING USING A FRIEND FUNCTION (basic example)
 * ---------------------------------------------------------------------
 * The "stream operators" are:
 *   <<  (insertion / output)  -> used with cout
 *   >>  (extraction / input)  -> used with cin
 *
 * We want to write:   cout << e1;      (print an Emergency directly)
 *                      cin  >> e1;      (read an Emergency directly)
 * exactly like we do with normal types (int, string, etc.).
 *
 * These operators MUST be written as functions OUTSIDE the class
 * (because the object always appears on the RIGHT side of << or >>,
 * not the left), but they still need to touch the class's PRIVATE
 * data. That is exactly why they are declared as FRIEND functions
 * inside the class.
 */

class Emergency {
private:
    int peopleAffected;   // private data

public:
    // Constructor
    Emergency(int peopleAffected = 0) {
        this->peopleAffected = peopleAffected;
    }

    // Friend declarations for the two stream operators.
    friend ostream& operator<<(ostream &out, const Emergency &e);
    friend istream& operator>>(istream &in, Emergency &e);
};

// OVERLOADING << (output stream operator) using a friend function
ostream& operator<<(ostream &out, const Emergency &e) {
    out << "People Affected: " << e.peopleAffected;   // private data accessed directly
    return out;
}

// OVERLOADING >> (input stream operator) using a friend function
istream& operator>>(istream &in, Emergency &e) {
    cout << "Enter number of people affected: ";
    in >> e.peopleAffected;   // private data accessed directly
    return in;
}

int main() {
    Emergency e1(15);

    cout << "Emergency 1 -> " << e1 << endl;   // uses overloaded <<

    Emergency e2;
    cin >> e2;                                 // uses overloaded >>
    cout << "Emergency 2 -> " << e2 << endl;   // uses overloaded << again

    return 0;
}