package soundwave.data;

/**
 * Contains SQL query constants used by DAO classes.
 */
public final class Queries {

    // --- OP 7: INSERIMENTO ARTISTA ---
    public static final String INSERT_ARTIST = 
        """
        INSERT INTO Artisti (NomeDArte, Nome, Cognome, DataNascita, PaeseProvenienza, Biografia, AnnoInizioAttivita, TipoArtista)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

    // --- OP 8: INSERIMENTO ALBUM E RELATIVI BRANI ---
    public static final String SELECT_ALBUM_ARTISTS = 
        """
        SELECT CodiceArtista, NomeDArte 
        FROM Artisti 
        WHERE TipoArtista != 'Autore Podcast'
        ORDER BY NomeDArte ASC
        """;

    public static final String INSERT_ALBUM = 
        """
        INSERT INTO Album (CodiceArtista, TitoloAlbum, DataPubblicazione, CasaDiscografica)
        VALUES (?, ?, ?, ?)
        """;

    // Riutilizziamo o creiamo la query per i contenuti di tipo 'Brano'
    public static final String INSERT_CONTENUTO_BRANO = 
        """
        INSERT INTO Contenuti (Titolo, Durata, Descrizione, DataPubblicazione, TipoContenuto)
        VALUES (?, ?, ?, ?, 'Brano')
        """;

    public static final String INSERT_BRANO = 
        """
        INSERT INTO Brani (CodiceBrano, CodiceAlbum, NumeroTraccia)
        VALUES (?, ?, ?)
        """;

    public static final String UPDATE_ALBUM_DURATION = 
        """
        UPDATE Album 
        SET DurataTotale = (
            SELECT SUM(C.Durata) 
            FROM Brani B 
            JOIN Contenuti C ON B.CodiceBrano = C.CodiceContenuto 
            WHERE B.CodiceAlbum = ?
        )
        WHERE CodiceAlbum = ?
        """;

    public static final String INSERT_CANTARE = 
        """
        INSERT INTO Cantare (CodiceArtista, CodiceBrano)
        VALUES (?, ?)
        """;

    public static final String INSERT_APPARTENENZA = 
        """
        INSERT INTO Appartenenze (CodiceBrano, NomeGenere)
        VALUES (?, ?)
        """;

    public static final String INSERT_GENRE_IF_NOT_EXISTS = 
        """
        INSERT IGNORE INTO Generi (NomeGenere)
        VALUES (?)
        """;

    // --- OP 9: INSERIMENTO PODCAST ---
    public static final String SELECT_PODCAST_AUTHORS = 
        """
        SELECT CodiceArtista, NomeDArte 
        FROM Artisti 
        WHERE TipoArtista = 'Autore Podcast'
        ORDER BY NomeDArte ASC
        """;

    public static final String INSERT_PODCAST = 
        """
        INSERT INTO Podcast (CodiceArtista, NomePodcast, DescrizionePodcast, Categoria)
        VALUES (?, ?, ?, ?)
        """;

    public static final String CHECK_IS_PODCAST_AUTHOR = 
        """
        SELECT CodiceArtista 
        FROM Artisti 
        WHERE CodiceArtista = ? AND TipoArtista = 'Autore Podcast'
        """;

    // --- OP 10: INSERIMENTO EPISODIO ---
    public static final String CHECK_PODCAST_EXISTS = 
        """
        SELECT CodicePodcast, NomePodcast FROM Podcast 
        WHERE CodicePodcast = ?
        """;

    public static final String INSERT_CONTENUTO = 
        """
        INSERT INTO Contenuti (Titolo, Durata, Descrizione, DataPubblicazione, TipoContenuto)
        VALUES (?, ?, ?, ?, ?)
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
        VALUES (?, ?, CURRENT_DATE, ?, ?)
        """;

    // --- OP 13: AGGIUNTA / RIMOZIONE BRANO DA PLAYLIST ---
    public static final String SELECT_PLAYLISTS_BY_USER = 
        """
        SELECT DISTINCT p.CodicePlaylist, p.Username, p.NomePlaylist, p.DataCreazione, p.Visibilita, p.Collaborativa 
        FROM Playlist p
        LEFT JOIN Collaborazioni c ON p.CodicePlaylist = c.CodicePlaylist
        WHERE p.Username = ? OR (p.Collaborativa = TRUE AND c.Username = ?)
        ORDER BY p.NomePlaylist ASC
        """;

    public static final String CHECK_PERMESSI_PLAYLIST = 
        """
        SELECT P.CodicePlaylist 
        FROM Playlist P
        LEFT JOIN Collaborazioni C ON P.CodicePlaylist = C.CodicePlaylist
        WHERE P.CodicePlaylist = ? 
        AND (P.Username = ? OR C.Username = ?)
        """;

    public static final String CHECK_DUPLICATE_TRACK_IN_PLAYLIST = 
        """
        SELECT 1 
        FROM Inclusioni 
        WHERE CodicePlaylist = ? AND CodiceBrano = ?
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

