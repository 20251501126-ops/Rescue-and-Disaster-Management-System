
package Rescue.Disaster.Management.repository;

import Rescue.Disaster.Management.model.RescueTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RescueTeamRepository extends JpaRepository<RescueTeam, String> {

    long countByTeamStatusIgnoreCase(String teamStatus);

    Optional<RescueTeam> findFirstByTeamStatusIgnoreCase(String teamStatus);
}
