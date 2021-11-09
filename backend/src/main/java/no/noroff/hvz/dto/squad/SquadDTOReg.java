package no.noroff.hvz.dto.squad;

@SuppressWarnings("unused")
public class SquadDTOReg {

    private String name;
    private boolean human;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHuman() {
        return human;
    }

    public void setHuman(Boolean human) {
        this.human = human;
    }
}
