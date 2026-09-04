package soundwave.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an Artist entity.
 */
public final class Artist {
    private static final String NOME_ARTE_LITERAL = "NomeDArte";

    private final int artistCode;
    private final String stageName;
    private final String name;
    private final String surname;
    private final LocalDate birthDate;
    private final String country;
    private final String biography;
    private final int startYear;
    private final String artistType;

    /**
     * Constructs a new Artist instance.
     *
     * @param artistCode the artist code.
     * @param stageName the stage name.
     * @param name the real name.
     * @param surname the surname.
     * @param birthDate the birth date.
     * @param country the country of origin.
     * @param biography the biography.
     * @param startYear the start year of activity.
     * @param artistType the type of artist.
     */
    public Artist(final int artistCode, final String stageName, final String name, final String surname, 
                    final LocalDate birthDate, final String country, final String biography, 
                    final int startYear, final String artistType) {
        this.artistCode = artistCode;
        this.stageName = stageName == null ? "" : stageName;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.country = country == null ? "" : country;
        this.biography = biography;
        this.startYear = startYear;
        this.artistType = artistType == null ? "" : artistType;
    }

    /**
     * Constructs a lightweight Artist instance for dropdown or summary selections.
     *
     * @param artistCode the artist code.
     * @param stageName the stage name.
     */
    public Artist(final int artistCode, final String stageName) {
        this(artistCode, stageName, null, null, null, "", null,
             0, "Autore Podcast");
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
     * Gets the stage name.
     *
     * @return the stage name.
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * Gets the real name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the surname.
     *
     * @return the surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Gets the birth date.
     *
     * @return the birth date.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Gets the country of origin.
     *
     * @return the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the biography.
     *
     * @return the biography.
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Gets the start year of activity.
     *
     * @return the start year.
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * Gets the type of artist.
     *
     * @return the artist type.
     */
    public String getArtistType() {
        return artistType;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Artist) {
            final var a = (Artist) other;
            return a.artistCode == this.artistCode
                    && a.stageName.equals(this.stageName)
                    && Objects.equals(a.name, this.name)
                    && Objects.equals(a.surname, this.surname)
                    && Objects.equals(a.birthDate, this.birthDate)
                    && a.country.equals(this.country)
                    && Objects.equals(a.biography, this.biography)
                    && a.startYear == this.startYear
                    && a.artistType.equals(this.artistType);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.artistCode, this.stageName, this.name, this.surname, 
                            this.birthDate, this.country, this.biography, this.startYear, this.artistType);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Artist",
            List.of(
                Printer.field("artistCode", this.artistCode),
                Printer.field("stageName", this.stageName),
                Printer.field("name", this.name),
                Printer.field("surname", this.surname),
                Printer.field("birthDate", this.birthDate),
                Printer.field("country", this.country),
                Printer.field("biography", this.biography),
                Printer.field("startYear", this.startYear),
                Printer.field("artistType", this.artistType)
            )
        );
    }

    /**
     * Data Access Object for Artist operations.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Inserts a new artist into the database.
         *
         * @param connection the database connection.
         * @param stageName the stage name of the artist.
         * @param name the real name.
         * @param surname the surname.
         * @param birthDate the birth date.
         * @param country the country of origin.
         * @param biography the biography.
         * @param startYear the start year of activity.
         * @param artistType the type of artist.
         * @return the auto-generated key of the inserted artist.
         */
        public static int insert(final Connection connection, final String stageName, final String name,
                                final String surname, final LocalDate birthDate, final String country,
                                final String biography, final int startYear, final String artistType) {
            final Object sqlBirthDate = birthDate != null ? Date.valueOf(birthDate) : null;

            try (
                var statement = DAOUtils.prepareWithKeys(
                    connection, 
                    Queries.INSERT_ARTIST, 
                    java.sql.Statement.RETURN_GENERATED_KEYS, 
                    stageName, name, surname, sqlBirthDate, country, biography, startYear, artistType
                );
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
            throw new DAOException("Unable to retrieve generated key for Artist.");
        }

        /**
         * Retrieves all artists eligible to publish albums.
         *
         * @param connection the database connection.
         * 
         * @return a list of eligible artists.
         * 
         * @throws DAOException if a database access error occurs.
         */
        public static List<Artist> getAlbumArtists(final Connection connection) {
            final List<Artist> artists = new ArrayList<>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.SELECT_ALBUM_ARTISTS);
                var resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    artists.add(new Artist(
                        resultSet.getInt("CodiceArtista"),
                        resultSet.getString(NOME_ARTE_LITERAL)
                    ));
                }
                return artists;
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * Retrieves all artists authorized as podcast authors.
         *
         * @param connection the database connection.
         * 
         * @return a list of artists who are podcast authors.
         * 
         * @throws DAOException if a database access error occurs.
         */
        public static List<Artist> getPodcastAuthors(final Connection connection) {
            final List<Artist> authors = new ArrayList<>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.SELECT_PODCAST_AUTHORS);
                var resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    authors.add(new Artist(
                        resultSet.getInt("CodiceArtista"),
                        resultSet.getString(NOME_ARTE_LITERAL)
                    ));
                }
                return authors;
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * Checks whether the specified artist is authorized as a podcast author.
         *
         * @param connection the database connection.
         * @param artistCode the unique code of the artist to check.
         * 
         * @return true if the artist exists and is a podcast author, false otherwise.
         * 
         * @throws DAOException if a database access error occurs.
         */
        public static boolean isPodcastAuthor(final Connection connection, final int artistCode) {
            try (var statement = DAOUtils.prepare(connection, Queries.CHECK_IS_PODCAST_AUTHOR, artistCode);
                var resultSet = statement.executeQuery()) {
                return resultSet.next();
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * Retrieves the most played artist in a specific year.
         *
         * @param connection the database connection.
         * @param year the year to check.
         * 
         * @return a string representation of the most played artist.
         */
        public static String getMostPlayedArtist(final Connection connection, final int year) {
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_MOST_PLAYED_ARTIST, year);
                var resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return "Artista: " + resultSet.getString(NOME_ARTE_LITERAL)
                            + " (Ascolti: " + resultSet.getInt("NumeroAscolti") + ")";
                }

            } catch (final SQLException e) {
                throw new DAOException(e);
            }

            return "Nessun artista trovato per quest'anno.";
        }
    }
}
