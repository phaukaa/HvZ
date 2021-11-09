package no.noroff.hvz.repositories;

import no.noroff.hvz.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
