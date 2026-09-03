package soundwave.data;

/**
 * Contains SQL query constants used by DAO classes.
 */
public final class Queries {

    // --- OP 1: REGISTRAZIONE NUOVO ---
    public static final String CHECK_USERNAME_EXISTS = 
        """
        SELECT Username
        FROM Utenti
        WHERE Username= ?
        """;
        
    public static final String INSERT_USER = 
        """
        INSERT INTO Utenti (Username, Email, Password, Nome, Cognome, DataNascita, Paese, CreditiBonus)
        VALUES (?, ?, ?, ?, ?, ?, ?, 0)
        """;

    public static final String INSERT_INVITECODE = 
        """
        INSERT INTO CodiciInvito (Codice, Username)
        VALUES (?, ?)
        """;

    // --- OP 2: ATTIVAZIONE DI UNA SOTTOSCRIZIONE ---

    // --- 2.1: ATTIVAZIONE DI UNA SOTTOSCRIZIONE IN MODO STANDARD ---
    public static final String CHECK_ACTIVE_SUBSCRIPTION = 
        """
        SELECT CodiceSottoscrizione
        FROM Sottoscrizioni
        WHERE Username= ? AND Stato = 'Attiva'
        """;

    public static final String INSERT_SUBSCRIPTION_STANDARD = 
        """
        INSERT INTO Sottoscrizioni (Username, CodiceAbbonamento, CodicePromozione, CodiceInvito, DataInizio, DataFine, Stato, RinnovoAutomatico) 
        SELECT ?, A.CodiceAbbonamento, NULL, NULL, CURRENT_DATE, CURRENT_DATE + INTERVAL A.Durata MONTH, 'Attiva', ?
        FROM Abbonamenti A
        WHERE A.CodiceAbbonamento= ?;
        """;

    public static final String INSERT_TRANSACTION_STANDARD = 
        """
        INSERT INTO Transazioni (CodiceSottoscrizione, Importo, MetodoPagamento, Stato)
        SELECT ?, A.Costo, ?, 'Completata'
        FROM Abbonamenti A
        WHERE A.CodiceAbbonamento= ?;
        """;
    
    // --- 2.2: ATTIVAZIONE DI UNA SOTTOSCRIZIONE CON CODICE PROMOZIONALE ---
    public static final String CHECK_PROMOTION_VALIDITY = 
        """
        SELECT P.CodicePromozione, P.TipoSconto, P.ValoreSconto, P.MesiRichiesti, A.Costo, A.Durata
        FROM Promozioni P 
        JOIN ValiditaPromozioni V ON V.CodicePromozione = P.CodicePromozione  
        JOIN Abbonamenti A ON A.CodiceAbbonamento = V.CodiceAbbonamento
        WHERE P.CodicePromozione=? 
        AND A.CodiceAbbonamento=? 
        AND CURRENT_DATE BETWEEN P.DataInizioPromo AND P.DataFinePromo
        AND (P.MesiRichiesti IS NULL OR A.Durata >= P.MesiRichiesti)
        """;

    public static final String INSERT_SUBSCRIPTION_PROMOTIONAL = 
        """
        INSERT INTO Sottoscrizioni (Username, CodiceAbbonamento, CodicePromozione, CodiceInvito, DataInizio, DataFine, Stato, RinnovoAutomatico) 
        SELECT ?, A.CodiceAbbonamento, P.CodicePromozione, NULL, CURRENT_DATE, CURRENT_DATE + INTERVAL A.Durata MONTH, 'Attivo', ?
        FROM Abbonamenti A
        JOIN ValiditaPromozioni V ON  V.CodiceAbbonamento = A.CodiceAbbonamento 
        JOIN Promozioni P ON P.CodicePromozione = V.CodicePromozione
        WHERE P.CodicePromozione = ? 
        AND A.CodiceAbbonamento = ? 
        AND CURRENT_DATE BETWEEN P.DataInizioPromo AND P.DataFinePromo
        """;

    public static final String INSERT_TRANSACTION_PROMOTIONAL = 
        """
        INSERT INTO Transazioni (CodiceSottoscrizione, Importo, MetodoPagamento, Stato)
        VALUES (?, ?, ?, 'Completata')
        """;

    // ---  2.3: ATTIVAZIONE DI UNA SOTTOSCRIZIONE CON CODICE INVITO ---
    public static final String CHECK_IS_FIRST_SUBSCRIPTION = 
        """
        SELECT CodiceSottoscrizione
        FROM Sottoscrizioni
        WHERE Username= ?
        """;

