package Rescue.Disaster.Management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RescueTeam {

    @Id
    private String teamId;

    private String teamName;
    private String teamStatus;
    private String currentLocation;

    public RescueTeam() {
        this.teamStatus = "Available";
    }

    public RescueTeam(String teamId, String teamName, String currentLocation) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.currentLocation = currentLocation;
        this.teamStatus = "Available";
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(String teamStatus) {
        this.teamStatus = teamStatus;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}