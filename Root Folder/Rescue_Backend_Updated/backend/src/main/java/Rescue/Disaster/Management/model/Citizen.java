package Rescue.Disaster.Management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Citizen {
    @Id
    private String citizenId;
    @NotBlank(message = "Citizen name is required")
    private String name;
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must contain exactly 10 digits")
    private String contactNumber;
    @Email(message = "Enter a valid email address")
    private String email;
    private String address;

    public Citizen() {}
    public Citizen(String citizenId, String name, String contactNumber, String email, String address) {
        this.citizenId = citizenId; this.name = name; this.contactNumber = contactNumber;
        this.email = email; this.address = address;
    }
    public String getCitizenId() { return citizenId; }
    public void setCitizenId(String citizenId) { this.citizenId = citizenId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
