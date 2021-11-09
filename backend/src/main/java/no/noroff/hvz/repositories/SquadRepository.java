package no.noroff.hvz.repositories;

import no.noroff.hvz.models.Squad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SquadRepository extends JpaRepository<Squad, Long> {
}
