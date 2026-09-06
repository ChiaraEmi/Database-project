package soundwave.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import soundwave.data.Album;
import soundwave.data.Album.DAO.AlbumWithSongs;
import soundwave.data.Artist;
import soundwave.data.SongInput;
import soundwave.data.User;

public final class MockedModel implements Model {

    // Una lista in memoria per simulare il database dei podcast
    private final List<String> savedPodcasts = new ArrayList<>();

    @Override
    public int insertArtist(final String stageName, final String name, final String surname, 
                            final LocalDate birthDate, final String provenanceCountry, 
                            final String biography, final int startYear, final String artistType) {
        System.out.println("[MOCK] Artist inserted: " + stageName);
        return 1;
    }

    @Override
    public int insertAlbumWithSongs(final int artistCode, final String title, final String releaseDate,
                                    final String recordCompany, final List<SongInput> songs) {
        System.out.println("[MOCK] Album inserted: " + title + " with " + songs.size() + " songs.");
        return 1;
    }

    @Override
    public int insertPodcast(final int artistCode, final String name, final String description, final String category) {
        // Simuliamo l'inserimento stampando un messaggio e restituendo un ID fittizio (es. 1)
        System.out.println("[MOCK] Podcast inserito: " + name + " (Artista: " + artistCode + ")");
        savedPodcasts.add(name);
        return 1; 
    }

    @Override
    public int insertEpisode(final int podcastCode, final String title, final int duration, final String description, 
                                final int episodeNumber) {
        return 1;
    }

    @Override
    public int insertPlaylist(final String username, final String playlistName, final String visibility, 
                                final boolean isCollaborative) {
        return 1;
    }

    @Override
    public void addTrackToPlaylist(final int playlistCode, final int trackCode) {
        // Simulazione vuota
    }

    @Override
    public void insertListeningEvent(final String username, final int contentCode, final String device, 
                                    final int eventDuration) {
        // Simulazione vuota
    }
    
    @Override
    public List<User> loadUsers() {
        return List.of(
            // Inserisci un utente di prova fittizio
            new User("mario88", "Mario", "Rossi", "mario@email.com", "pass123", 
                        LocalDate.of(1990, 5, 10), "Italia", 10)
        );
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
    public void removeLike(String username, int contentCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeLike'");
    }

    @Override
    public List<String> getLikedSongs(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLikedSongs'");
    }

    @Override
    public void addLike(String username, int contentCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addLike'");
    }

    @Override
    public List<String> getSongsByGenre(String genre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSongsByGenre'");
    }

    @Override
    public List<Artist> getArtistsByPartialName(String query) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArtistsByPartialName'");
    }

    @Override
    public Artist getArtistByCode(int artistCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArtistByCode'");
    }

    @Override
    public List<Album> getAlbumsByPartialTitle(String query) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAlbumsByPartialTitle'");
    }

    @Override
    public AlbumWithSongs getAlbumWithSongs(int albumCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAlbumWithSongs'");
    }

    @Override
    public List<String> getAlbumReviews(int albumCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAlbumReviews'");
    }

    @Override
    public void insertOrUpdateReview(String username, int albumCode, int rating, String comment) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertOrUpdateReview'");
    }

    @Override
    public void followArtist(String string, int artistCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'followArtist'");
    }
}
