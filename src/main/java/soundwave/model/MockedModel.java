package soundwave.model;

import java.util.ArrayList;
import java.util.List;

public final class MockedModel implements Model {

    // Una lista in memoria per simulare il database dei podcast
    private final List<String> savedPodcasts = new ArrayList<>();

    @Override
    public int insertPodcast(final int artistCode, final String name, final String description, final String category) {
        // Simuliamo l'inserimento stampando un messaggio e restituendo un ID fittizio (es. 1)
        System.out.println("[MOCK] Podcast inserito: " + name + " (Artista: " + artistCode + ")");
        savedPodcasts.add(name);
        return 1; 
    }

    @Override
    public int insertEpisode(int podcastCode, String title, int duration, String description, int episodeNumber) {
        return 1;
    }

    @Override
    public int insertPlaylist(String username, String playlistName, String visibility, boolean isCollaborative) {
        return 1;
    }

    @Override
    public void addTrackToPlaylist(int playlistCode, int trackCode) {
        // Simulazione vuota
    }

    @Override
    public void insertListeningEvent(String username, int contentCode, String device, int eventDuration) {
        // Simulazione vuota
    }
}