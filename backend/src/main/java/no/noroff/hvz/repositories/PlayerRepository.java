package no.noroff.hvz.repositories;

import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player getPlayerByGameAndUser(Game game, AppUser user);
    boolean existsByGameAndUser(Game game, AppUser user);
    Player getPlayerByGameAndBiteCode(Game game, String biteCode);
    boolean existsByGameAndBiteCode(Game game, String biteCode);
    Player getPlayerByGame_IdAndBiteCode(long gameId, String biteCode);
}
