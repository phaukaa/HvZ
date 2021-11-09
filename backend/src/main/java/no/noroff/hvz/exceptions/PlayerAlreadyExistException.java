package no.noroff.hvz.exceptions;

public class PlayerAlreadyExistException extends Error {
    public PlayerAlreadyExistException() {
    }

    public PlayerAlreadyExistException(String message) {
        super(message);
    }
}
