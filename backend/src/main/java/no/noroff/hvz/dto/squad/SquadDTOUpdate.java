package no.noroff.hvz.dto.squad;

@SuppressWarnings("unused")
public class SquadDTOUpdate {
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

    public void setHuman(boolean human) {
        this.human = human;
    }
}
