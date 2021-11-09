package no.noroff.hvz.repositories;

import no.noroff.hvz.models.Player;
import no.noroff.hvz.models.SquadMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SquadMemberRepository extends JpaRepository<SquadMember, Long> {
    SquadMember getByPlayer(Player player);
    boolean existsBySquad_IdAndPlayer_Id(long squad_Id, long player_Id);
}
