package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Genre entity.
 */
public final class Genre {

    private final String genreName;

    /**
     * Constructs a new Genre instance.
     *
     * @param genreName the name of the genre.
     */
    public Genre(final String genreName) {
        this.genreName = genreName == null ? "" : genreName;
    }

    /**
     * Gets the genre name.
     *
     * @return the genre name.
     */
    public String getGenreName() {
        return genreName;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Genre) {
            final var g = (Genre) other;
            return g.genreName.equals(this.genreName);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.genreName);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Genre",
            List.of(Printer.field("genreName", this.genreName))
        );
    }

    /**
     * Data Access Object for Genre operations.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Retrieves the most played music genre in a specific year.
         *
         * @param connection the database connection.
         * @param year the year to check.
         * @return a string representation of the most played genre.
         */
        public static String getMostPlayedGenre(final Connection connection, final int year) {
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_MOST_PLAYED_GENRE, year);
                 var resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return "Genere: " + resultSet.getString("NomeGenere") 
                            + " (Ascolti: " + resultSet.getInt("NumeroAscolti") + ")";
                }

            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return "Nessun genere trovato per quest'anno.";
        }
    }
}
