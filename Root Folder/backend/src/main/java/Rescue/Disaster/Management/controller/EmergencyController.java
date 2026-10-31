package Rescue.Disaster.Management.controller;

import Rescue.Disaster.Management.model.Emergency;
import Rescue.Disaster.Management.service.EmergencyService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergencies")
public class EmergencyController {

    private final EmergencyService emergencyService;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    // 1. Report Emergency
    @PostMapping
    public Emergency createEmergency(
            @Valid @RequestBody Emergency emergency) {

        return emergencyService.saveEmergency(emergency);
    }

    // 2. Get All Emergencies
    @GetMapping
    public List<Emergency> getAllEmergencies() {
        return emergencyService.getAllEmergencies();
    }

    // 3. Verify Emergency
    @PutMapping("/{id}/verify")
    public Emergency verifyEmergency(@PathVariable String id) {
        return emergencyService.verifyEmergency(id);
    }

    // 4. Assign Rescue Team
    @PutMapping("/{id}/assign")
    public Emergency assignTeam(@PathVariable String id) {
        return emergencyService.assignTeam(id);
    }

    // 5. Complete Emergency
    @PutMapping("/{id}/complete")
    public Emergency completeEmergency(@PathVariable String id) {
        return emergencyService.completeEmergency(id);
    }
}