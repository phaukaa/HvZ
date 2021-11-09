package no.noroff.hvz.dto.player;

@SuppressWarnings("unused")
public class PlayerDTOFull implements PlayerDTO {

    private Long id;
    private Boolean isHuman;
    private Boolean patientZero;
    private String biteCode;
    private Long gameID;

    public PlayerDTOFull(Long id, Boolean isHuman, Boolean patientZero, String biteCode, Long gameID) {
        this.id = id;
        this.isHuman = isHuman;
        this.patientZero = patientZero;
        this.biteCode = biteCode;
        this.gameID = gameID;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getHuman() {
        return isHuman;
    }

    public void setHuman(Boolean human) {
        isHuman = human;
    }

    public String getBiteCode() {
        return biteCode;
    }

    public void setBiteCode(String biteCode) {
        this.biteCode = biteCode;
    }


    public Boolean getPatientZero() {
        return patientZero;
    }

    public void setPatientZero(Boolean patientZero) {
        this.patientZero = patientZero;
    }

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

}
