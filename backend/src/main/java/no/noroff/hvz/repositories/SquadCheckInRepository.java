package no.noroff.hvz.repositories;

import no.noroff.hvz.models.SquadCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SquadCheckInRepository extends JpaRepository<SquadCheckIn, Long> {
}
