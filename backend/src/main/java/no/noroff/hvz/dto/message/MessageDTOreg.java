package no.noroff.hvz.dto.message;

@SuppressWarnings("unused")
public class MessageDTOreg {

    private String message;
    private boolean faction;
    private boolean human;

    public boolean isHuman() {
        return human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFaction() {
        return faction;
    }

    public void setFaction(boolean faction) {
        this.faction = faction;
    }
}
