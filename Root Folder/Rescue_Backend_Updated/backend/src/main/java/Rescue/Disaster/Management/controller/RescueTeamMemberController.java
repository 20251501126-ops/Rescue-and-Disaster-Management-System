package Rescue.Disaster.Management.controller;
import Rescue.Disaster.Management.model.RescueTeamMember;
import Rescue.Disaster.Management.service.RescueTeamMemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/team-members")
public class RescueTeamMemberController {
    private final RescueTeamMemberService service;
    public RescueTeamMemberController(RescueTeamMemberService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public RescueTeamMember create(@Valid @RequestBody RescueTeamMember member) { return service.create(member); }
    @GetMapping public List<RescueTeamMember> getAll() { return service.getAll(); }
    @GetMapping("/team/{teamId}") public List<RescueTeamMember> getByTeam(@PathVariable String teamId) { return service.getByTeam(teamId); }
    @PutMapping("/{id}/status") public RescueTeamMember updateStatus(@PathVariable String id, @RequestParam String status) { return service.updateStatus(id, status); }
}
