/**
 * COMPOSITION (the "has-a" object that only makes sense inside its owner)
 * A Location, in this simple model, only exists because an Emergency
 * needs one. It is created inside Emergency's constructor
 */
public class Location {

    private double latitude;
    private double longitude;
    private String area;

    public Location(double latitude, double longitude, String area) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.area = area;
    }

    public String getArea() {  // getter for area
        return area;
    }
    public String toString() {
        return area + " (" + latitude + ", " + longitude + ")";
    }
}