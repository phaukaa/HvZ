package no.noroff.hvz.dto.kill;

import java.util.Date;

@SuppressWarnings("unused")
public class KillDTOReg {

    private Long killerID;
    private Date timeOfDeath;
    private String biteCode;
    private String story;
    private String lat;
    private String lng;

    public Date getTimeOfDeath() {
        return timeOfDeath;
    }

    public void setTimeOfDeath(Date timeOfDeath) {
        this.timeOfDeath = timeOfDeath;
    }

    public Long getKillerID() {
        return killerID;
    }

    public void setKillerID(Long killerID) {
        this.killerID = killerID;
    }

    public String getBiteCode() {
        return biteCode;
    }

    public void setBiteCode(String biteCode) {
        this.biteCode = biteCode;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
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
}
