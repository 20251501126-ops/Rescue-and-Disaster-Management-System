package Rescue.Disaster.Management.service;

import Rescue.Disaster.Management.model.RescueTeam;
import Rescue.Disaster.Management.repository.RescueTeamRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RescueTeamService {

    private final RescueTeamRepository rescueTeamRepository;

    public RescueTeamService(RescueTeamRepository rescueTeamRepository) {
        this.rescueTeamRepository = rescueTeamRepository;
    }

    public RescueTeam addTeam(RescueTeam team) {
        return rescueTeamRepository.save(team);
    }

    public List<RescueTeam> getAllTeams() {
        return rescueTeamRepository.findAll();
    }

     public RescueTeam updateTeamStatus(String teamId, String status) {

    RescueTeam team = rescueTeamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Rescue team not found"));

    if (status == null || status.isBlank()) {
        throw new IllegalArgumentException("Status cannot be empty");
    }

    String normalizedStatus = status.trim();

    if (!normalizedStatus.equalsIgnoreCase("Available")
            && !normalizedStatus.equalsIgnoreCase("En Route")
            && !normalizedStatus.equalsIgnoreCase("In Action")
            && !normalizedStatus.equalsIgnoreCase("Completed")) {

        throw new IllegalArgumentException("Invalid team status");
    }

    team.setTeamStatus(normalizedStatus);

    return rescueTeamRepository.save(team);
   }
}