package Rescue.Disaster.Management.service;

import Rescue.Disaster.Management.model.Emergency;
import Rescue.Disaster.Management.model.RescueTeam;
import Rescue.Disaster.Management.repository.EmergencyRepository;
import Rescue.Disaster.Management.repository.RescueTeamRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmergencyService {

    private final EmergencyRepository emergencyRepository;
    private final RescueTeamRepository rescueTeamRepository;

    private int nextId = 1;

    public EmergencyService(
            EmergencyRepository emergencyRepository,
            RescueTeamRepository rescueTeamRepository) {

        this.emergencyRepository = emergencyRepository;
        this.rescueTeamRepository = rescueTeamRepository;
    }

    // 1. Create / Report Emergency
    public Emergency saveEmergency(Emergency emergency) {

        String id = String.format("E%03d", nextId++);

        emergency.setEmergencyId(id);
        emergency.setStatus("Reported");

        return emergencyRepository.save(emergency);
    }

    // 2. Get All Emergencies
    public List<Emergency> getAllEmergencies() {

        return emergencyRepository.findAll();
    }

    // 3. Verify Emergency
    public Emergency verifyEmergency(String id) {

        Emergency emergency = emergencyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Emergency not found: " + id));

        emergency.setStatus("Verified");

        return emergencyRepository.save(emergency);
    }

    // 4. Assign Available Rescue Team
    @Transactional
    public Emergency assignTeam(String emergencyId) {

        Emergency emergency = emergencyRepository.findById(emergencyId)
                .orElseThrow(() ->
                        new RuntimeException("Emergency not found: " + emergencyId));

        if (!"Verified".equalsIgnoreCase(emergency.getStatus())) {

            throw new IllegalStateException(
                    "Only verified emergencies can be assigned."
            );
        }

        RescueTeam team = rescueTeamRepository.findAll()
                .stream()
                .filter(t -> "Available".equalsIgnoreCase(t.getTeamStatus()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("No available rescue team found.")
                );

        emergency.setAssignedTeamId(team.getTeamId());
        emergency.setStatus("Assigned");

        team.setTeamStatus("En Route");

        rescueTeamRepository.save(team);

        return emergencyRepository.save(emergency);
    }

    // 5. Complete Emergency
    @Transactional
    public Emergency completeEmergency(String emergencyId) {

        Emergency emergency = emergencyRepository.findById(emergencyId)
                .orElseThrow(() ->
                        new RuntimeException("Emergency not found: " + emergencyId));

        if (!"Assigned".equalsIgnoreCase(emergency.getStatus())
                && !"En Route".equalsIgnoreCase(emergency.getStatus())
                && !"In Action".equalsIgnoreCase(emergency.getStatus())) {

            throw new IllegalStateException(
                    "Only assigned or active emergencies can be completed."
            );
        }

        emergency.setStatus("Completed");

        String teamId = emergency.getAssignedTeamId();

        if (teamId != null) {

            RescueTeam team = rescueTeamRepository.findById(teamId)
                    .orElseThrow(() ->
                            new RuntimeException("Assigned team not found: " + teamId));

            team.setTeamStatus("Available");

            rescueTeamRepository.save(team);
        }

        return emergencyRepository.save(emergency);
    }
}