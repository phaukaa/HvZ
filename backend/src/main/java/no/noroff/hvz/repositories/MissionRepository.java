package no.noroff.hvz.repositories;

import no.noroff.hvz.models.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}
