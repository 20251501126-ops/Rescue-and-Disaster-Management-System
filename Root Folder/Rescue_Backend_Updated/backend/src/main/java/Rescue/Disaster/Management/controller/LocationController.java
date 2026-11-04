package Rescue.Disaster.Management.controller;
import Rescue.Disaster.Management.model.Location;
import Rescue.Disaster.Management.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService service;
    public LocationController(LocationService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Location create(@Valid @RequestBody Location location) { return service.create(location); }
    @GetMapping public List<Location> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public Location getById(@PathVariable String id) { return service.getById(id); }
    @PutMapping("/{id}") public Location update(@PathVariable String id, @Valid @RequestBody Location location) { return service.update(id, location); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { service.delete(id); }
}
