package no.noroff.hvz.dto.game;

import java.util.Date;

@SuppressWarnings("unused")
public class GameDTOUpdate {

    private String name;
    private String gameState;
    private String description;
    private String nw_lat;
    private String se_lat;
    private String nw_long;
    private String se_long;
    private Date startDate;
    private Date endDate;

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
}
