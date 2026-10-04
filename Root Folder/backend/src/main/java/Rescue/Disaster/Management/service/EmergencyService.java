package Rescue.Disaster.Management.service;

import Rescue.Disaster.Management.model.Emergency;
import Rescue.Disaster.Management.repository.EmergencyRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyService {

    private final EmergencyRepository emergencyRepository;

    private int nextId = 1;

    public EmergencyService(EmergencyRepository emergencyRepository) {
        this.emergencyRepository = emergencyRepository;
    }

    public Emergency saveEmergency(Emergency emergency) {

        String id = String.format("E%03d", nextId++);

        emergency.setEmergencyId(id);

        return emergencyRepository.save(emergency);
    }

    public List<Emergency> getAllEmergencies() {

        return emergencyRepository.findAll();
    }
}