package Rescue.Disaster.Management.repository;

import Rescue.Disaster.Management.model.RescueTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RescueTeamRepository
        extends JpaRepository<RescueTeam, String> {
}