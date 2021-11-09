package no.noroff.hvz.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String gameState;

    @Column
    private String description;

    @Column(length = 20)
    private String nw_lat;

    @Column(length = 20)
    private String se_lat;

    @Column(length = 20)
    private String nw_long;

    @Column(length = 20)
    private String se_long;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "squad_id")
    private Set<Squad> squads;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "mission_id")
    private Set<Mission> missions;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "kill_id")
    private Set<Kill> kills;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "message_id")
    private Set<Message> messages;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "player_id")
    private Set<Player> players;

    public Game(String name, String gameState, String description, String nw_lat, String se_lat, String nw_long, String se_long, Date startDate, Date endDate) {
        this.name = name;
        this.gameState = gameState;
        this.description = description;
        this.nw_lat = nw_lat;
        this.se_lat = se_lat;
        this.nw_long = nw_long;
        this.se_long = se_long;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Game() {
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

    public Set<Squad> getSquads() {
        return squads;
    }

    public void setSquads(Set<Squad> squads) {
        this.squads = squads;
    }

    public Set<Mission> getMissions() {
        return missions;
    }

    public void setMissions(Set<Mission> missions) {
        this.missions = missions;
    }

    public Set<Kill> getKills() {
        return kills;
    }

    public void setKills(Set<Kill> kills) {
        this.kills = kills;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
