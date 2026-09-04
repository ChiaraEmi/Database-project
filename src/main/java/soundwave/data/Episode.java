package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Represents an Episode entity.
 */
public final class Episode {

    private final int episodeCode;
    private final int podcastCode;
    private final int episodeNumber;

    /**
     * Constructs a new Episode instance.
     *
     * @param code the episode code (matches Contenuto code).
     * @param podcastCode the code of the podcast it belongs to.
     * @param episodeNumber the episode number within the podcast.
     */
    public Episode(final int code, final int podcastCode, final int episodeNumber) {
        this.episodeCode = code;
        this.podcastCode = podcastCode;
        this.episodeNumber = episodeNumber;
    }

    /**
     * Gets the episode code.
     *
     * @return the code.
     */
    public int getEpisodeCode() {
        return episodeCode;
    }

    /**
     * Gets the podcast code.
     *
     * @return the podcast code.
     */
    public int getPodcastCode() {
        return podcastCode;
    }

    /**
     * Gets the episode number.
     *
     * @return the episode number.
     */
    public int getEpisodeNumber() {
        return episodeNumber;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Episode) {
            final var e = (Episode) other;
            return e.episodeCode == this.episodeCode
                   && e.podcastCode == this.podcastCode
                   && e.episodeNumber == this.episodeNumber;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.episodeCode, this.podcastCode, this.episodeNumber);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Episode",
            List.of(
                Printer.field("code", this.episodeCode),
                Printer.field("podcastCode", this.podcastCode),
                Printer.field("episodeNumber", this.episodeNumber)
            )
        );
    }

    /**
     * Data Access Object for Episode operations.
     */
    public static final class DAO {

        private DAO() {

        }

        /**
         * Inserts a new podcast episode into the database.
         * First creates the parent record in Contenuti via Content.DAO.insert,
         * then links it to the Podcast in Episodi.
         *
         * @param connection the database connection.
         * @param podcastCode the foreign key referring to Podcast.
         * @param title the title of the episode.
         * @param duration the duration in seconds.
         * @param description the description of the episode.
         * @param episodeNumber the episode number within the podcast.
         * @return the generated episode code.
         */
        public static int insert(final Connection connection, final int podcastCode, 
                                 final String title, final int duration, 
                                 final String description, final int episodeNumber) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                try (var checkStmt = DAOUtils.prepare(connection, Queries.CHECK_PODCAST_EXISTS, podcastCode);
                     var resultSet = checkStmt.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new DAOException("Podcast with ID " + podcastCode + " does not exist.");
                    }
                }

                final int contentCode = Content.DAO.insert(connection, title, duration, description, "Episodio");

                try (var statement = DAOUtils.prepare(connection, Queries.INSERT_EPISODIO, contentCode, 
                                                    podcastCode, episodeNumber)) {
                    statement.executeUpdate();
                }

                connection.commit();
                return contentCode;
            } catch (final SQLException e) {
                try {
                    connection.rollback();
                } catch (final SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
                throw new DAOException(e);
            } finally {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (final SQLException ignored) {
                    // Intentionally ignored: primary exception was already thrown in the main catch block
                }
            }
        }
    }
}
