package no.noroff.hvz.dto.squad;

import no.noroff.hvz.dto.player.PlayerDTO;

import java.util.Date;

@SuppressWarnings("unused")
public class SquadCheckInDTO {

    private Long id;
    private Date time;
    private String lat;
    private String lng;
    private PlayerDTO member;

    public SquadCheckInDTO(Long id, Date time, String lat, String lng, PlayerDTO member) {
        this.id = id;
        this.time = time;
        this.lat = lat;
        this.lng = lng;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public PlayerDTO getMember() {
        return member;
    }

    public void setMember(PlayerDTO member) {
        this.member = member;
    }
}
