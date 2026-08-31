package soundwave.data;

/**
 * Contains SQL query constants used by DAO classes.
 */
public final class Queries {

    // --- OP 9: INSERIMENTO PODCAST ---
    public static final String CHECK_ARTISTA_EXISTS = 
        """
        SELECT CodiceArtista, NomeDArte 
        FROM Artisti 
        WHERE CodiceArtista = ?
        """;

    public static final String INSERT_PODCAST = 
        """
        INSERT INTO Podcast (CodiceArtista, NomePodcast, DescrizionePodcast, Categoria)
        VALUES (?, ?, ?, ?)
        """;

    // --- OP 10: INSERIMENTO EPISODIO ---
    public static final String CHECK_PODCAST_EXISTS = 
        """
        SELECT CodicePodcast, NomePodcast FROM Podcast 
        WHERE CodicePodcast = ?
        """;

    public static final String INSERT_CONTENUTO_EPISODIO = 
        """
        INSERT INTO Contenuti (Titolo, Durata, Descrizione, DataPubblicazione, TipoContenuto)
        VALUES (?, ?, ?, CURRENT_DATE, 'Episodio')
        """;

    public static final String INSERT_EPISODIO = 
        """
        INSERT INTO Episodi (CodiceEpisodio, CodicePodcast, NumeroEpisodio)
        VALUES (?, ?, ?)
        """;

    // --- OP 11: GENERAZIONE EVENTO DI ASCOLTO ---
    public static final String CHECK_UTENTE_EXISTS = 
        """
        SELECT Username 
        FROM Utenti 
        WHERE Username = ?
        """;

    public static final String CHECK_CONTENUTO_EXISTS = 
        """
        SELECT CodiceContenuto 
        FROM Contenuti 
        WHERE CodiceContenuto = ?
        """;

    public static final String INSERT_EVENTO_ASCOLTO = 
        """
        INSERT INTO EventiAscolto (Username, CodiceContenuto, DataOra, Dispositivo, DurataEvento)
        VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?)
        """;

    // --- OP 12: CREAZIONE PLAYLIST ---
    public static final String INSERT_PLAYLIST = 
        """
        INSERT INTO Playlist (Username, NomePlaylist, DataCreazione, Visibilita, Collaborativa)
        VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?)
        """;

    // --- OP 13: AGGIUNTA / RIMOZIONE BRANO DA PLAYLIST ---
    public static final String CHECK_PERMESSI_PLAYLIST = 
        """
        SELECT P.CodicePlaylist 
        FROM Playlist P
        LEFT JOIN Collaborazioni C ON P.CodicePlaylist = C.CodicePlaylist
        WHERE P.CodicePlaylist = ? 
        AND (P.Username = ? OR C.Username = ?)
        """;

    public static final String ADD_BRANO_TO_PLAYLIST = 
        """
        INSERT INTO Inclusioni (CodicePlaylist, CodiceBrano)
        VALUES (?, ?)
        """;

    public static final String REMOVE_BRANO_FROM_PLAYLIST = 
        """
        DELETE FROM Inclusioni
        WHERE CodicePlaylist = ? 
        AND CodiceBrano = ?
        """;

    private Queries() { }
}
