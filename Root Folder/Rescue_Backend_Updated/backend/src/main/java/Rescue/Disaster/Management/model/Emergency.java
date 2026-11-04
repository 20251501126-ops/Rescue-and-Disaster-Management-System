package Rescue.Disaster.Management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

@Entity
public class Emergency {

    @Id
    private String emergencyId;

    @NotBlank(message = "Emergency type is required")
    private String type;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Severity is required")
    private String severity;

    @Min(value = 1, message = "People affected must be at least 1")
    private int peopleAffected;

    @NotBlank(message = "Reported by is required")
    private String reportedBy;

    private String status;

    private String assignedTeamId;

    // Default Constructor
    public Emergency() {
        this.status = "Reported";
    }

    // Parameterized Constructor
    public Emergency(String emergencyId, String type, String location,
                     String severity, int peopleAffected, String reportedBy) {

        this.emergencyId = emergencyId;
        this.type = type;
        this.location = location;
        this.severity = severity;
        this.peopleAffected = peopleAffected;
        this.reportedBy = reportedBy;
        this.status = "Reported";
    }

    // Getters and Setters

    public String getEmergencyId() {
        return emergencyId;
    }

    public void setEmergencyId(String emergencyId) {
        this.emergencyId = emergencyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int getPeopleAffected() {
        return peopleAffected;
    }

    public void setPeopleAffected(int peopleAffected) {
        this.peopleAffected = peopleAffected;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTeamId() {
        return assignedTeamId;
    }

    public void setAssignedTeamId(String assignedTeamId) {
        this.assignedTeamId = assignedTeamId;
    }
}