package Rescue.Disaster.Management.repository;
import Rescue.Disaster.Management.model.RescueTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RescueTeamMemberRepository extends JpaRepository<RescueTeamMember, String> {
    List<RescueTeamMember> findByTeamId(String teamId);
}
