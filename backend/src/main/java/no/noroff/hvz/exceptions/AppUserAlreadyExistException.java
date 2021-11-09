package no.noroff.hvz.exceptions;

public class AppUserAlreadyExistException extends Exception{
    public AppUserAlreadyExistException() {
    }

    public AppUserAlreadyExistException(String message) {
        super(message);
    }
}
