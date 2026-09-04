package soundwave.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soundwave.data.Artist;
import soundwave.data.SongInput;
import soundwave.data.User;

/**
 * A mocked implementation of the Model interface.
 */
public final class MockedModel implements Model {

    private static final int DEFAULT_BIRTH_YEAR = 1990;
    private static final int DEFAULT_BIRTH_MONTH = 5;
    private static final int DEFAULT_BIRTH_DAY = 10;
    private static final int DEFAULT_USER_POINTS = 10;
    private static final int DEFAULT_EPISODE_ID = 1;
    private static final int DEFAULT_PLAYLIST_ID = 1;

    private final List<User> users;
    private final List<String> savedPodcasts;
    private final Map<Integer, String> artists;
    private final Set<Integer> musicArtistIds;
    private final Set<Integer> podcastAuthorIds;
    private final Map<Integer, String> albums;

    /**
     * Constructs a new MockedModel with initial test data.
     */
    public MockedModel() {
        this.users = new ArrayList<>();
        this.savedPodcasts = new ArrayList<>();
        this.artists = new HashMap<>();
        this.musicArtistIds = new HashSet<>();
        this.podcastAuthorIds = new HashSet<>();
        this.albums = new HashMap<>();

        this.users.add(
            new User("mario88", "Mario", "Rossi", "mario@email.com", "pass123", 
                     LocalDate.of(DEFAULT_BIRTH_YEAR, DEFAULT_BIRTH_MONTH, DEFAULT_BIRTH_DAY), 
                     "Italia", DEFAULT_USER_POINTS)
        );

        this.artists.put(1, "Test Podcast Author");
        this.podcastAuthorIds.add(1);

        this.artists.put(2, "Test Music Artist");
        this.musicArtistIds.add(2);
    }

    @Override
    public int insertArtist(final String stageName, final String name, final String surname, 
                            final LocalDate birthDate, final String provenanceCountry, 
                            final String biography, final int startYear, final String artistType) {
        final int newId = this.artists.size() + 1;
        this.artists.put(newId, stageName);

        if ("Autore Podcast".equals(artistType)) {
            this.podcastAuthorIds.add(newId);
        } else {
            this.musicArtistIds.add(newId);
        }

        return newId;
    }

    @Override
    public int insertAlbumWithSongs(final int artistCode, final String title, final String releaseDate,
                                    final String recordCompany, final List<SongInput> songs) {
        final int newId = this.albums.size() + 1;
        this.albums.put(newId, title);
        return newId;
    }

    /**
     * Gets the list of artists eligible to publish albums.
     * 
     * @return a list of Artist objects.
     */
    @Override 
    public List<Artist> getAlbumArtists() {
        final List<Artist> albumArtists = new ArrayList<>();
        for (final Map.Entry<Integer, String> entry : this.artists.entrySet()) {
            if (this.musicArtistIds.contains(entry.getKey())) {
                albumArtists.add(new Artist(entry.getKey(), entry.getValue()));
            }
        }
        return albumArtists;
    }

    @Override
    public List<Artist> getPodcastAuthors() {
        final List<Artist> authors = new ArrayList<>();
        for (final Map.Entry<Integer, String> entry : this.artists.entrySet()) {
            if (this.podcastAuthorIds.contains(entry.getKey())) {
                authors.add(new Artist(entry.getKey(), entry.getValue()));
            }
        }
        return authors;
    }

    @Override
    public int insertPodcast(final int artistCode, final String name, final String description, final String category) {
        this.savedPodcasts.add(name);
        return this.savedPodcasts.size(); 
    }

    @Override
    public int insertEpisode(final int podcastCode, final String title, final int duration, final String description, 
                                final int episodeNumber) {
        return DEFAULT_EPISODE_ID;
    }

    @Override
    public int insertPlaylist(final String username, final String playlistName, final String visibility, 
                                final boolean isCollaborative) {
        return DEFAULT_PLAYLIST_ID;
    }

    @Override
    public void addTrackToPlaylist(final int playlistCode, final int trackCode) {
        // Simulazione in memoria
    }

    @Override
    public void insertListeningEvent(final String username, final int contentCode, final String device, 
                                     final int eventDuration) {
        // Simulazione in memoria
    }

    @Override
    public List<User> loadUsers() {
        return List.copyOf(this.users);
    }

    /**
     * Checks whether the specified artist is authorized as a podcast author.
     *
     * @param artistCode the unique code of the artist to check
     * @return true if the artist exists and is a podcast author, false otherwise
     */
    @Override
    public boolean isPodcastAuthor(final int artistCode) {
        return this.podcastAuthorIds.contains(artistCode);
    }

    @Override
    public String getMostPlayedArtist(final int year) {
        return "[MOCK] Artista: Test Artist (Ascolti: 150)";
    }

    @Override
    public String getMostPlayedGenre(final int year) {
        return "[MOCK] Genere: Rock (Ascolti: 500)";
    }

    @Override
    public List<String> getUsersAboveAverageListens(final int year) {
        return List.of("[MOCK] Utente: mario88 - Ascolti: 120");
    }

    @Override
    public List<String> getAlbumsAboveGlobalAverage() {
        return List.of("[MOCK] Album: Great Hits - Media Voti: 4.8");
    }
}
