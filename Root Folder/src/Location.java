/**
 * COMPOSITION (the "has-a" object that only makes sense inside its owner)
 * -----------------------------------------------------------------------
 * A Location, in this simple model, only exists because an Emergency
 * needs one. It is created inside Emergency's constructor and its
 * lifetime is tied to the Emergency that owns it — that tight,
 * "owner creates and destroys the part" relationship is what
 * distinguishes COMPOSITION from a looser has-a (aggregation).
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

    public String getArea() {
        return area;
    }

    @Override
    public String toString() {
        return area + " (" + latitude + ", " + longitude + ")";
    }
}