    public static final String CHECK_INVITECODE = 
        """
        SELECT Codice, DataGenerazione, Username
        FROM CodiciInvito
        WHERE Codice = ?
        """;

    public static final String INSERT_SUBSCRIPTION_INVITE = 
        """
        INSERT INTO Sottoscrizioni (Username, CodiceAbbonamento, CodicePromozione, CodiceInvito, DataInizio, DataFine, Stato, RinnovoAutomatico) 
        SELECT ?, A.CodiceAbbonamento, NULL, CI.Codice, CURRENT_DATE, CURRENT_DATE + INTERVAL A.Durata MONTH, 'Attivo', ?
        FROM CodiciInvito CI JOIN Abbonamenti A 
        WHERE CI.Codice=? 
        AND A.CodiceAbbonamento=?;
        """;

    public static final String INSERT_TRANSACTION_INVITE = 
        """
        INSERT INTO Transazioni ( CodiceSottoscrizione, Importo, MetodoPagamento, Stato)
        SELECT  ?, A.Costo*0.80, ?, 'Completata'
        FROM Abbonamenti A
        WHERE A.CodiceAbbonamento= ?;
        """;

    public static final String UPDATE_BONUS_CREDIT = 
        """
        UPDATE Utenti
        SET CreditoBonus = CreditoBonus + 2
        WHERE Username = ?;
        """;

    // --- OP 3: RINNOVO AUTOMATICO E ANNULAMENTO DELLA SOTTOSCRIZIONE ---
    
    // ---  3.1: RINNOVO AUTOMATICO ---
    public static final String CHECK_SUBSCRIPTION_RENEWAL = 
        """
        SELECT CodiceSottoscrizione, CodiceAbbonamento, DataFine, RinnovoAutomatico
        FROM Sottoscrizioni
        WHERE Username= ? AND Stato = 'Attiva' AND RinnovoAutomatico = TRUE
        """;

    public static final String RENEW_SUBSCRIPTION = 
        """
        UPDATE Sottoscrizioni S
        SET DataFine = DataFine + INTERVAL ( SELECT Durata
                                            FROM Abbonamenti A
                                            WHERE A.CodiceAbbonamento = S.CodiceAbbonamento
        ) MONTH
        WHERE CodiceSottoscrizione = ?;
        """;
       
    public static final String INSERT_RENEWAL_TRANSACTION = 
        """
        INSERT INTO Transazioni (CodiceSottoscrizione, Importo, MetodoPagamento, Stato)
        SELECT ?, A.Costo, ?, ?
        FROM Sottoscrizioni S 
        JOIN Abbonamenti A ON  A.CodiceAbbonamento = S.CodiceAbbonamento
        WHERE S.CodiceSottoscrizione = ?
        """;

    // ---  3.2: DISATTIVAZIONE DEL RINNOVO ---
    public static final String CANCEL_RENEWAL = 
        """
        UPDATE Sottoscrizioni
        SET RinnovoAutomatico = FALSE
        WHERE CodiceSottoscrizione = ? AND Stato = 'Attiva'
        """;

    public static final String EXPIRE_SUBSCRIPTION = 
        """
        UPDATE Sottoscrizioni
        SET Stato = 'Scaduta'
        WHERE CodiceSottoscrizione = ? AND DataFine < CURRENT_DATE
        """;


    // --- OP 4: RISCATTO CON CREDITI BONUS ---
    public static final String CHECK_BONUS_CREDIT = 
        """
        SELECT Username, CreditoBonus
        FROM Utenti
        WHERE Username= ? AND CreditoBonus >= 2
        """;
    
    public static final String CHECK_MONTHLY_SUBSCRIPTION = 
        """
        SELECT CodiceAbbonamento, Durata
        FROM Abbonamenti
        WHERE CodiceAbbonamento= ? AND Durata = 1
        """;

    public static final String DEBIT_BONUS_CREDIT =
        """
        UPDATE Utenti
        SET CreditoBonus = CreditoBonus - 2
        WHERE Username = ? AND CreditoBonus >= 2;
        """;

    // ---  4.1: RISCATTO PER NUOVA SOTTOSCRIZIONE ---
    public static final String INSERT_SUBSCRIPTION_BONUS_CREDIT = 
        """
        INSERT INTO Sottoscrizioni (Username, CodiceAbbonamento, CodicePromozione, CodiceInvito, DataInizio, DataFine, Stato, RinnovoAutomatico) 
        VALUES (?, ?, NULL, NULL, CURRENT_DATE, CURRENT_DATE + INTERVAL 1 MONTH, 'Attiva', ?);
        """;

