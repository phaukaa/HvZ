package no.noroff.hvz.dto.squad;

import java.util.List;

@SuppressWarnings("unused")
public class SquadDTO {

    private Long id;
    private String name;
    private List<SquadMemberDTO> members;
    private int numDead;

    public SquadDTO(Long id, String name, List<SquadMemberDTO> players, int numDead) {
        this.id = id;
        this.name = name;
        this.members = players;
        this.numDead = numDead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SquadMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<SquadMemberDTO> members) {
        this.members = members;
    }

    public int getNumDead() {
        return numDead;
    }

    public void setNumDead(int numDead) {
        this.numDead = numDead;
    }
}
