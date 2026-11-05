package Rescue.Disaster.Management.controller;

import Rescue.Disaster.Management.model.RescueTeam;
import Rescue.Disaster.Management.service.RescueTeamService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class RescueTeamController {

    private final RescueTeamService rescueTeamService;

    public RescueTeamController(RescueTeamService rescueTeamService) {
        this.rescueTeamService = rescueTeamService;
    }

    @PostMapping
    public RescueTeam addTeam(@RequestBody RescueTeam team) {
        return rescueTeamService.addTeam(team);
    }

    @GetMapping
    public List<RescueTeam> getAllTeams() {
        return rescueTeamService.getAllTeams();
    }

    @PutMapping("/{teamId}/status")
    public RescueTeam updateTeamStatus(
            @PathVariable String teamId,
            @RequestParam String status) {

        return rescueTeamService.updateTeamStatus(teamId, status);
    }
}