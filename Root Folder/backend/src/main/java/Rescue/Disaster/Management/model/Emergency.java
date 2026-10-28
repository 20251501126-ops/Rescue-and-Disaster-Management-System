package Rescue.Disaster.Management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Emergency {

    @Id
    private String emergencyId;

    private String type;
    private String location;
    private String severity;
    private int peopleAffected;
    private String reportedBy;
    private String status;

    public Emergency() {
        this.status = "Reported";
    }

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
}