package fr.neatmonster.agaria.events.socket;

public class SocketErrorEvent extends SocketEvent {
    public final Throwable cause;

    public SocketErrorEvent(final Throwable cause) {
        this.cause = cause;
    }
}
