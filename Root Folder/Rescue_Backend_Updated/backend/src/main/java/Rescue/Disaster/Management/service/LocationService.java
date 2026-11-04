package Rescue.Disaster.Management.service;
import Rescue.Disaster.Management.model.Location;
import Rescue.Disaster.Management.repository.LocationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
@Service
public class LocationService {
    private final LocationRepository repository;
    public LocationService(LocationRepository repository) { this.repository = repository; }
    public Location create(Location location) { location.setLocationId("LOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()); return repository.save(location); }
    public List<Location> getAll() { return repository.findAll(); }
    public Location getById(String id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("Location not found: " + id)); }
    public Location update(String id, Location input) {
        Location l = getById(id); l.setAddress(input.getAddress()); l.setCity(input.getCity()); l.setState(input.getState());
        l.setLatitude(input.getLatitude()); l.setLongitude(input.getLongitude()); l.setDescription(input.getDescription()); return repository.save(l);
    }
    public void delete(String id) { repository.delete(getById(id)); }
}
