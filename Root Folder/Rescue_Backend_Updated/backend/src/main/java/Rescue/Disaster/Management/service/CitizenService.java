package Rescue.Disaster.Management.service;
import Rescue.Disaster.Management.model.Citizen;
import Rescue.Disaster.Management.repository.CitizenRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
@Service
public class CitizenService {
    private final CitizenRepository repository;
    public CitizenService(CitizenRepository repository) { this.repository = repository; }
    public Citizen create(Citizen citizen) { citizen.setCitizenId("CIT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()); return repository.save(citizen); }
    public List<Citizen> getAll() { return repository.findAll(); }
    public Citizen getById(String id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("Citizen not found: " + id)); }
    public Citizen update(String id, Citizen input) {
        Citizen citizen = getById(id); citizen.setName(input.getName()); citizen.setContactNumber(input.getContactNumber());
        citizen.setEmail(input.getEmail()); citizen.setAddress(input.getAddress()); return repository.save(citizen);
    }
    public void delete(String id) { Citizen citizen = getById(id); repository.delete(citizen); }
}
