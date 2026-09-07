package soundwave.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import soundwave.data.Artist;
import soundwave.data.LikeBrani;
import soundwave.data.Playlist;
import soundwave.data.Podcast;
import soundwave.data.Plan;
import soundwave.data.SongInput;
import soundwave.data.User;

/**
 * A mocked implementation of the Model interface.
 */
public final class MockedModel implements Model {

    private static final String TEST_USERNAME = "mario88";

    private static final double PLAN_MONTHLY_COST = 9.99;
    private static final double PLAN_QUARTERLY_COST = 14.99;
    private static final int PLAN_QUARTERLY_DURATION = 3;
    private static final double PLAN_ANNUAL_COST = 89.99;
    private static final int PLAN_ANNUAL_DURATION = 12;
    private static final double PLAN_PREMIUM_COST = 129.99;
    private static final int PLAN_PREMIUM_DURATION = 12;
    private static final int DISCOUNT_PERCENTAGE = 20;
    private static final int MAX_LISTENING_TIME = 18500;
    private static final int MIN_LISTENING_TIME = 125;
    private static final int TRACK_COUNT_1 = 45;
    private static final int TRACK_COUNT_2 = 30;
    private static final int TRACK_COUNT_3 = 25;
    private static final int TRACK_COUNT_4 = 15;
    private static final int MAX_TOP_ITEMS = 5;
    private static final int ARTIST_COUNT_1 = 70;
    private static final int ARTIST_COUNT_2 = 50;
    private static final int ARTIST_COUNT_3 = 35;
    private static final int SUBSCRIPTION_DAYS_1 = 12;
    private static final int SUBSCRIPTION_DAYS_2 = 15;
    private static final int NOT_FOUND_CODE = 999;

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
    private final List<Podcast> podcasts;

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
        this.podcasts = new ArrayList<>();

        this.users.add(
            new User(TEST_USERNAME, "Mario", "Rossi", "mario@email.com", "pass123",
                     LocalDate.of(DEFAULT_BIRTH_YEAR, DEFAULT_BIRTH_MONTH, DEFAULT_BIRTH_DAY),
                     "Italia", DEFAULT_USER_POINTS)
        );

        this.artists.put(1, "Test Podcast Author");
        this.podcastAuthorIds.add(1);

