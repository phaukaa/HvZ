package no.noroff.hvz.repositories;

import no.noroff.hvz.models.Kill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KillerRepository extends JpaRepository<Kill, Long> {
    List<Kill> getKillsByGame_IdAndKiller_Id(long gameId, long killerId);
    List<Kill> getKillsByGame_Id(long gameId);
}