    public static final String INSERT_TRANSACTION_BONUS_CREDIT = 
        """
        INSERT INTO Transazioni ( CodiceSottoscrizione, Importo, MetodoPagamento, Stato)
        VALUES ( ?,0, 'Crediti Bonus', 'Completata')
        """;

    // ---  4.1: RISCATTO PER NUOVA SOTTOSCRIZIONE ---
    public static final String CHECK_ACTIVE_MONTHLY_SUBSCRIPTION =
        """
        SELECT S.CodiceSottoscrizione, S.DataFine, A.Durata
        FROM Sottoscrizioni S 
        JOIN Abbonamenti A ON A.CodiceAbbonamento = S.CodiceAbbonamento
        WHERE S.CodiceSottoscrizione = ? AND S.Username=? AND S.Stato= 'Attiva' AND A. Durata = 1
        """;

    public static final String EXTEND_SUBSCRIPTION_BONUS_CREDIT = 
        """
        UPDATE Sottoscrizioni
        SET DataFine = DataFine + INTERVAL 1 MONTH
        WHERE CodiceSottoscrizione = ? AND Username=? AND Stato= 'Attiva'
        """;

    // --- OP 5: VISUALIZZIONE SOTTOSCRIZIONI E TRANSAZIONI DI UN UTENTE ---
    public static final String SELECT_SUBSCRIPTIONS_WITH_TRANSACTIONS =
        """
        SELECT S.CodiceSottoscrizione, A.TipoAbbonamento AS TipoAbbonamento, S.DataInizio, S.DataFine, S.Stato AS StatoSottoscrizione, 
                S.CodicePromozione, S.CodiceInvito, S.RinnovoAutomatico, T.CodiceTransazione, T.Data AS DataTransazione, T.Importo, 
                T.MetodoPagamento, T.Stato AS StatoTransazione
        FROM Sottoscrizioni S
        JOIN Abbonamenti A ON  A.CodiceAbbonamento = S.CodiceAbbonamento 
        LEFT JOIN Transazioni T ON S.CodiceSottoscrizione = T.CodiceSottoscrizione
        WHERE S.Username=? 
        ORDER BY T.Data DESC;
        """;

    // --- OP 6: INSERIMENTO DI UNA NUOVA CAMPAGNA PROMOZIONALE ---
    public static final String INSERT_PROMOTIONAL_CAMPAIGN = 
        """
        INSERT INTO Promozioni (Nome, Descrizione, DataInizioPromo, DataFinePromo, TipoSconto, ValoreSconto, MesiRichiesti)
        VALUES ( ?, ?, ?, ?, ?, ?, ?)
        """;

    public static final String INSERT_PROMOTIONAL_VALIDITY = 
        """
        INSERT INTO ValiditaPromozioni (CodicePromozione, CodiceAbbonamento)
        VALUES (?, ?)
        """;

    // --- OP 7: INSERIMENTO ARTISTA ---
    public static final String INSERT_ARTIST = 
        """
        INSERT INTO Artisti (NomeDArte, Nome, Cognome, DataNascita, PaeseProvenienza, Biografia, AnnoInizioAttivita, TipoArtista)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

    // --- OP 8: INSERIMENTO ALBUM E RELATIVI BRANI ---
    public static final String INSERT_ALBUM = 
        """
        INSERT INTO Album (CodiceArtista, TitoloAlbum, AnnoPubblicazione, CasaDiscografica)
        VALUES (?, ?, ?, ?)
        """;

    // Riutilizziamo o creiamo la query per i contenuti di tipo 'Brano'
    public static final String INSERT_CONTENUTO_BRANO = 
        """
        INSERT INTO Contenuti (Titolo, Durata, Descrizione, DataPubblicazione, TipoContenuto)
        VALUES (?, ?, ?, CURRENT_DATE, 'Brano')
        """;

    public static final String INSERT_BRANO = 
        """
        INSERT INTO Brani (CodiceBrano, CodiceAlbum, NumeroTraccia)
        VALUES (?, ?, ?)
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

    // --- OP 9: INSERIMENTO PODCAST ---
    public static final String CHECK_ARTIST_EXISTS = 
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

    public static final String INSERT_CONTENUTO = 
        """
        INSERT INTO Contenuti (Titolo, Durata, Descrizione, DataPubblicazione, TipoContenuto)
        VALUES (?, ?, ?, CURRENT_DATE, ?)
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
