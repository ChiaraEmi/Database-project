package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Content (Contenuto) entity.
 */
public final class Content {

    private final int code;
    private final String title;
    private final int duration;
    private final String description;
    private final String publicationDate;
    private final String contentType;

    /**
     * Constructs a new Content instance.
     *
     * @param code the content code.
     * @param title the title of the content.
     * @param duration the duration in seconds.
     * @param description the description of the content.
     * @param publicationDate the publication date.
     * @param contentType the type of content ('Brano' or 'Episodio').
     */
    public Content(final int code, final String title, final int duration, 
                   final String description, final String publicationDate, final String contentType) {
        this.code = code;
        this.title = title == null ? "" : title;
        this.duration = duration;
        this.description = description == null ? "" : description;
        this.publicationDate = publicationDate == null ? "" : publicationDate;
        this.contentType = contentType == null ? "" : contentType;
    }

    /**
     * Gets the content code.
     *
     * @return the code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the content title.
     *
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the content duration in seconds.
     *
     * @return the duration.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Gets the content description.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the publication date.
     *
     * @return the publication date.
     */
    public String getPublicationDate() {
        return publicationDate;
    }

    /**
     * Gets the content type ('Brano' or 'Episodio').
     *
     * @return the content type.
     */
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Content) {
            final var c = (Content) other;
            return c.code == this.code
                   && c.duration == this.duration
                   && c.title.equals(this.title)
                   && c.description.equals(this.description)
                   && c.publicationDate.equals(this.publicationDate)
                   && c.contentType.equals(this.contentType);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.title, this.duration, this.description, this.publicationDate, this.contentType);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Content",
            List.of(
                Printer.field("code", this.code),
                Printer.field("title", this.title),
                Printer.field("duration", this.duration),
                Printer.field("description", this.description),
                Printer.field("publicationDate", this.publicationDate),
                Printer.field("contentType", this.contentType)
            )
        );
    }

    /**
     * Data Access Object for Content operations.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Inserts a new generic record into the Contenuti table.
         *
         * @param connection the database connection.
         * @param title the content title.
         * @param duration the duration in seconds.
         * @param description the content description.
         * @param contentType the type of content ('Brano' or 'Episodio').
         * @return the auto-generated key of the inserted content.
         */
        public static int insert(final Connection connection, final String title, 
                                 final int duration, final String description, 
                                 final String contentType) {
            try (
                var statement = DAOUtils.prepareWithKeys(
                    connection, 
                    Queries.INSERT_CONTENUTO, 
                    java.sql.Statement.RETURN_GENERATED_KEYS, 
                    title, duration, description, contentType
                )
            ) {
                statement.executeUpdate();

                try (var resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            throw new DAOException("Unable to retrieve generated key for Content.");
        }
    }
}
