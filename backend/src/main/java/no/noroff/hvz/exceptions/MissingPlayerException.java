package no.noroff.hvz.exceptions;

public class MissingPlayerException extends Error{
    public MissingPlayerException() {
    }

    public MissingPlayerException(String message) {
        super(message);
    }
}
