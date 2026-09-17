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

#include <iostream>
using namespace std;

class Citizen {
private:
    string name;
    string phone;

public:
    Citizen(string n, string p) {
        name = n;
        phone = p;
    }

    friend ostream& operator<<(ostream& out, const Citizen& c) {
        out << "Name: " << c.name << endl;
        out << "Phone: " << c.phone << endl;

        return out;
    }
};

int main() {

    Citizen c("Rahul", "9876543210");

    cout << c;

    return 0;
}