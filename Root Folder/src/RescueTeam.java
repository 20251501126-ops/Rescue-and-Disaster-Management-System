import java.util.ArrayList;
import java.util.List;

public class RescueTeam {

    private String teamId;
    private String teamName;
    private String teamStatus;
    String currentLocation;

    // COMPOSITION: a RescueTeam "has-a" list of RescueTeamMembers.
    // The team creates/owns this list; members are added only through
    // the team's own methods.
    private List<RescueTeamMember> members;

    public RescueTeam(String teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamStatus = "Available";
        this.members = new ArrayList<>();
    }

    public void addMember(RescueTeamMember member) {
        members.add(member);
    }

    public void acceptTask(Emergency emergency) {
        emergency.assignTeam(this);  // this means the current RescueTeam object
        updateStatus("En Route");
    }

    // METHOD OVERLOADING (version 1) — just change the status
    public void updateStatus(String status) {
        this.teamStatus = status;
    }

    // METHOD OVERLOADING (version 2) — change status AND location
    public void updateStatus(String status, String newLocation) {
        this.teamStatus = status;
        this.currentLocation = newLocation;
    }

    public String getTeamId() { return teamId; }
    public String getTeamName() { return teamName; }
    public String getTeamStatus() { return teamStatus; }
    public List<RescueTeamMember> getMembers() { return members; }

    public String getSummary() {
        return teamName + " [" + teamId + "] - status=" + teamStatus
                + ", members=" + members.size();
    }
}