    // --- OPS 22 ---
    public static final String SELECT_MOST_PLAYED_SONG = 
    """
    SELECT B.CodiceBrano, C.Titolo, COUNT(*) AS NumeroAscolti
    FROM EventiAscolto E
    JOIN Brani B ON E.CodiceContenuto = B.CodiceBrano
    JOIN Contenuti C ON B.CodiceBrano = C.CodiceContenuto 
    WHERE YEAR(E.DataOra) = ?
    GROUP BY B.CodiceBrano, C.Titolo
    ORDER BY NumeroAscolti DESC
    LIMIT 1
    """;

    public static final String SELECT_MOST_PLAYED_ARTIST = 
    """
    SELECT A.CodiceArtista, A.NomeDArte, COUNT(*) AS NumeroAscolti
    FROM (
        SELECT E.DataOra, C.CodiceArtista
        FROM EventiAscolto E
        JOIN Brani B ON E.CodiceContenuto = B.CodiceBrano
        JOIN Cantare C ON B.CodiceBrano = C.CodiceBrano

        UNION ALL

        SELECT E.DataOra, P.CodiceArtista
        FROM EventiAscolto E
        JOIN Episodi EP ON E.CodiceContenuto = EP.CodiceEpisodio
        JOIN Podcast P ON EP.CodicePodcast = P.CodicePodcast
    ) AS AscoltiArtista
    JOIN Artisti A ON AscoltiArtista.CodiceArtista = A.CodiceArtista
    WHERE YEAR(AscoltiArtista.DataOra) = ?
    GROUP BY A.CodiceArtista, A.NomeDArte
    ORDER BY NumeroAscolti DESC
    LIMIT 1
    """;

    public static final String SELECT_MOST_PLAYED_GENRE = 
    """
    SELECT A.NomeGenere, COUNT(*) AS NumeroAscolti
    FROM EventiAscolto E
    JOIN Brani B ON E.CodiceContenuto = B.CodiceBrano
    JOIN Appartenenze A ON B.CodiceBrano = A.CodiceBrano
    WHERE YEAR(E.DataOra) = ? 
    GROUP BY A.NomeGenere
    ORDER BY NumeroAscolti DESC
    LIMIT 1
    """;

    public static final String SELECT_USERS_ABOVE_AVG_LISTENS = 
    """
    SELECT E.Username, COUNT(*) AS NumeroAscolti
    FROM EventiAscolto E
    WHERE YEAR(E.DataOra) = ? 
    GROUP BY E.Username
    HAVING COUNT(*) > (
        SELECT AVG(TotaleAscolti)
        FROM (SELECT COUNT(*) AS TotaleAscolti
    FROM EventiAscolto
    WHERE YEAR(DataOra) = ? 
    GROUP BY Username ) AS AscoltiPerUtente
    )
    """;

    public static final String SELECT_ALBUMS_ABOVE_GLOBAL_AVG_RATING = 
    """
    SELECT A.CodiceAlbum, A.TitoloAlbum, A.MediaVoti
    FROM ALBUM A
    WHERE A.MediaVoti > (
        SELECT AVG(MediaVoti)
        FROM ALBUM)
    """;

    public static final String SELECT_ALL_GENRES = 
    """
    SELECT NomeGenere 
    FROM Generi
    ORDER BY NomeGenere ASC
    """;

    private Queries() { }
}
