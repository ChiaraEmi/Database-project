USE soundwave;

-- Disattiva i controlli delle chiavi esterne per evitare errori di vincolo
SET FOREIGN_KEY_CHECKS = 0;

-- Svuota le tabelle partendo da quelle dipendenti (figlie)
TRUNCATE TABLE EventiAscolto;
TRUNCATE TABLE Transazioni;
TRUNCATE TABLE Sottoscrizioni;
TRUNCATE TABLE ValiditaPromozioni;
TRUNCATE TABLE CodiciInvito;
TRUNCATE TABLE Follow;
TRUNCATE TABLE LikeBrani;
TRUNCATE TABLE Recensioni;
TRUNCATE TABLE Inclusioni;
TRUNCATE TABLE Appartenenze;
TRUNCATE TABLE Cantare;
TRUNCATE TABLE Collaborazioni;
TRUNCATE TABLE Playlist;
TRUNCATE TABLE Brani;
TRUNCATE TABLE Album;
TRUNCATE TABLE Episodi;
TRUNCATE TABLE Podcast;
TRUNCATE TABLE Contenuti;
TRUNCATE TABLE Promozioni;
TRUNCATE TABLE Abbonamenti;
TRUNCATE TABLE Generi;
TRUNCATE TABLE Artisti;
TRUNCATE TABLE Utenti;

-- Riattiva i controlli delle chiavi esterne
SET FOREIGN_KEY_CHECKS = 1;

-- 1. UTENTI
INSERT INTO Utenti (Username, Nome, Cognome, Email, Password, DataNascita, Paese, CreditoBonus) VALUES
('mario88', 'Mario', 'Rossi', 'mario.rossi@email.com', 'hash_pass1', '1998-05-12', 'Italia', 10),
('luisa_g', 'Luisa', 'Gialli', 'luisa.gialli@email.com', 'hash_pass2', '2001-08-23', 'Italia', 0),
('giovanni_k', 'Giovanni', 'Verdi', 'giovanni.verdi@email.com', 'hash_pass3', '1995-11-03', 'Italia', 5),
('elena_v', 'Elena', 'Viola', 'elena.viola@email.com', 'hash_pass4', '2000-01-30', 'Spagna', 0),
('alex_smith', 'Alex', 'Smith', 'alex.smith@email.com', 'hash_pass5', '2001-08-23', 'Regno Unito', 0),
('claire_d', 'Claire', 'Dubois', 'claire.dubois@email.com', 'hash_pass6', '1995-11-03', 'Francia', 5);

-- 2. ARTISTI
INSERT INTO Artisti (CodiceArtista, NomeDArte, Nome, Cognome, DataNascita, PaeseProvenienza, Biografia, AnnoInizioAttivita, TipoArtista) VALUES
(1, 'Elodie', 'Elodie', 'Di Patrizi', '1990-05-03', 'Italia', 'Cantante pop italiana', 2015, 'Cantante'),
(2, 'Måneskin', NULL, NULL, NULL, 'Italia', 'Rock band di fama internazionale', 2016, 'Band'),
(3, 'Marco Mengoni', 'Marco', 'Mengoni', '1988-12-25', 'Italia', 'Vincitore di Sanremo', 2009, 'Cantante'),
(4, 'Gianluca Gazzoli', 'Gianluca', 'Gazzoli', '1988-08-18', 'Italia', 'Host del podcast BSMT', 2014, 'Autore Podcast'),
(5, 'The Weeknd', 'Abel', 'Tesfaye', '1990-02-16', 'Canada', 'Artista R&B e Pop di successo globale', 2010, 'Cantante'),
(6, 'Lex Fridman', 'Lex', 'Fridman', '1983-08-15', 'USA', 'Host del Lex Fridman Podcast', 2018, 'Autore Podcast');

-- 3. GENERI
INSERT INTO Generi (NomeGenere) VALUES
('Pop'),
('Rock'),
('Jazz'),
('Classica'),
('Indie'),
('Hip-Hop');

-- 4. ABBONAMENTI
INSERT INTO Abbonamenti (CodiceAbbonamento, TipoAbbonamento, Durata, Costo) VALUES
(1, 'Mensile Standard', 1, 9.99),
(2, 'Annuale Premium', 12, 89.99);

-- 5. PROMOZIONI
INSERT INTO Promozioni (CodicePromozione, Nome, Descrizione, DataInizioPromo, DataFinePromo, TipoSconto, ValoreSconto, MesiRichiesti) VALUES
(1, 'Promo Estate 2026', 'Sconto del 20% sul piano annuale', '2026-06-01', '2026-08-31', 'Percentuale', 20.00, 12),
(2, 'Benvenuto', 'Sconto fisso di 5 Euro', '2026-01-01', '2026-12-31', 'Fisso', 5.00, 1);

-- 6. CONTENUTI (Base per Brani ed Episodi)
INSERT INTO Contenuti (CodiceContenuto, Titolo, Durata, Descrizione, DataPubblicazione, TipoContenuto) VALUES
(1, 'OK. Respira', 165, 'Singolo pop ritmato', '2023-01-10', 'Brano'),
(2, 'Due', 180, 'Brano presentato a Sanremo', '2023-02-08', 'Brano'),
(3, 'ZITTI E BUONI', 194, 'Brano vincitore Eurovision 2021', '2021-03-03', 'Brano'),
(4, 'THE LONELIEST', 247, 'Ballad rock intensa', '2022-10-07', 'Brano'),
(5, 'Due Vite', 225, 'Canzone vincitrice Sanremo 2023', '2023-02-08', 'Brano'),
(6, 'Episodio 100 - Ospite Speciale', 3600, 'Intervista esclusiva', '2026-03-15', 'Episodio'),
(7, 'Episodio 101 - Aforismi e Musica', 2700, 'Chiacchierata informale', '2026-03-22', 'Episodio');

