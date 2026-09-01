package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Represents a ListeningEvent entity corresponding to the EventiAscolto table.
 */
public final class ListeningEvent {

    private final String username;
    private final int contentCode;
    private final String timestamp;
    private final String device;
    private final int eventDuration;

    /**
     * Constructs a new ListeningEvent instance.
     *
     * @param username the username of the listening user (Username).
     * @param contentCode the content code listened to (CodiceContenuto).
     * @param timestamp the date and time of the event (DataOra).
     * @param device the device used for listening (Dispositivo).
     * @param eventDuration the duration played in seconds (DurataEvento).
     */
    public ListeningEvent(final String username, final int contentCode, final String timestamp, 
                          final String device, final int eventDuration) {
        this.username = username == null ? "" : username;
        this.contentCode = contentCode;
        this.timestamp = timestamp == null ? "" : timestamp;
        this.device = device == null ? "" : device;
        this.eventDuration = eventDuration;
    }

    /**
     * Gets the username (Username).
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the content code (CodiceContenuto).
     *
     * @return the content code.
     */
    public int getContentCode() {
        return contentCode;
    }

    /**
     * Gets the timestamp (DataOra).
     *
     * @return the timestamp string.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the device (Dispositivo).
     *
     * @return the device name.
     */
    public String getDevice() {
        return device;
    }

    /**
     * Gets the event duration in seconds (DurataEvento).
     *
     * @return the duration in seconds.
     */
    public int getEventDuration() {
        return eventDuration;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof ListeningEvent) {
            final var e = (ListeningEvent) other;
            return e.contentCode == this.contentCode
                   && e.eventDuration == this.eventDuration
                   && e.username.equals(this.username)
                   && e.timestamp.equals(this.timestamp)
                   && e.device.equals(this.device);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username, this.contentCode, this.timestamp, this.device, this.eventDuration);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "ListeningEvent",
            List.of(
                Printer.field("username", this.username),
                Printer.field("contentCode", this.contentCode),
                Printer.field("timestamp", this.timestamp),
                Printer.field("device", this.device),
                Printer.field("eventDuration", this.eventDuration)
            )
        );
    }

    /**
     * Data Access Object for ListeningEvent operations.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Inserts a new listening event into the EventiAscolto table (OP 11).
         *
         * @param connection the database connection.
         * @param username the foreign key referring to Utenti (Username).
         * @param contentCode the foreign key referring to Contenuti (CodiceContenuto).
         * @param device the device used for listening (Dispositivo).
         * @param eventDuration the duration played in seconds (DurataEvento).
         */
        public static void insert(final Connection connection, final String username, 
                                  final int contentCode, final String device, 
                                  final int eventDuration) {
            try (
                var statement = DAOUtils.prepare(
                    connection, 
                    Queries.INSERT_EVENTO_ASCOLTO, 
                    username, contentCode, device, eventDuration
                )
            ) {
                statement.executeUpdate();
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }
    }
}
