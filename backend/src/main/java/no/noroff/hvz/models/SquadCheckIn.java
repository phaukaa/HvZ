package no.noroff.hvz.models;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("unused")
@Entity
public class SquadCheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false, length = 20)
    private String lat;

    @Column(nullable = false, length = 20)
    private String lng;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private SquadMember member;

    public SquadCheckIn() {
    }

    public SquadCheckIn( Date time, String lat, String lng, SquadMember member) {
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

    public SquadMember getMember() {
        return member;
    }

    public void setMember(SquadMember member) {
        this.member = member;
    }
}
