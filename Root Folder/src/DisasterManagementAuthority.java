import java.util.ArrayList;
import java.util.List;

public class DisasterManagementAuthority {

    private String authorityId;
    private String name;

    // COMPOSITION: the authority "has-a" list of teams and emergencies
    // it manages.
    private List<RescueTeam> rescueTeams;
    private List<Emergency> emergencies;

    public DisasterManagementAuthority(String authorityId, String name) {
        this.authorityId = authorityId;
        this.name = name;
        this.rescueTeams = new ArrayList<>();
        this.emergencies = new ArrayList<>();
    }

    public void registerTeam(RescueTeam team) {
        rescueTeams.add(team);
    }

    public void verifyEmergency(Emergency emergency) {
        emergencies.add(emergency);
        emergency.updateStatus("Verified");
    }

    // Picks the first available team and assigns it.
    public void assignRescueTeam(Emergency emergency) {
        for (RescueTeam team : rescueTeams) {
            if (team.getTeamStatus().equals("Available")) {
                team.acceptTask(emergency);
                return;
            }
        }
        System.out.println("No available rescue team for " + emergency.getEmergencyId());
    }

    public String getName() { return name; }
    public List<RescueTeam> getRescueTeams() { return rescueTeams; }
    public List<Emergency> getEmergencies() { return emergencies; }
}