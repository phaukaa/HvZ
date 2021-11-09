package no.noroff.hvz.models;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("unused")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean isHuman;

    @Column(nullable = false)
    private boolean isGlobal;

    @Column(nullable = false)
    private boolean isFaction;

    @Column
    private Date chatTime;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "squad_message_id")
    private Squad squad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public Date getChatTime() {
        return chatTime;
    }

    public void setChatTime(Date chatTime) {
        this.chatTime = chatTime;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public boolean isFaction() {
        return isFaction;
    }

    public void setFaction(boolean faction) {
        isFaction = faction;
    }
}
