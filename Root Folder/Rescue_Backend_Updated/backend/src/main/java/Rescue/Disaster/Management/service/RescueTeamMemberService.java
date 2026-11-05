package Rescue.Disaster.Management.service;
import Rescue.Disaster.Management.model.RescueTeamMember;
import Rescue.Disaster.Management.repository.RescueTeamMemberRepository;
import Rescue.Disaster.Management.repository.RescueTeamRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
@Service
public class RescueTeamMemberService {
    private final RescueTeamMemberRepository memberRepository;
    private final RescueTeamRepository teamRepository;
    public RescueTeamMemberService(RescueTeamMemberRepository memberRepository, RescueTeamRepository teamRepository) {
        this.memberRepository = memberRepository; this.teamRepository = teamRepository;
    }
    public RescueTeamMember create(RescueTeamMember member) {
        if (!teamRepository.existsById(member.getTeamId())) throw new RuntimeException("Rescue team not found: " + member.getTeamId());
        member.setMemberId("MEM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        if (member.getMemberStatus() == null || member.getMemberStatus().isBlank()) member.setMemberStatus("Active");
        return memberRepository.save(member);
    }
    public List<RescueTeamMember> getAll() { return memberRepository.findAll(); }
    public List<RescueTeamMember> getByTeam(String teamId) {
        if (!teamRepository.existsById(teamId)) throw new RuntimeException("Rescue team not found: " + teamId);
        return memberRepository.findByTeamId(teamId);
    }
    public RescueTeamMember updateStatus(String id, String status) {
        RescueTeamMember member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Team member not found: " + id));
        if (status == null || (!status.equalsIgnoreCase("Active") && !status.equalsIgnoreCase("Inactive"))) throw new IllegalArgumentException("Member status must be Active or Inactive");
        member.setMemberStatus(status.trim()); return memberRepository.save(member);
    }
}
