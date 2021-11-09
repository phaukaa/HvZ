package no.noroff.hvz.models;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("unused")
@Entity
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private boolean isHuman;

    @Column(nullable = false)
    private String description;

    @Column
    private Date startTime;

    @Column
    private Date endTime;

    @Column
    private String lat;

    @Column
    private String lng;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Game game;

    public Mission(String name, boolean isHuman, String description, Date startTime, Date endTime, String lat, String lng, Game game) {
        this.name = name;
        this.isHuman = isHuman;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lat = lat;
        this.lng = lng;
        this.game = game;
    }

    public Mission(Long id, String name, boolean isHuman, String description, Date startTime, Date endTime, String lat, String lng, Game game) {
        this.id = id;
        this.name = name;
        this.isHuman = isHuman;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lat = lat;
        this.lng = lng;
        this.game = game;
    }

    public Mission(){}

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

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
