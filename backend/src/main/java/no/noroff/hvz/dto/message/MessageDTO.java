package no.noroff.hvz.dto.message;

import java.util.Date;

@SuppressWarnings("unused")
public class MessageDTO {
    private Long id;
    private String message;
    private Date messageTime;
    private String playerName;
    private boolean human;
    private boolean global;
    private boolean faction;

    public MessageDTO(Long id, String message, Date messageTime,  String playerName, boolean human, boolean global, boolean faction) {
        this.id = id;
        this.message = message;
        this.messageTime = messageTime;
        this.playerName = playerName;
        this.human = human;
        this.global = global;
        this.faction = faction;
    }

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

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isHuman() {
        return human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isFaction() {
        return faction;
    }

    public void setFaction(boolean faction) {
        this.faction = faction;
    }
}