-- 7. PODCAST
INSERT INTO Podcast (CodicePodcast, CodiceArtista, NomePodcast, DescrizionePodcast, Categoria) VALUES
(1, 4, 'Passa dal BSMT', 'Interviste a personaggi del mondo dello spettacolo', 'Intrattenimento'),
(2, 6, 'Lex Fridman Podcast', 'Conversazioni su AI, scienza e tecnologia', 'Tecnologia');
-- 8. EPISODI
INSERT INTO Episodi (CodiceEpisodio, CodicePodcast, NumeroEpisodio) VALUES
(6, 1, 100),
(7, 2, 101);

-- 9. ALBUM
INSERT INTO Album (CodiceAlbum, CodiceArtista, TitoloAlbum, AnnoPubblicazione, CasaDiscografica, MediaVoti, DurataTotale) VALUES
(101, 1, 'OK. Respira', 2023, 'Island Records', 8.50, 345),
(102, 2, 'RUSH!', 2023, 'Epic Records', 9.10, 441),
(103, 3, 'Materia (Pelle)', 2022, 'Epic Records', 8.80, 225);

-- 10. BRANI
INSERT INTO Brani (CodiceBrano, CodiceAlbum, NumeroTraccia) VALUES
(1, 101, 1),
(2, 101, 2),
(3, 102, 1),
(4, 102, 2),
(5, 103, 1);

-- 11. PLAYLIST
INSERT INTO Playlist (CodicePlaylist, Username, NomePlaylist, DataCreazione, Visibilita, Collaborativa) VALUES
(1, 'mario88', 'Top Italian Pop', '2026-01-15 10:30:00', 'Pubblica', TRUE),
(2, 'luisa_g', 'Workout Rock', '2026-02-01 14:00:00', 'Privata', FALSE);

-- 12. COLLABORAZIONI
INSERT INTO Collaborazioni (CodicePlaylist, Username) VALUES
(1, 'luisa_g');

-- 13. CANTARE (Relazione N:M Artista - Brano)
INSERT INTO Cantare (CodiceArtista, CodiceBrano) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(3, 5);

-- 14. APPARTENENZE (Relazione N:M Brano - Genere)
INSERT INTO Appartenenze (CodiceBrano, NomeGenere) VALUES
(1, 'Pop'),
(2, 'Pop'),
(3, 'Rock'),
(4, 'Rock'),
(5, 'Pop');

-- 15. INCLUSIONE (Relazione N:M Brano - Playlist)
INSERT INTO Inclusioni (CodiceBrano, CodicePlaylist) VALUES
(1, 1),
(2, 1),
(5, 1),
(3, 2),
(4, 2);

-- 16. RECENSIONI
INSERT INTO Recensioni (Username, CodiceAlbum, Voto, Commento, DataRecensione) VALUES
('mario88', 101, 9, 'Album pop fantastico e moderno.', '2026-02-10'),
('luisa_g', 102, 10, 'Energia pura, il miglior album rock dell anno!', '2026-02-15');

-- 17. LIKE BRANI
INSERT INTO LikeBrani (Username, CodiceBrano) VALUES
('mario88', 1),
('mario88', 5),
('luisa_g', 3),
('giovanni_k', 4);

-- 18. FOLLOW (Fondamentale per testare OP 19)
INSERT INTO Follow (Username, CodiceArtista, DataInizio, DataFine) VALUES
('mario88', 1, '2026-01-01', NULL),
('luisa_g', 1, '2026-01-15', NULL),
('giovanni_k', 1, '2026-02-01', NULL),
('mario88', 2, '2026-01-01', NULL),
('elena_v', 2, '2026-02-20', NULL);

-- 19. CODICI INVITO
INSERT INTO CodiciInvito (Codice, DataGenerazione, Username) VALUES
('INVITE_MARIO', '2026-01-01', 'mario88'),
('INVITE_LUISA', '2026-01-15', 'luisa_g');

-- 20. VALIDITA PROMOZIONI
INSERT INTO ValiditaPromozioni (CodicePromozione, CodiceAbbonamento) VALUES
(1, 2),
(2, 1);

-- 21. SOTTOSCRIZIONI
INSERT INTO Sottoscrizioni (CodiceSottoscrizione, Username, CodiceAbbonamento, CodicePromozione, CodiceInvito, DataInizio, DataFine, Stato, RinnovoAutomatico) VALUES
(1, 'mario88', 2, 1, 'INVITE_MARIO', '2026-01-01', '2027-01-01', 'Attiva', TRUE);

-- 22. TRANSAZIONI
INSERT INTO Transazioni (CodiceTransazione, CodiceSottoscrizione, Data, Importo, MetodoPagamento, Stato) VALUES
(1, 1, '2026-01-01 10:00:00', 71.99, 'Carta di Credito', 'Completata');

-- 23. EVENTI ASCOLTO
INSERT INTO EventiAscolto (Username, CodiceContenuto, DataOra, Dispositivo, DurataEvento) VALUES
('mario88', 1, '2026-03-01 15:30:00', 'Smartphone Android', 165),
('mario88', 3, '2026-03-01 15:35:00', 'Smartphone Android', 194),
('luisa_g', 6, '2026-03-02 18:00:00', 'Desktop Windows', 3600);
