package no.noroff.hvz.dto.player;

@SuppressWarnings("unused")
public class PlayerDTORegAdmin {

    private String userID;
    private boolean human;
    private boolean patientZero;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isHuman() {
        return human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public boolean isPatientZero() {
        return patientZero;
    }

    public void setPatientZero(boolean patientZero) {
        this.patientZero = patientZero;
    }
}
