
package Rescue.Disaster.Management.service;

import Rescue.Disaster.Management.model.Emergency;
import Rescue.Disaster.Management.model.RescueTeam;

import Rescue.Disaster.Management.repository.EmergencyRepository;
import Rescue.Disaster.Management.repository.RescueTeamRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmergencyService {

    private final EmergencyRepository emergencyRepository;
    private final RescueTeamRepository rescueTeamRepository;

    public EmergencyService(
            EmergencyRepository emergencyRepository,
            RescueTeamRepository rescueTeamRepository) {

        this.emergencyRepository = emergencyRepository;
        this.rescueTeamRepository = rescueTeamRepository;
    }

    // 1. Report Emergency
    public Emergency saveEmergency(Emergency emergency) {

        if (emergency.getEmergencyId() == null ||
                emergency.getEmergencyId().isBlank()) {

            String id = "E-" + UUID.randomUUID()
                    .toString()
                    .substring(0, 8)
                    .toUpperCase();

            emergency.setEmergencyId(id);
        }

        emergency.setStatus("Reported");

        return emergencyRepository.save(emergency);
    }

    // 2. Get All Emergencies
    public List<Emergency> getAllEmergencies() {

        return emergencyRepository.findAll();
    }

    // 3. Verify Emergency
    public Emergency verifyEmergency(String id) {

        Emergency emergency = getEmergency(id);

        if (!"Reported".equalsIgnoreCase(emergency.getStatus())) {
            throw new IllegalStateException(
                    "Only reported emergencies can be verified."
            );
        }

        emergency.setStatus("Verified");

        return emergencyRepository.save(emergency);
    }

    // 4. Reject Emergency
    public Emergency rejectEmergency(String id) {

        Emergency emergency = getEmergency(id);

        if (emergency.getAssignedTeamId() != null) {
            throw new IllegalStateException(
                    "Assigned emergencies cannot be rejected."
            );
        }

        if ("Completed".equalsIgnoreCase(emergency.getStatus()) ||
                "Rejected".equalsIgnoreCase(emergency.getStatus())) {
            throw new IllegalStateException(
                    "This emergency cannot be rejected."
            );
        }

        emergency.setStatus("Rejected");

        return emergencyRepository.save(emergency);
    }

    // 5. Assign Rescue Team
    public Emergency assignTeam(String id, String teamId) {

        Emergency emergency = getEmergency(id);

        if (!"Verified".equalsIgnoreCase(emergency.getStatus())) {
            throw new IllegalStateException(
                    "Emergency must be verified before assignment."
            );
        }

        RescueTeam team;

        // If authority selects a particular team
        if (teamId != null && !teamId.isBlank()) {

            team = rescueTeamRepository.findById(teamId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Rescue team not found: " + teamId
                            )
                    );

        } else {

            // Automatically find an available team
            team = rescueTeamRepository
                    .findFirstByTeamStatusIgnoreCase("Available")
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "No available rescue team."
                            )
                    );
        }

        if (!"Available".equalsIgnoreCase(team.getTeamStatus())) {
            throw new IllegalStateException(
                    "Selected rescue team is not available."
            );
        }

        emergency.setAssignedTeamId(team.getTeamId());
        emergency.setStatus("Assigned");

        team.setTeamStatus("En Route");

        rescueTeamRepository.save(team);

        return emergencyRepository.save(emergency);
    }

    // 6. Complete Emergency
    public Emergency completeEmergency(String id) {

        Emergency emergency = getEmergency(id);

        if (emergency.getAssignedTeamId() == null) {
            throw new IllegalStateException(
                    "No rescue team is assigned to this emergency."
            );
        }

        if (!"Assigned".equalsIgnoreCase(emergency.getStatus())) {
            throw new IllegalStateException(
                    "Only assigned emergencies can be completed."
            );
        }

        RescueTeam team = rescueTeamRepository
                .findById(emergency.getAssignedTeamId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Assigned rescue team not found."
                        )
                );

        emergency.setStatus("Completed");
        team.setTeamStatus("Completed");

        rescueTeamRepository.save(team);

        return emergencyRepository.save(emergency);
    }

    // Common helper method
    private Emergency getEmergency(String id) {

        return emergencyRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Emergency not found: " + id
                        )
                );
    }
}
