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
    int id;
    string type;
    string location;
    string status;

public:
    Emergency(int i, string t, string l) {
        id = i;
        type = t;
        location = l;
        status = "Pending";
    }

    friend ostream& operator<<(ostream& out, const Emergency& e) {
        out << "Emergency ID: " << e.id << endl;
        out << "Type: " << e.type << endl;
        out << "Location: " << e.location << endl;
        out << "Status: " << e.status << endl;

        return out;
    }
};

int main() {
    Emergency e1(15, "Fire", "123 Main St");
    cout << "Emergency 1 -> " << e1 << endl;   // uses overloaded <<

    return 0;
}