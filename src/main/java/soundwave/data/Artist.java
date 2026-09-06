package soundwave.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import soundwave.data.Album.DAO.AlbumWithSongs;

/**
 * Represents an Artist entity.
 */
public final class Artist {

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
     * Constructs a new Artist instance (full details).
     */
    public Artist(final int artistCode, final String stageName, final String name, final String surname, final LocalDate birthDate, 
                  final String country, final String biography, final int startYear, final String artistType) {
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
     * Constructs a new Artist instance with minimal info (for dropdowns/selectors).
     */
    public Artist(final int artistCode, final String stageName) {
        this.artistCode = artistCode;
        this.stageName = stageName == null ? "" : stageName;
        this.name = null;
        this.surname = null;
        this.birthDate = null;
        this.country = "";
        this.biography = null;
        this.startYear = 0;
        this.artistType = "";
    }

    public int getArtistCode() {
        return artistCode;
    }

    public String getStageName() {
        return stageName;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getCountry() {
        return country;
    }

    public String getBiography() {
        return biography;
    }

    public int getStartYear() {
        return startYear;
    }

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

    /**
     * Restituisce direttamente il nome d'arte affinché la JComboBox lo mostri correttamente.
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
         * Retrieves the most played artist in a specific year.
         */
        public static String getMostPlayedArtist(final Connection connection, final int year) {
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_MOST_PLAYED_ARTIST, year);
                var resultSet = statement.executeQuery()) {
                
                if (resultSet.next()) {
                    return "Artista: " + resultSet.getString("NomeDArte") + 
                        " (Ascolti: " + resultSet.getInt("NumeroAscolti") + ")";
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return "Nessun artista trovato per quest'anno.";
        }
    
        /**
         * Retrieves an artist's profile by their exact stage name.
         */
        public static Artist getByCode(final Connection connection, final int artistCode) {
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_ARTIST_BY_CODE, artistCode);
                 var resultSet = statement.executeQuery()) {
        
                if (resultSet.next()) {
                    return new Artist(
                        resultSet.getInt("CodiceArtista"),
                        resultSet.getString("NomeDArte"),
                        resultSet.getString("Nome"),
                        resultSet.getString("Cognome"),
                        resultSet.getDate("DataNascita") != null ? resultSet.getDate("DataNascita").toLocalDate() : null,
                        resultSet.getString("PaeseProvenienza"),
                        resultSet.getString("Biografia"),
                        resultSet.getInt("AnnoInizioAttivita"),
                        resultSet.getString("TipoArtista")
                    );
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return null;
        }
        

        /**
         * Retrieves a list of artists matching a partial stage name (per popolare la tendina).
         */
        public static List<Artist> getByPartialStageName(final Connection connection, final String query) {
            final List<Artist> artists = new java.util.ArrayList<>();
            final String searchPattern = "%" + (query != null ? query : "") + "%";
    
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_ARTISTS_BY_PARTIAL_NAME, searchPattern);
                 var resultSet = statement.executeQuery()) {
        
                while (resultSet.next()) {
                    artists.add(new Artist(
                        resultSet.getInt("CodiceArtista"),
                        resultSet.getString("NomeDArte")
                    ));
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return artists;
        }
        /**
         * Retrieves a list of albums matching a partial name using Queries.SELECT_ALBUMS_BY_NAME.
         */
        public static List<Album> getByPartialTitle(final Connection connection, final String query) {
            final List<Album> albums = new ArrayList<>();
            final String searchPattern = "%" + (query != null ? query : "") + "%";

            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_ALBUMS_BY_NAME, searchPattern);
                 var resultSet = statement.executeQuery()) {
                
                while (resultSet.next()) {
                    albums.add(new Album(
                        resultSet.getInt("CodiceAlbum"),
                        resultSet.getInt("CodiceArtista"),
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