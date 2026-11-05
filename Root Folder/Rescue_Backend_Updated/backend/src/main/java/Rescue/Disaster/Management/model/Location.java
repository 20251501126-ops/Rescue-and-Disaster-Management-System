package Rescue.Disaster.Management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Location {
    @Id
    private String locationId;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "City is required")
    private String city;
    private String state;
    private Double latitude;
    private Double longitude;
    private String description;

    public Location() {}
    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
