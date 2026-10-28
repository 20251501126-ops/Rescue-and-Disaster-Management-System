#include <iostream>
#include <string>
using namespace std;

/* =====================================================================
 * TYPE CASTING BETWEEN CLASSES AND BASIC TYPES  (basic level)
 * =====================================================================
 * C++ already converts int -> double automatically. For OUR classes we
 * must teach C++ how to convert, using special functions:
 *
 *  1) BASIC -> CLASS : a CONSTRUCTOR with one argument
 *                      (conversion constructor, in the destination class)
 *  2) CLASS -> BASIC : a CASTING OPERATOR function, "operator int()"
 *                      (in the source class)
 *  3) CLASS -> CLASS : a CASTING OPERATOR in the source class
 *                      (or a one-argument constructor in the destination)
 * ===================================================================== */

// Destination class for the CLASS -> CLASS example
class AlertMessage {
private:
    string text;
public:
    AlertMessage(string text = "") { this->text = text; }
    void display() { cout << "ALERT: " << text << endl; }
};

class Emergency {
private:
    int peopleAffected;
    string type;

public:
    Emergency(string type = "Unknown", int peopleAffected = 0) {
        this->type = type;
        this->peopleAffected = peopleAffected;
    }

    // 1) BASIC -> CLASS
    // One-argument constructor: lets us write  Emergency e = 15;
    // The int 15 is automatically converted into an Emergency object.
    Emergency(int peopleAffected) {
        this->type = "Unknown";
        this->peopleAffected = peopleAffected;
    }

    // 2) CLASS -> BASIC
    // Casting operator: lets us write  int n = e;
    // No return type is written; it is implied by the name "int".
    operator int() {
        return peopleAffected;
    }

    // 3) CLASS -> CLASS
    // Casting operator that converts an Emergency into an AlertMessage.
    operator AlertMessage() {
        return AlertMessage(type + " reported, " + to_string(peopleAffected) + " people affected");
    }

    void display() {
        cout << "Emergency: " << type << ", people affected = " << peopleAffected << endl;
    }
};

/* =====================================================================
 * TEMPLATES
 * =====================================================================
 * A template is a "blueprint" that works for ANY data type, so we write
 * the code once instead of once per type. The compiler creates the real
 * version (int, double, ...) when we use it.
 * (In Java, the same idea is called GENERICS.)
 * ===================================================================== */

// FUNCTION TEMPLATE: works for int, double, etc.
template <typename T>
T getLarger(T a, T b) {
    return (a > b) ? a : b;
}

// CLASS TEMPLATE: a rescue resource whose quantity can be int or double
template <typename T>
class Resource {
private:
    string name;
    T quantity;
public:
    Resource(string name, T quantity) {
        this->name = name;
        this->quantity = quantity;
    }
    void display() {
        cout << name << ": " << quantity << endl;
    }
};

int main() {
    cout << "--- 1) BASIC -> CLASS (int to Emergency) ---" << endl;
    Emergency e1 = 15;          // conversion constructor runs here
    e1.display();

    cout << "\n--- 2) CLASS -> BASIC (Emergency to int) ---" << endl;
    Emergency e2("Flood", 40);
    int people = e2;            // casting operator int() runs here
    cout << "People affected as a plain int: " << people << endl;

    cout << "\n--- 3) CLASS -> CLASS (Emergency to AlertMessage) ---" << endl;
    AlertMessage alert = e2;    // casting operator AlertMessage() runs here
    alert.display();

    cout << "\n--- 4) TEMPLATES ---" << endl;
    cout << "Larger of 12 and 8 (int): " << getLarger(12, 8) << endl;
    cout << "Larger of 2.5 and 7.1 (double): " << getLarger(2.5, 7.1) << endl;

    Resource<int> boats("Rescue boats", 6);        // T = int
    Resource<double> fuel("Fuel (litres)", 250.5); // T = double
    boats.display();
    fuel.display();

    return 0;
}