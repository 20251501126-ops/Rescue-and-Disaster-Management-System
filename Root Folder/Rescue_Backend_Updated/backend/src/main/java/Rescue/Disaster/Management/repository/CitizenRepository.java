package Rescue.Disaster.Management.repository;
import Rescue.Disaster.Management.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CitizenRepository extends JpaRepository<Citizen, String> {}
