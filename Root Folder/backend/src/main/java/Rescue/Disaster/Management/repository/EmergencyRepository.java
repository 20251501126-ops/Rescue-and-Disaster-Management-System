package Rescue.Disaster.Management.repository;

import Rescue.Disaster.Management.model.Emergency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyRepository extends JpaRepository<Emergency, String> {
}