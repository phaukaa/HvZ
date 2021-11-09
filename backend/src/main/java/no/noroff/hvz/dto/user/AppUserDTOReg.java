package no.noroff.hvz.dto.user;

@SuppressWarnings("unused")
public class AppUserDTOReg {
    private String firstName;
    private String lastName;

    public AppUserDTOReg(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AppUserDTOReg() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
