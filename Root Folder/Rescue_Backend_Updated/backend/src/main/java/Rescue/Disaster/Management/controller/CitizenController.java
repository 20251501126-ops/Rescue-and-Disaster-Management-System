package Rescue.Disaster.Management.controller;
import Rescue.Disaster.Management.model.Citizen;
import Rescue.Disaster.Management.service.CitizenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/citizens")
public class CitizenController {
    private final CitizenService service;
    public CitizenController(CitizenService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Citizen create(@Valid @RequestBody Citizen citizen) { return service.create(citizen); }
    @GetMapping public List<Citizen> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public Citizen getById(@PathVariable String id) { return service.getById(id); }
    @PutMapping("/{id}") public Citizen update(@PathVariable String id, @Valid @RequestBody Citizen citizen) { return service.update(id, citizen); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { service.delete(id); }
}