        this.artists.put(2, "Test Music Artist");
        this.musicArtistIds.add(2);
    }

    @Override
    public User findUser(final String username) {
        if (username == null) {
            return null;
        }
        for (final User user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void insertPromotion(final String code, final String name, final String description,
                                 final LocalDate startDate, final LocalDate endDate, final String discountType,
                                 final double discountValue, final Integer requiredMonths, final List<Integer> planCodes) {
        System.out.println("[MOCK] Promotion inserted: " + name);
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
    public List<Podcast> getPodcasts() {
        return List.copyOf(this.podcasts);
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
    public List<Playlist> getUserPlaylists(final String username) {
        final List<Playlist> playlists = new ArrayList<>();
        // Esempio di popolamento mock con i parametri corretti:
        playlists.add(new Playlist(1, username, "I miei preferiti",
                                    "2026-01-01", "Pubblica", false));
        return playlists;
    }

    @Override
    public boolean addTrackToPlaylist(final String username, final int playlistCode, final int trackCode) {
        // Simulazione in memoria dell'aggiunta del brano
        return true;
    }

    @Override
    public boolean removeTrackFromPlaylist(final String username, final int playlistCode, final int trackCode) {
        // Simulazione in memoria della rimozione del brano
        return true;
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
     * @param artistCode the unique code of the artist to check.
     *
     * @return true if the artist exists and is a podcast author, false otherwise.
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

    @Override
    public List<LikeBrani> getLikedTracks(final String username) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getFollowedArtists(final String username) {
        return List.of("The Weeknd", "Dua Lipa", "Harry Styles");
    }

    @Override
    public void likeTrack(final String username, final int trackCode) {
        // Simulazione in memoria dell'aggiunta del like
    }

    @Override
    public boolean unlikeTrack(final String username, final int trackCode) {
        // Simulazione in memoria della rimozione del like
        return true;
    }

    @Override
    public List<Plan> getSubscriptioPlans() {
        return List.of(
            new Plan(1, "Mensile Standard", 1, PLAN_MONTHLY_COST),
            new Plan(2, "Mensile Premium", 1, PLAN_QUARTERLY_COST),
            new Plan(3, "Annuale Standard", PLAN_ANNUAL_DURATION, PLAN_ANNUAL_COST),
            new Plan(4, "Annuale Premium", PLAN_PREMIUM_DURATION, PLAN_PREMIUM_COST)
        );
    }

    @Override
    public int activateSubscription(final String username, final int planCode, final String paymentMethod,
                                    final String promoCode, final String inviteCode, final boolean autoRenew) {
        System.out.println("[MOCK] Sottoscrizione attivata per: " + username);
        System.out.println("[MOCK] Piano: " + planCode);
        System.out.println("[MOCK] Metodo: " + paymentMethod);
        return 1;
    }

    @Override
    public boolean verifyInviteCode(final String inviteCode) {
        return inviteCode.startsWith("INV_");
    }

    @Override
    public Object[] verifyPromotionCode(final String promoCode, final int planCode) {
        final List<String> validCodes = List.of("PROMO20", "WELCOME", "BLACKFRI", "STUDENT");

        if (promoCode != null && validCodes.contains(promoCode.toUpperCase(java.util.Locale.ROOT))) {
            return new Object[]{true, DISCOUNT_PERCENTAGE, "Percentuale", PLAN_ANNUAL_COST};
        }
        return new Object[]{false, 0.0, null, 0.0};
    }

    @Override
    public List<Object[]> getSubscriptionData(final String username) {
        final List<String> trans1 = List.of("2026-01-01 10:00:00 | €71.99 | Completata");
        final List<String> trans2 = List.of("2026-02-01 12:15:00 | €4.99 | Completata");

        return List.of(
            new Object[]{1, "Annuale Premium", "2026-01-01", "2027-01-01", "Attiva", true, "-", "INV_MARIO", trans1},
            new Object[]{2, "Mensile Standard", "2026-02-01", "2026-03-01", "Scaduta", false, "PROMO20", "-", trans2}
        );
    }

    @Override
    public Object[] getPersonalTotals(final String username, final int year) {
        // Ritorna [TotaleAscolti, TotaleSecondi] simulati
        return new Object[]{MIN_LISTENING_TIME, MAX_LISTENING_TIME};
    }

    @Override
    public List<Object[]> getPersonalTopTracks(final String username, final int year) {
        return List.of(
            new Object[]{1, "Blinding Lights", TRACK_COUNT_1},
            new Object[]{2, "Levitating", TRACK_COUNT_2},
            new Object[]{3, "Starboy", TRACK_COUNT_3},
            new Object[]{4, "Save Your Tears", TRACK_COUNT_4},
            new Object[]{MAX_TOP_ITEMS, "As It Was", 10}
        );
    }

    @Override
    public List<Object[]> getPersonalTopArtists(final String username, final int year) {
        return List.of(
            new Object[]{1, "The Weeknd", ARTIST_COUNT_1},
            new Object[]{2, "Dua Lipa", ARTIST_COUNT_2},
            new Object[]{3, "Harry Styles", ARTIST_COUNT_3}
        );
    }

    @Override
    public String getPersonalTopGenre(final String username, final int year) {
        return "Pop";
    }

    @Override
    public int getBonusCredits(final String username) {
        if (TEST_USERNAME.equals(username)) {
            return SUBSCRIPTION_DAYS_1;
        }

        if ("davide_99".equals(username)) {
            return SUBSCRIPTION_DAYS_2;
        }

        return 2;
    }

    @Override
    public int redeemBonus(final String username, final int planCode, final boolean autoRenew) {
        System.out.println("[MOCK] Riscatto bonus per: " + username);
        System.out.println("[MOCK] Piano: " + planCode);
        System.out.println("[MOCK] AutoRenew: " + autoRenew);
        return NOT_FOUND_CODE;
    }

    @Override
    public String registerUser(final String username, final String name, final String surname,
                            final String email, final String password,
                            final LocalDate birthDate, final String country) {
        System.out.println("[MOCK] Utente registrato: " + username);
        System.out.println("[MOCK] Email: " + email);
        System.out.println("[MOCK] Codice invito generato: INV_" + username.toUpperCase(Locale.ROOT));
        return "INV_" + username.toUpperCase(Locale.ROOT);
    }

    @Override
    public void renewSubscriptionNow(final String username, final int subscriptionCode) {
        System.out.println("[MOCK] Rinnovo manuale per: " + username + " - Sub #" + subscriptionCode);
    }

    @Override
    public void toggleAutoRenew(final String username, final int subscriptionCode,
                            final boolean enabled) {
        System.out.println("[MOCK] Rinnovo " + (enabled ? "attivato" : "disattivato")
                            + " per: " + username + " - Sub #" + subscriptionCode);
    }

    @Override
    public boolean getAutoRenewStatus(final String username, final int subscriptionCode) {
        // Simula: per mario88 ritorna true, per altri false
        if (TEST_USERNAME.equals(username)) {
            return true;
        }
        return false;
    }

    @Override
    public int[] processAutoRenewals() {
        System.out.println("[MOCK] Processo rinnovo automatico eseguito");
        return new int[]{1, 0, 0};  // mock: 1 rinnovata, 0 fallite, 0 scadute
    }
}
