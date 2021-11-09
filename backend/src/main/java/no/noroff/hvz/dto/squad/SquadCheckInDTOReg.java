package no.noroff.hvz.dto.squad;

@SuppressWarnings("unused")
public class SquadCheckInDTOReg {

    private String lat;
    private String lng;
    private Long playerID;

    public SquadCheckInDTOReg( String lat, String lng, Long playerID) {

        this.lat = lat;
        this.lng = lng;
        this.playerID = playerID;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Long getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Long playerID) {
        this.playerID = playerID;
    }
}
