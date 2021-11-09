package no.noroff.hvz.dto.player;

@SuppressWarnings("unused")
public class PlayerDTOStandard implements PlayerDTO {

    private Long id;
    private Boolean isHuman;
    private String biteCode;
    private String name;
    private Long gameID;

    public PlayerDTOStandard(Long id, Boolean isHuman, String biteCode, String name, Long gameID) {
        this.id = id;
        this.isHuman = isHuman;
        this.biteCode = biteCode;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }
}
