package no.noroff.hvz.dto.game;

import java.util.Date;

@SuppressWarnings("unused")
public class GameDTO {

    private Long id;
    private String name;
    private String gameState;
    private String description;
    private int playerAmount;
    private String nw_lat;
    private String se_lat;
    private String nw_long;
    private String se_long;
    private Date startDate;
    private Date endDate;

    public GameDTO(Long id, String name, String gameState, String description, int playerAmount, String nw_lat, String se_lat, String nw_long, String se_long, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.gameState = gameState;
        this.description = description;
        this.playerAmount = playerAmount;
        this.nw_lat = nw_lat;
        this.se_lat = se_lat;
        this.nw_long = nw_long;
        this.se_long = se_long;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNw_lat() {
        return nw_lat;
    }

    public void setNw_lat(String nw_lat) {
        this.nw_lat = nw_lat;
    }

    public String getSe_lat() {
        return se_lat;
    }

    public void setSe_lat(String se_lat) {
        this.se_lat = se_lat;
    }

    public String getNw_long() {
        return nw_long;
    }

    public void setNw_long(String nw_long) {
        this.nw_long = nw_long;
    }

    public String getSe_long() {
        return se_long;
    }

    public void setSe_long(String se_long) {
        this.se_long = se_long;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(int playerAmount) {
        this.playerAmount = playerAmount;
    }
}
