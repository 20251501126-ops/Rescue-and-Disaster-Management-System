package Rescue.Disaster.Management.controller;

import Rescue.Disaster.Management.model.Emergency;
import Rescue.Disaster.Management.service.EmergencyService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergencies")
public class EmergencyController {

    private final EmergencyService emergencyService;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    @PostMapping
    public Emergency createEmergency(@RequestBody Emergency emergency) {
        return emergencyService.saveEmergency(emergency);
    }

    @GetMapping
    public List<Emergency> getAllEmergencies() {
        return emergencyService.getAllEmergencies();
    }
}