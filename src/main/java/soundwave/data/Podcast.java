package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Podcast entity.
 */
public final class Podcast {

    private final int code;
    private final int artistCode;
    private final String name;
    private final String description;
    private final String category;

    /**
     * Constructs a new Podcast instance.
     *
     * @param code the podcast code.
     * @param artistCode the artist code associated with the podcast.
     * @param name the name of the podcast.
     * @param description the description of the podcast.
     * @param category the category of the podcast.
     */
    public Podcast(final int code, final int artistCode, final String name, final String description, final String category) {
        this.code = code;
        this.artistCode = artistCode;
        this.name = name == null ? "" : name;
        this.description = description == null ? "" : description;
        this.category = category == null ? "" : category;
    }

    /**
     * Gets the podcast code.
     *
     * @return the code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the artist code.
     *
     * @return the artist code.
     */
    public int getArtistCode() {
        return artistCode;
    }

    /**
     * Gets the podcast name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the podcast description.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the podcast category.
     *
     * @return the category.
     */
    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Podcast) {
            final var p = (Podcast) other;
            return p.code == this.code
                   && p.artistCode == this.artistCode
                   && p.name.equals(this.name)
                   && p.description.equals(this.description)
                   && p.category.equals(this.category);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.artistCode, this.name, this.description, this.category);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Podcast",
            List.of(Printer.field("code", this.code),
                Printer.field("artistCode", this.artistCode),
                Printer.field("name", this.name),
                Printer.field("description", this.description),
                Printer.field("category", this.category)
            )
        );
    }

    /**
     * Data Access Object for Podcast operations.
     */
    public static final class DAO {

        private DAO() {

        }

        /**
         * Inserts a new podcast into the database.
         *
         * @param connection the database connection.
         * @param artistCode the code of the artist.
         * @param name the name of the podcast.
         * @param description the description of the podcast.
         * @param category the category of the podcast.
         * @return the auto-generated key of the inserted podcast.
         */
        public static int insert(final Connection connection, final int artistCode, final String name, 
                                final String description, final String category) {
            try (
                var statement = connection.prepareStatement(Queries.INSERT_PODCAST, java.sql.Statement.RETURN_GENERATED_KEYS)
            ) {
                statement.setObject(1, artistCode);
                statement.setObject(2, name);
                statement.setObject(3, description);
                statement.setObject(4, category);

                statement.executeUpdate();

                try (var resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            throw new DAOException("Unable to retrieve generated key for Podcast.");
        }
    }
}
