package Rescue.Disaster.Management.controller;
import Rescue.Disaster.Management.dto.DashboardSummary;
import Rescue.Disaster.Management.repository.CitizenRepository;
import Rescue.Disaster.Management.repository.EmergencyRepository;
import Rescue.Disaster.Management.repository.RescueTeamMemberRepository;
import Rescue.Disaster.Management.repository.RescueTeamRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final EmergencyRepository emergencies;
    private final RescueTeamRepository teams;
    private final CitizenRepository citizens;
    private final RescueTeamMemberRepository members;
    public DashboardController(EmergencyRepository emergencies, RescueTeamRepository teams,
            CitizenRepository citizens, RescueTeamMemberRepository members) {
        this.emergencies = emergencies; this.teams = teams; this.citizens = citizens; this.members = members;
    }
    @GetMapping("/summary")
    public DashboardSummary getSummary() {
        return new DashboardSummary(emergencies.count(), emergencies.countByStatusIgnoreCase("Reported"),
            emergencies.countByStatusIgnoreCase("Verified"), emergencies.countByStatusIgnoreCase("Assigned"),
            emergencies.countByStatusIgnoreCase("Completed"), teams.count(), teams.countByTeamStatusIgnoreCase("Available"),
            citizens.count(), members.count());
    }
}
