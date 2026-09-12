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
    private static final String ARTIST_CODE = "CodiceArtista";
    private static final String PERCENT_SIGN = "%";

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
        this(artistCode, stageName, null, null, null, "", null, 0, "Autore Podcast");
    }

    /**
     * Returns the artist code.
     * 
     * @return the artist code.
     */
    public int getArtistCode() {
        return artistCode;
    }

    /**
     * Returns the stage name.
     * 
     * @return the stage name.
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * Returns the real name.
     * 
     * @return the real name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the surname.
     * 
     * @return the surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Returns the birth date.
     * 
     * @return the birth date.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Returns the country of origin.
     * 
     * @return the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the biography.
     * 
     * @return the biography.
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Returns the start year of activity.
     * 
     * @return the start year.
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * Returns the type of artist.
     * 
     * @return the artist type.
     */
    public String getArtistType() {
        return artistType;
    }

    /**
     * Compares this artist with another object for equality.
     * 
     * @param other the object to compare with.
     * 
     * @return true if equal, false otherwise.
     */
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

    /**
     * Returns the hash code value for this artist.
     * 
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.artistCode, this.stageName, this.name, this.surname, 
                            this.birthDate, this.country, this.biography, this.startYear, this.artistType);
    }

    /**
     * Returns the stage name directly so that JComboBox can display it correctly in dropdowns.
     * 
     * @return the stage name string.
     */
    @Override
    public String toString() {
        return this.stageName != null ? this.stageName : "";
    }

    /**
     * Data Access Object for Artist operations.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Inserts a new artist into the database (OP 7).
         *
         * @param connection the database connection.
         * @param stageName the stage name.
         * @param name the real name.
         * @param surname the surname.
         * @param birthDate the birth date.
         * @param country the country of origin.
         * @param biography the biography.
         * @param startYear the start year of activity.
         * @param artistType the type of artist.
         * 
         * @return the generated artist code.
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
         * @return a list of album artists.
         */
        public static List<Artist> getAlbumArtists(final Connection connection) {
            final List<Artist> artists = new ArrayList<>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.SELECT_ALBUM_ARTISTS);
                var resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    artists.add(new Artist(
                        resultSet.getInt(ARTIST_CODE),
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
         * @return a list of podcast authors.
         */
        public static List<Artist> getPodcastAuthors(final Connection connection) {
            final List<Artist> authors = new ArrayList<>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.SELECT_PODCAST_AUTHORS);
                var resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    authors.add(new Artist(
                        resultSet.getInt(ARTIST_CODE),
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
         * @param artistCode the artist code.
         * 
         * @return true if the artist is a podcast author, false otherwise.
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
         * @param year the year.
         * 
         * @return a string representing the most played artist and their play count, 
         *         or a default message if none found.
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

        /**
         * Retrieves an artist's profile by their exact code.
         *
         * @param connection the database connection.
         * @param artistCode the artist code.
         * 
         * @return the Artist instance, or null if not found.
         */
        public static Artist getByCode(final Connection connection, final int artistCode) {
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_ARTIST_BY_CODE, artistCode);
                 var resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new Artist(
                        resultSet.getInt(ARTIST_CODE),
                        resultSet.getString(NOME_ARTE_LITERAL),
                        resultSet.getString("Nome"),
                        resultSet.getString("Cognome"),
                        resultSet.getDate("DataNascita") != null 
                        ? resultSet.getDate("DataNascita").toLocalDate() : null,
                        resultSet.getString("PaeseProvenienza"),
                        resultSet.getString("Biografia"),
                        resultSet.getInt("AnnoInizioAttivita"),
                        resultSet.getString("TipoArtista")
                    );
                }
            } catch (final SQLException e) {
                e.printStackTrace();
                throw new DAOException(e);
            }
            return null;
        }

        /**
         * Retrieves a list of artists matching a partial stage name.
         *
         * @param connection the database connection.
         * @param query the partial stage name query string.
         * 
         * @return a list of matching artists.
         */
        public static List<Artist> getByPartialStageName(final Connection connection, final String query) {
            final List<Artist> artists = new ArrayList<>();
            final String searchPattern = PERCENT_SIGN + (query != null ? query : "") + PERCENT_SIGN;

            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_ARTISTS_BY_PARTIAL_NAME, searchPattern);
                 var resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    artists.add(new Artist(
                        resultSet.getInt(ARTIST_CODE),
                        resultSet.getString(NOME_ARTE_LITERAL)
                    ));
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return artists;
        }

        /**
         * Retrieves a list of albums matching a partial name.
         *
         * @param connection the database connection.
         * @param query the partial album title query string.
         * 
         * @return a list of matching albums.
         */
        public static List<Album> getByPartialTitle(final Connection connection, final String query) {
            final List<Album> albums = new ArrayList<>();
            final String searchPattern = PERCENT_SIGN + (query != null ? query : "") + PERCENT_SIGN;

            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_ALBUMS_BY_NAME, searchPattern);
                 var resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    albums.add(new Album(
                        resultSet.getInt("CodiceAlbum"),
                        resultSet.getInt(ARTIST_CODE),
                        resultSet.getString("TitoloAlbum"),
                        resultSet.getString("AnnoPubblicazione"),
                        resultSet.getString("CasaDiscografica"),
                        resultSet.getDouble("MediaVoti"),
                        resultSet.getInt("DurataTotale")
                    ));
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return albums;
        }
    }
}
