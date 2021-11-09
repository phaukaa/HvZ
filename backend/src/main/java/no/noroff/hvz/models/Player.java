package no.noroff.hvz.models;

import javax.persistence.*;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isHuman;

    @Column(nullable = false, length = 30)
    private String biteCode;

    @Column(nullable = false)
    private boolean isPatientZero;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToMany(mappedBy ="killer",cascade = CascadeType.REMOVE)
    private Set<Kill> kills;

    @OneToOne(mappedBy = "player", cascade = CascadeType.REMOVE)
    private SquadMember membership;

    @OneToOne(mappedBy = "victim", cascade = CascadeType.REMOVE)
    private Kill death;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    public String getBiteCode() {
        return biteCode;
    }

    public void setBiteCode(String biteCode) {
        this.biteCode = biteCode;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Set<Kill> getKills() {
        return kills;
    }

    public void setKills(Set<Kill> kills) {
        this.kills = kills;
    }

    public boolean isPatientZero() {
        return isPatientZero;
    }

    public void setPatientZero(boolean patientZero) {
        isPatientZero = patientZero;
    }

    public SquadMember getMembership() {
        return membership;
    }

    public void setMembership(SquadMember membership) {
        this.membership = membership;
    }

    public Kill getDeath() {
        return death;
    }

    public void setDeath(Kill death) {
        this.death = death;
    }
}
