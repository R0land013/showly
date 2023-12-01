package io.github.r0land013.showly.web.exception;

public class PortBeingUsedException extends RuntimeException{
    
    public PortBeingUsedException(int port) {
        super("Port " + port + " is already in use.");
    }

}
