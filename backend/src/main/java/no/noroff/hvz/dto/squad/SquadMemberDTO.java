package no.noroff.hvz.dto.squad;

import no.noroff.hvz.dto.player.PlayerDTO;

@SuppressWarnings("unused")
public class SquadMemberDTO {

    private PlayerDTO player;
    private int rank;

    public SquadMemberDTO(PlayerDTO player, int rank) {
        this.player = player;
        this.rank = rank;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
