package no.noroff.hvz.dto.user;

import no.noroff.hvz.dto.player.PlayerDTOFull;
import java.util.Set;

public class AppUserDTOFull {
    private String openID;
    private String firstName;
    private String lastName;
    private Set<PlayerDTOFull> players;
    private boolean isAdmin;

    public AppUserDTOFull(String openID, String firstName, String lastName, Set<PlayerDTOFull> players, boolean isAdmin) {
        this.openID = openID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.players = players;
        this.isAdmin = isAdmin;
    }

    @SuppressWarnings("unused")
    public String getOpenID() {
        return openID;
    }

    @SuppressWarnings("unused")
    public void setOpenID(String openID) {
        this.openID = openID;
    }

    @SuppressWarnings("unused")
    public String getFirstName() {
        return firstName;
    }

    @SuppressWarnings("unused")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @SuppressWarnings("unused")
    public String getLastName() {
        return lastName;
    }

    @SuppressWarnings("unused")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @SuppressWarnings("unused")
    public Set<PlayerDTOFull> getPlayers() {
        return players;
    }

    @SuppressWarnings("unused")
    public void setPlayers(Set<PlayerDTOFull> players) {
        this.players = players;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
