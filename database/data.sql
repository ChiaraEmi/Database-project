/*USE soundwave;

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
('claire_d', 'Claire', 'Dubois', 'claire.dubois@email.com', 'hash_pass6', '1995-11-03', 'Francia', 5),
('davide_99', 'Davide', 'Neri', 'davide.neri@email.com', 'hash_pass7', '1999-04-14', 'Italia', 15),
('sofia_b', 'Sofia', 'Conti', 'sofia.conti@email.com', 'hash_pass8', '2002-12-05', 'Italia', 0),
('marco_p', 'Marco', 'Piras', 'marco.piras@email.com', 'hash_pass9', '1997-06-19', 'Italia', 0),
('federica_m', 'Federica', 'Mancini', 'federica.mancini@email.com', 'hash_pass10', '2003-02-11', 'Italia', 10),
('luca_t', 'Luca', 'Trevisan', 'luca.trevisan@email.com', 'hash_pass11', '1994-09-25', 'Italia', 0),
('martina_r', 'Martina', 'Riva', 'martina.riva@email.com', 'hash_pass12', '2000-10-04', 'Italia', 5);

-- 2. ARTISTI
INSERT INTO Artisti (CodiceArtista, NomeDArte, Nome, Cognome, DataNascita, PaeseProvenienza, Biografia, AnnoInizioAttivita, TipoArtista) VALUES
-- Cantanti e Band (1-15)
(1, 'Elodie', 'Elodie', 'Di Patrizi', '1990-05-03', 'Italia', 'Cantante pop italiana', 2015, 'Cantante'),
(2, 'Måneskin', NULL, NULL, NULL, 'Italia', 'Rock band di fama internazionale', 2016, 'Band'),
(3, 'Marco Mengoni', 'Marco', 'Mengoni', '1988-12-25', 'Italia', 'Vincitore di Sanremo', 2009, 'Cantante'),
(4, 'Cesare Cremonini', 'Cesare', 'Cremonini', '1980-03-27', 'Italia', 'Cantautore storico italiano', 1999, 'Cantante'),
(5, 'The Weeknd', 'Abel', 'Tesfaye', '1990-02-16', 'Canada', 'Artista R&B e Pop di successo globale', 2010, 'Cantante'),
(6, 'Dua Lipa', 'Dua', 'Lipa', '1995-08-22', 'Regno Unito', 'Pop star internazionale', 2015, 'Cantante'),
(7, 'Pinguini Tattici Nucleari', NULL, NULL, NULL, 'Italia', 'Band pop-rock italiana', 2010, 'Band'),
(8, 'Fabri Fibra', 'Fabrizio', 'Tarducci', '1976-10-17', 'Italia', 'Pioniere del rap italiano', 1996, 'Cantante'),
(9, 'Blanco', 'Riccardo', 'Fabbriconi', '2003-02-10', 'Italia', 'Cantautore rivelazione pop-rap', 2021, 'Cantante'),
(10, 'Madame', 'Francesca', 'Calearo', '2002-01-16', 'Italia', 'Cantautrice urban e pop', 2018, 'Cantante'),
(11, 'Colapesce Dimartino', NULL, NULL, NULL, 'Italia', 'Duo indie-pop italiano', 2020, 'Band'),
(12, 'Negramaro', NULL, NULL, NULL, 'Italia', 'Band rock-pop italiana', 2001, 'Band'),
(13, 'Laura Pausini', 'Laura', 'Pausini', '1974-05-16', 'Italia', 'Icona della musica leggera italiana nel mondo', 1993, 'Cantante'),
(14, 'Tiziano Ferro', 'Tiziano', 'Ferro', '1979-02-21', 'Italia', 'Cantautore pop di fama internazionale', 2001, 'Cantante'),
(15, 'Salmo', 'Maurizio', 'Pisciottu', '1984-09-29', 'Italia', 'Rapper e produttore discografico', 2009, 'Cantante'),
-- Autori di Podcast (16-20)
(16, 'Gianluca Gazzoli', 'Gianluca', 'Gazzoli', '1988-08-18', 'Italia', 'Host del podcast BSMT', 2014, 'Autore Podcast'),
(17, 'Lex Fridman', 'Lex', 'Fridman', '1983-08-15', 'USA', 'Host del Lex Fridman Podcast', 2018, 'Autore Podcast'),
(18, 'Pablo Trincia', 'Pablo', 'Trincia', '1977-09-27', 'Italia', 'Giornalista e autore di podcast storici', 2005, 'Autore Podcast'),
(19, 'Stefano Nazzi', 'Stefano', 'Nazzi', '1961-10-22', 'Italia', 'Giornalista e autore di cronaca nera', 2015, 'Autore Podcast'),
(20, 'Guglielmo Scilla', 'Guglielmo', 'Scilla', '1987-11-26', 'Italia', 'Creator e podcaster', 2009, 'Autore Podcast');

-- 3. GENERI
INSERT INTO Generi (NomeGenere) VALUES
('Pop'),
('Rock'),
('Jazz'),
('Classica'),
('Indie'),
('Hip-Hop'),
('R&B'),
('Elettronica'),
('Rap');

-- 4. ABBONAMENTI
INSERT INTO Abbonamenti (CodiceAbbonamento, TipoAbbonamento, Durata, Costo) VALUES
(1, 'Mensile Standard', 1, 9.99),
(2, 'Annuale Premium', 12, 89.99),
(3, 'Famiglia Mensile', 1, 14.99),
(4, 'Student Mensile', 1, 4.99);

-- 5. PROMOZIONI
INSERT INTO Promozioni (CodicePromozione, Nome, Descrizione, DataInizioPromo, DataFinePromo, TipoSconto, ValoreSconto, MesiRichiesti) VALUES
('PROMO20', 'Promo Estate 2026', 'Sconto del 20% sul piano annuale', '2026-06-01', '2026-08-31', 'Percentuale', 20.00, 12),
('WELCOME', 'Benvenuto', 'Sconto fisso di 5 Euro', '2026-01-01', '2026-12-31', 'Fisso', 5.00, 1),
('BLACKFRI', 'Black Friday', 'Sconto del 50% sul piano annuale', '2026-11-20', '2026-11-30', 'Percentuale', 50.00, 12),
('STUDENT', 'Student Promo', 'Sconto fisso speciale studenti', '2026-01-01', '2026-12-31', 'Fisso', 2.00, 1);

-- 6. CONTENUTI (Brani ed Episodi)
INSERT INTO Contenuti (CodiceContenuto, Titolo, Durata, Descrizione, DataPubblicazione, TipoContenuto) VALUES
-- Brani esistenti
(1, 'OK. Respira', 165, 'Singolo pop ritmato', '2023-01-10', 'Brano'),
(2, 'Due', 180, 'Brano presentato a Sanremo', '2023-02-08', 'Brano'),
(3, 'ZITTI E BUONI', 194, 'Brano vincitore Eurovision 2021', '2021-03-03', 'Brano'),
(4, 'THE LONELIEST', 247, 'Ballad rock intensa', '2022-10-07', 'Brano'),
(5, 'Due Vite', 225, 'Canzone vincitrice Sanremo 2023', '2023-02-08', 'Brano'),
(8, 'Logico', 255, 'Hit estiva d autore', '2014-04-11', 'Brano'),
(9, 'Levitating', 203, 'Brano dance-pop di grande successo', '2020-03-27', 'Brano'),
(10, 'Blinding Lights', 200, 'Hit mondiale synth-pop', '2019-11-29', 'Brano'),
(11, 'Nostalgica', 190, 'Singolo ritmato pop-dance', '2022-06-10', 'Brano'),
(13, 'Ringo Starr', 185, 'Successo indie-pop', '2019-09-13', 'Brano'),
(14, 'Giovani Wannabe', 210, 'Tormentone estivo', '2022-05-27', 'Brano'),
(15, 'Stavo Pensando a Te', 220, 'Brano rap melodico', '2017-09-08', 'Brano'),
-- Episodi Podcast esistenti
(6, 'Episodio 1 - Ospite Speciale', 3600, 'Intervista esclusiva', '2026-03-15', 'Episodio'),
(7, 'Episodio 1 - Introduzione all AI', 2700, 'Chiacchierata informale', '2026-03-22', 'Episodio'),
(12, 'Episodio 2 - Dietro le quinte della musica', 3100, 'Seconda puntata speciale', '2026-03-29', 'Episodio'),
(16, 'Episodio 2 - Futuro della Robotica', 3400, 'Discussione avanzata con esperti', '2026-04-05', 'Episodio'),
-- Nuovi Brani (per i nuovi artisti/album inseriti)
(17, 'Blu Celeste', 205, 'Brano intenso e intimo', '2021-09-10', 'Brano'),
(18, 'Voce', 175, 'Brano sanremese di Madame', '2021-03-19', 'Brano'),
(19, 'Splash', 200, 'Successo indie estivo', '2023-05-05', 'Brano'),
(20, 'Non è detto', 204, 'Singolo pop melodico', '2018-03-16', 'Brano'),
-- Nuovi Episodi Podcast (per i nuovi autori di podcast)
(21, 'Veleno - Capitolo 1', 2800, 'Inchiesta sulla cronaca nera', '2017-03-10', 'Episodio'),
(22, 'Indagini - Il caso', 3100, 'Approfondimento giudiziario', '2023-02-01', 'Episodio'),
(23, 'Cose Molto Umane - Curiosità', 1900, 'Risposte a domande insolite', '2022-01-01', 'Episodio'),
(24, 'Lex Fridman - Extra', 3400, 'Discussione avanzata', '2023-01-01', 'Episodio'),
(25, 'Veleno - Capitolo 2', 2800, 'Continuazione inchiesta', '2017-03-15', 'Episodio'),
(26, 'Indagini - Approfondimento', 3100, 'Nuovi dettagli sul caso', '2023-02-08', 'Episodio'),
(27, 'Cose Molto Umane - Extra', 1900, 'Altre curiosità', '2022-01-10', 'Episodio');

-- 7. PODCAST
INSERT INTO Podcast (CodicePodcast, CodiceArtista, NomePodcast, DescrizionePodcast, Categoria) VALUES
(1, 16, 'Passa dal BSMT', 'Interviste a personaggi del mondo dello spettacolo', 'Intrattenimento'),
(2, 17, 'Lex Fridman Podcast', 'Conversazioni su AI, scienza e tecnologia', 'Tecnologia'),
(3, 18, 'Veleno Podcast', 'Inchieste e storie di cronaca nera', 'Cronaca'),
(4, 19, 'Indagini Podcast', 'Storie e approfondimenti sui grandi casi giudiziari', 'Attualità'),
(5, 20, 'Cose Molto Umane', 'Curiosità e risposte a domande insolite', 'Divulgazione');

-- 8. EPISODI
INSERT INTO Episodi (CodiceEpisodio, CodicePodcast, NumeroEpisodio) VALUES
(6, 1, 1),
(12, 1, 2),
(23, 1, 3),
(7, 2, 1),
(16, 2, 2),
(24, 2, 3),
(25, 3, 1),
(26, 4, 1),
(27, 5, 1);

-- 9. ALBUM (con TipoAlbum corretto in base ai brani associati)
INSERT INTO Album (CodiceAlbum, CodiceArtista, TipoAlbum, TitoloAlbum, DataPubblicazione, 
                    CasaDiscografica, MediaVoti, DurataTotale) VALUES
(101, 1, 'EP', 'OK. Respira', '2023-07-11', 'Island Records', 4.25, 345),
(102, 2, 'EP', 'RUSH!', '2023-12-01', 'Epic Records', 4.55, 441),
(103, 3, 'Singolo', 'Materia (Pelle)', '2022-08-03', 'Epic Records', 4.40, 225),
(104, 7, 'Singolo', 'Logico', '2014-05-27', 'Pressing Line', 4.50, 255),
(105, 8, 'Singolo', 'Future Nostalgia', '2020-03-27', 'Warner Records', 4.75, 203),
(106, 5, 'Singolo', 'After Hours', '2020-03-20', 'Republic Records', 4.80, 200),
(107, 9, 'EP', 'Ah dieren', '2019-04-12', 'Sony Music', 4.45, 395),
(108, 10, 'Singolo', 'Fenomeno', '2017-03-31', 'Universal Music', 4.60, 220),
(109, 9, 'LP', 'Blu Celeste', '2021-09-10', 'Columbia Records', 4.70, 205),
(110, 10, 'LP', 'Madame', '2021-03-19', 'Sugar Music', 4.65, 175),
(111, 11, 'LP', 'Lux Eterna Beach', '2023-05-05', 'Columbia Records', 4.40, 200),
(112, 12, 'LP', 'Free Love', '2019-06-21', 'Universal Music', 4.50, 210),
(113, 13, 'LP', 'Fatti Sentire', '2018-03-16', 'Warner Music Italy', 4.55, 204),
(114, 14, 'LP', 'Accetto Miracoli', '2019-11-22', 'Virgin Records', 4.35, 212),
(115, 15, 'LP', 'Flop', '2021-10-01', 'Columbia Records', 4.75, 198);

-- 10. BRANI
INSERT INTO Brani (CodiceBrano, CodiceAlbum, NumeroTraccia) VALUES
(1, 101, 1),
(2, 101, 2),
(11, 101, 3),
(3, 102, 1),
(4, 102, 2),
(5, 103, 1),
(8, 104, 1),
(9, 105, 1),
(10, 106, 1),
(13, 107, 1),
(14, 107, 2),
(15, 108, 1);

-- 11. PLAYLIST
INSERT INTO Playlist (CodicePlaylist, Username, NomePlaylist, DataCreazione, Visibilita, Collaborativa) VALUES
(1, 'mario88', 'Top Italian Pop', '2026-01-15', 'Pubblica', TRUE),
(2, 'luisa_g', 'Workout Rock', '2026-02-01', 'Privata', FALSE),
(3, 'davide_99', 'Global Hits', '2026-02-10', 'Pubblica', FALSE),
(4, 'sofia_b', 'Rap & Rhymes', '2026-02-18', 'Pubblica', TRUE),
(5, 'elena_v', 'Chillout Vibes', '2026-03-01', 'Privata', FALSE);

-- 12. COLLABORAZIONI
INSERT INTO Collaborazioni (CodicePlaylist, Username) VALUES
(1, 'luisa_g'),
(1, 'sofia_b'),
(4, 'mario88'),
(4, 'davide_99');

-- 13. CANTARE (Artista - Brano)
INSERT INTO Cantare (CodiceArtista, CodiceBrano) VALUES
(1, 1),
(1, 2),
(1, 11),
(2, 3),
(2, 4),
(3, 5),
(7, 8),
(8, 9),
(5, 10),
(9, 13),
(9, 14),
(10, 15);

-- 14. APPARTENENZE (Brano - Genere)
INSERT INTO Appartenenze (CodiceBrano, NomeGenere) VALUES
(1, 'Pop'),
(2, 'Pop'),
(11, 'Pop'),
(3, 'Rock'),
(4, 'Rock'),
(5, 'Pop'),
(8, 'Pop'),
(9, 'Elettronica'),
(10, 'R&B'),
(13, 'Indie'),
(14, 'Pop'),
(15, 'Rap');

-- 15. INCLUSIONE (Brano - Playlist)
INSERT INTO Inclusioni (CodiceBrano, CodicePlaylist) VALUES
(1, 1),
(2, 1),
(5, 1),
(8, 1),
(11, 1),
(3, 2),
(4, 2),
(9, 3),
(10, 3),
(13, 5),
(14, 5),
(15, 4);

-- 16. RECENSIONI
INSERT INTO Recensioni (Username, CodiceAlbum, Voto, Commento, DataRecensione) VALUES
('mario88', 101, 4, 'Album pop fantastico e moderno.', '2026-02-10'),
('luisa_g', 102, 5, 'Energia pura, il miglior album rock dell anno!', '2026-02-15'),
('davide_99', 105, 5, 'Capolavoro pop moderno!', '2026-02-20'),
('sofia_b', 104, 4, 'Molto orecchiabile e nostalgico.', '2026-02-22'),
('mario88', 106, 5, 'Un capolavoro R&B imperdibile.', '2026-02-25'),
('elena_v', 107, 4, 'Grandi classici indie italiani.', '2026-03-01'),
('alex_smith', 108, 4, 'Testi profondi e grandi basi.', '2026-03-02');

-- 17. LIKE BRANI
INSERT INTO LikeBrani (Username, CodiceBrano) VALUES
('mario88', 1),
('mario88', 5),
('mario88', 10),
('luisa_g', 3),
('giovanni_k', 4),
('davide_99', 9),
('sofia_b', 8),
('sofia_b', 11),
('elena_v', 13),
('alex_smith', 15),
('claire_d', 14);

-- 18. FOLLOW
INSERT INTO Follow (Username, CodiceArtista, DataInizio, DataFine) VALUES
('mario88', 1, '2026-01-01', NULL),
('luisa_g', 1, '2026-01-15', NULL),
('giovanni_k', 1, '2026-02-01', NULL),
('mario88', 2, '2026-01-01', NULL),
('elena_v', 2, '2026-02-20', NULL),
('davide_99', 8, '2026-01-10', NULL),
('sofia_b', 7, '2026-01-12', NULL),
('mario88', 5, '2026-01-05', NULL),
('marco_p', 9, '2026-02-01', NULL),
('federica_m', 10, '2026-02-05', NULL);

-- 19. CODICI INVITO
INSERT INTO CodiciInvito (Codice, DataGenerazione, Username) VALUES
('INV_MARIO', '2026-01-01', 'mario88'),
('INV_LUISA', '2026-01-15', 'luisa_g'),
('INV_GIOVANNI', '2026-01-18', 'giovanni_k'),
('INV_ELENA', '2026-01-20', 'elena_v'),
('INV_ALEX', '2026-01-22', 'alex_smith'),
('INV_CLAIRE', '2026-02-01', 'claire_d'),
('INV_DAVIDE', '2026-02-05', 'davide_99'),
('INV_SOFIA', '2026-02-10', 'sofia_b'),
('INV_MARCO', '2026-02-12', 'marco_p'),
('INV_FEDERICA', '2026-02-15', 'federica_m'),
('INV_LUCA', '2026-02-20', 'luca_t'),
('INV_MARTINA', '2026-02-25', 'martina_r');

-- 20. VALIDITA PROMOZIONI
INSERT INTO ValiditaPromozioni (CodicePromozione, CodiceAbbonamento) VALUES
('PROMO20', 2),
('WELCOME', 1),
('BLACKFRI', 2),
('STUDENT', 4);

-- 21. SOTTOSCRIZIONI
INSERT INTO Sottoscrizioni (CodiceSottoscrizione, Username, CodiceAbbonamento, CodicePromozione, CodiceInvito, DataInizio, DataFine, Stato, RinnovoAutomatico) VALUES
(1, 'mario88', 2, null, 'INV_MARCO', '2026-01-01', '2027-01-01', 'Attiva', TRUE),
(2, 'davide_99', 2, 'PROMO20', null, '2026-02-01', '2026-03-01', 'Scaduta', FALSE),
(3, 'sofia_b', 4, null, null, '2026-02-10', '2026-03-10', 'Scaduta', FALSE);

-- 22. TRANSAZIONI
INSERT INTO Transazioni (CodiceTransazione, CodiceSottoscrizione, Data, Importo, MetodoPagamento, Stato) VALUES
(1, 1, '2026-01-01 10:00:00', 71.99, 'Carta di Credito', 'Completata'),
(2, 2, '2026-02-01 12:15:00', 4.99, 'PayPal', 'Completata'),
(3, 3, '2026-02-10 14:00:00', 2.99, 'Apple Pay', 'Completata');

-- 23. EVENTI ASCOLTO (Dataset completo, ricco e privo di duplicati per tutte le statistiche)
INSERT INTO EventiAscolto (Username, CodiceContenuto, DataOra, Dispositivo, DurataEvento) VALUES

-- ==========================================
-- ASCOLTI UTENTE: mario88
-- ==========================================
('mario88', 1,  '2024-01-10 10:00:00', 'Smartphone Android', 165),
('mario88', 3,  '2024-02-15 14:30:00', 'MacBook', 194),
('mario88', 5,  '2024-03-20 18:00:00', 'Smart Speaker', 225),
('mario88', 8,  '2024-04-12 21:10:00', 'Smartphone Android', 255),
('mario88', 9,  '2024-05-05 12:00:00', 'MacBook', 203),
('mario88', 10, '2024-06-18 19:20:00', 'iPhone', 200),
('mario88', 13, '2024-08-01 16:00:00', 'Smart Speaker', 185),
('mario88', 15, '2024-09-10 22:00:00', 'Desktop Windows', 220),
('mario88', 2,  '2024-10-15 11:10:00', 'Smartphone Android', 180),
('mario88', 4,  '2024-11-20 15:20:00', 'MacBook', 247),

('mario88', 2,  '2025-01-12 11:00:00', 'Smartphone Android', 180),
('mario88', 4,  '2025-02-20 15:00:00', 'MacBook', 247),
('mario88', 8,  '2025-04-10 18:30:00', 'Smart Speaker', 255),
('mario88', 9,  '2025-06-15 21:00:00', 'MacBook', 203),
('mario88', 10, '2025-08-22 12:00:00', 'Smart Speaker', 200),
('mario88', 14, '2025-10-05 19:40:00', 'iPhone', 210),
('mario88', 1,  '2025-12-31 23:30:00', 'MacBook', 165),
('mario88', 3,  '2025-05-14 14:15:00', 'Smartphone Android', 194),
('mario88', 5,  '2025-07-19 18:00:00', 'MacBook', 225),

('mario88', 1,  '2026-01-05 14:00:00', 'Smartphone Android', 165),
('mario88', 3,  '2026-02-10 16:30:00', 'Smartphone Android', 194),
('mario88', 5,  '2026-03-01 18:00:00', 'MacBook', 225),
('mario88', 9,  '2026-03-15 20:10:00', 'Smart Speaker', 203),
('mario88', 13, '2026-04-02 11:15:00', 'iPhone', 185),
('mario88', 10, '2026-04-20 21:00:00', 'Smart Speaker', 200),

-- ==========================================
-- ASCOLTI UTENTE: davide_99
-- ==========================================
('davide_99', 9,  '2024-01-20 09:00:00', 'iPhone', 203),
('davide_99', 10, '2024-02-14 11:30:00', 'MacBook', 200),
('davide_99', 3,  '2024-03-10 15:45:00', 'iPhone', 194),
('davide_99', 5,  '2024-05-22 20:30:00', 'iPhone', 225),
('davide_99', 8,  '2024-07-14 18:20:00', 'MacBook', 255),
('davide_99', 1,  '2024-09-01 08:10:00', 'iPhone', 165),
('davide_99', 13, '2024-11-11 21:00:00', 'Smart Speaker', 185),

('davide_99', 2,  '2025-01-15 10:00:00', 'iPhone', 180),
('davide_99', 4,  '2025-03-05 14:20:00', 'iPhone', 247),
('davide_99', 9,  '2025-05-12 19:00:00', 'MacBook', 203),
('davide_99', 10, '2025-07-20 22:15:00', 'iPhone', 200),
('davide_99', 15, '2025-09-18 16:30:00', 'Smart Speaker', 220),
('davide_99', 8,  '2025-11-10 11:40:00', 'iPhone', 255),

('davide_99', 1,  '2026-01-10 12:00:00', 'iPhone', 165),
('davide_99', 3,  '2026-02-04 15:30:00', 'MacBook', 194),
('davide_99', 9,  '2026-03-03 09:15:00', 'iPhone', 203),
('davide_99', 8,  '2026-03-20 16:40:00', 'iPhone', 255),
('davide_99', 14, '2026-04-12 18:00:00', 'MacBook', 210),

-- ==========================================
-- ASCOLTI UTENTE: luisa_g
-- ==========================================
('luisa_g', 3,  '2024-02-10 18:00:00', 'Desktop Windows', 194),
('luisa_g', 4,  '2024-04-15 21:10:00', 'iPhone', 247),
('luisa_g', 1,  '2024-06-20 14:00:00', 'iPhone', 165),
('luisa_g', 8,  '2024-08-12 19:30:00', 'Desktop Windows', 255),
('luisa_g', 9,  '2024-10-05 22:00:00', 'iPhone', 203),

('luisa_g', 10, '2025-03-11 16:20:00', 'Desktop Windows', 200),
('luisa_g', 5,  '2025-06-01 12:00:00', 'iPhone', 225),
('luisa_g', 13, '2025-09-15 18:40:00', 'Desktop Windows', 185),
('luisa_g', 15, '2025-11-20 21:10:00', 'iPhone', 220),

('luisa_g', 6,  '2026-03-02 18:00:00', 'Desktop Windows', 3600),
('luisa_g', 12, '2026-03-02 19:10:00', 'Desktop Windows', 3100),
('luisa_g', 1,  '2026-04-01 10:00:00', 'iPhone', 165),
('luisa_g', 2,  '2026-04-10 14:20:00', 'iPhone', 180),
('luisa_g', 3,  '2026-05-05 19:00:00', 'Desktop Windows', 194),

-- ==========================================
-- ASCOLTI PER GLI ALTRI UTENTI (Copertura totale)
-- ==========================================
('giovanni_k', 5,  '2024-11-20 18:00:00', 'Desktop Windows', 225),
('giovanni_k', 2,  '2024-12-05 14:00:00', 'Smartphone Android', 180),
('giovanni_k', 10, '2025-04-10 12:00:00', 'Smartphone Android', 200),
('giovanni_k', 3,  '2025-08-15 19:00:00', 'Desktop Windows', 194),
('giovanni_k', 7,  '2026-04-10 11:00:00', 'Smartphone Android', 2700),
('giovanni_k', 1,  '2026-04-20 15:30:00', 'Smartphone Android', 165),

('elena_v', 1,  '2024-01-18 11:20:00', 'MacBook', 165),
('elena_v', 2,  '2025-02-14 10:00:00', 'MacBook', 180),
('elena_v', 4,  '2025-04-19 19:30:00', 'iPhone', 247),
('elena_v', 9,  '2025-08-10 21:00:00', 'MacBook', 203),
('elena_v', 13, '2026-02-25 12:00:00', 'iPhone', 185),
('elena_v', 14, '2026-03-01 15:35:00', 'Smartphone Android', 210),

('claire_d', 10, '2024-12-01 19:00:00', 'Smartphone Android', 200),
('claire_d', 10, '2025-09-05 16:45:00', 'Smartphone Android', 200),
('claire_d', 3,  '2025-10-12 20:00:00', 'Smartphone Android', 194),
('claire_d', 13, '2026-03-25 16:00:00', 'Smartphone Android', 185),
('claire_d', 1,  '2026-04-15 11:00:00', 'Smartphone Android', 165),

('alex_smith', 9,  '2025-06-30 22:15:00', 'Smart Speaker', 203),
('alex_smith', 10, '2025-09-15 14:00:00', 'iPhone', 200),
('alex_smith', 15, '2025-11-01 18:30:00', 'Smart Speaker', 220),
('alex_smith', 2,  '2026-03-20 20:10:00', 'Smart Speaker', 180),
('alex_smith', 1,  '2026-04-05 12:00:00', 'Smart Speaker', 165),

('sofia_b', 3,  '2025-11-05 18:40:00', 'Desktop Windows', 194),
('sofia_b', 11, '2025-11-25 12:10:00', 'Smartphone Android', 190),
('sofia_b', 8,  '2026-03-03 14:00:00', 'MacBook', 255),
('sofia_b', 13, '2026-03-05 14:00:00', 'MacBook', 185),
('sofia_b', 14, '2026-03-10 17:20:00', 'MacBook', 210),
('sofia_b', 15, '2026-04-02 11:20:00', 'Smartphone Android', 220),

('marco_p', 9,  '2025-08-01 21:00:00', 'iPhone', 203),
('marco_p', 1,  '2026-04-12 12:00:00', 'iPhone', 165),
('marco_p', 3,  '2026-04-16 18:00:00', 'iPhone', 194),
('marco_p', 14, '2026-04-25 12:00:00', 'iPhone', 210),
('marco_p', 5,  '2026-05-01 10:00:00', 'iPhone', 225),

('federica_m', 10, '2025-08-19 14:30:00', 'Smartphone Android', 200),
('federica_m', 2,  '2026-04-15 16:20:00', 'Smartphone Android', 180),
('federica_m', 1,  '2026-04-22 16:20:00', 'Smartphone Android', 165),
('federica_m', 3,  '2026-05-02 14:00:00', 'Smartphone Android', 194),
('federica_m', 8,  '2026-05-10 18:00:00', 'Smartphone Android', 255),

('luca_t', 9,  '2025-09-04 17:15:00', 'Desktop Windows', 203),
('luca_t', 13, '2026-05-01 10:00:00', 'Desktop Windows', 185),
('luca_t', 15, '2026-05-03 18:30:00', 'iPhone', 220),
('luca_t', 1,  '2026-05-08 12:00:00', 'Desktop Windows', 165),
('luca_t', 3,  '2026-05-12 15:00:00', 'iPhone', 194),

('martina_r', 10, '2025-09-22 11:00:00', 'Smartphone Android', 200),
('martina_r', 14, '2026-05-02 15:00:00', 'Smartphone Android', 210),
('martina_r', 11, '2026-05-10 17:00:00', 'Smartphone Android', 190),
('martina_r', 5,  '2026-05-15 11:00:00', 'Smartphone Android', 225),
('martina_r', 8,  '2026-05-20 18:00:00', 'Smartphone Android', 255);

-- test per rinnovo automatico
INSERT INTO Sottoscrizioni (Username, CodiceAbbonamento, DataInizio, DataFine, Stato, RinnovoAutomatico) 
VALUES ('luca_t', 1, DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY), 'Attiva', FALSE);
*/


USE soundwave;

-- Disabilitiamo temporaneamente i controlli sulle foreign key per facilitare l'inserimento ordinato
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

-- -----------------------------------------------------
-- 1. UTENTI (10 utenti)
-- -----------------------------------------------------
INSERT INTO Utenti (Username, Nome, Cognome, Email, Password, DataNascita, Paese, CreditoBonus) VALUES
('mario_rossi', 'Mario', 'Rossi', 'mario.rossi@email.com', 'hashed_pass_1', '1995-05-12', 'Italia', 10),
('laura_bianchi', 'Laura', 'Bianchi', 'laura.bianchi@email.com', 'hashed_pass_2', '1998-03-22', 'Italia', 5),
('giovanni_verdi', 'Giovanni', 'Verdi', 'giovanni.verdi@email.com', 'hashed_pass_3', '1990-11-05', 'Italia', 0),
('anna_neri', 'Anna', 'Neri', 'anna.neri@email.com', 'hashed_pass_4', '2001-07-19', 'Francia', 20),
('luca_gialli', 'Luca', 'Gialli', 'luca.gialli@email.com', 'hashed_pass_5', '1988-12-01', 'Italia', 0),
('sara_romano', 'Sara', 'Romano', 'sara.romano@email.com', 'hashed_pass_6', '1999-04-15', 'Spagna', 15),
('davide_colombo', 'Davide', 'Colombo', 'davide.colombo@email.com', 'hashed_pass_7', '1992-09-30', 'Italia', 0),
('elena_ferrari', 'Elena', 'Ferrari', 'elena.ferrari@email.com', 'hashed_pass_8', '1997-06-25', 'Italia', 10),
('marco_conte', 'Marco', 'Conte', 'marco.conte@email.com', 'hashed_pass_9', '1985-01-10', 'Germania', 0),
('chiara_bruni', 'Chiara', 'Bruni', 'chiara.bruni@email.com', 'hashed_pass_10', '2000-10-08', 'Italia', 5);

-- -----------------------------------------------------
-- 2. ARTISTI (20 artisti: 15 Cantanti/Band, 5 Autori Podcast di varie nazionalità)
-- -----------------------------------------------------
INSERT INTO Artisti (CodiceArtista, NomeDArte, Nome, Cognome, DataNascita, PaeseProvenienza, Biografia, AnnoInizioAttivita, TipoArtista) VALUES
(1, 'The Soundwaves', 'Gruppo', 'Rock', '2010-01-01', 'Italia', 'Band rock alternativa.', 2018, 'Band'),
(2, 'Luna Pop', 'Luna', 'Marini', '1996-08-14', 'Italia', 'Cantautrice pop moderna.', 2020, 'Cantante'),
(3, 'DJ Apex', 'Alessio', 'Riva', '1993-02-20', 'Italia', 'Producer di musica elettronica.', 2015, 'Cantante'),
(4, 'Tech Talk Podcast', 'Alex', 'Turner', '1990-05-12', 'Regno Unito', 'Autore di podcast tecnologici.', 2022, 'Autore Podcast'),
(5, 'True Crime US', 'Sarah', 'Connor', '1985-11-11', 'Stati Uniti', 'Podcaster di storie noir e misteri.', 2021, 'Autore Podcast'),
(6, 'Acoustic Soul', 'Matteo', 'Neri', '1991-04-03', 'Italia', 'Cantante acustico.', 2017, 'Cantante'),
(7, 'Starlight', 'Chloe', 'Smith', '1998-03-14', 'Stati Uniti', 'Pop star internazionale.', 2019, 'Cantante'),
(8, 'Nordic Beats', 'Lars', 'Eriksen', '1987-09-22', 'Norvegia', 'Producer di musica elettronica.', 2016, 'Band'),
(9, 'Mind & Cosmos', 'Dr. Robert', 'Lang', '1978-12-05', 'Canada', 'Autore di podcast scientifici.', 2020, 'Autore Podcast'),
(10, 'Electric Avenue', 'Band', 'Rock', '2012-06-18', 'Regno Unito', 'Band rock britannica.', 2015, 'Band'),
(11, 'Soul Sisters', 'Gruppo', 'Vocale', '2015-04-10', 'Francia', 'Gruppo vocale soul.', 2018, 'Band'),
(12, 'El Fuego', 'Carlos', 'Gomez', '1994-07-25', 'Spagna', 'Cantante latino.', 2019, 'Cantante'),
(13, 'Tokyo Dreams', 'Kenji', 'Sato', '1992-11-03', 'Giappone', 'Artista synthpop.', 2021, 'Cantante'),
(14, 'Sydney Sun', 'Emma', 'Watson', '1995-02-14', 'Australia', 'Cantautrice indie.', 2020, 'Cantante'),
(15, 'Berlin Underground', 'Gruppo', 'Techno', '2011-09-09', 'Germania', 'Collettivo techno.', 2016, 'Band'),
(16, 'Celtic Voices', 'Gruppo', 'Folk', '2014-03-17', 'Irlanda', 'Band folk acustica.', 2017, 'Band'),
(17, 'Samba Brasil', 'Lucas', 'Silva', '1990-12-12', 'Brasile', 'Cantante di musica brasiliana.', 2015, 'Cantante'),
(18, 'History Uncovered', 'Marcus', 'Aurelius', '1982-06-01', 'Grecia', 'Autore di podcast storici.', 2019, 'Autore Podcast'),
(19, 'Healthy Mind', 'Dr. Anna', 'Muller', '1986-08-30', 'Svizzera', 'Autrice di podcast sul benessere.', 2022, 'Autore Podcast'),
(20, 'Jazz Odyssey', 'Miles', 'Davis Jr', '1988-04-05', 'Stati Uniti', 'Band jazz fusion.', 2012, 'Band');

-- -----------------------------------------------------
-- 3. GENERI
-- -----------------------------------------------------
INSERT INTO Generi (NomeGenere) VALUES
('Pop'), ('Rock'), ('Elettronica'), ('Acustica'), ('Jazz'), ('Folk');

-- -----------------------------------------------------
-- 4. ALBUM (15 album con tipo coerente)
-- -----------------------------------------------------
INSERT INTO Album (CodiceAlbum, CodiceArtista, TipoAlbum, TitoloAlbum, DataPubblicazione, CasaDiscografica, MediaVoti, DurataTotale) VALUES
(1, 1, 'LP', 'Electric Dreams', '2024-03-10', 'Sony Music', 4.50, 2160),
(2, 11, 'EP', 'French Sessions', '2024-06-15', 'Paris Rec', 4.00, 960),
(3, 7, 'Singolo', 'American Sky', '2024-01-05', 'Universal US', 3.80, 240),
(4, 2, 'LP', 'Raggi di Sole', '2024-05-20', 'Universal', 4.70, 2400),
(5, 12, 'EP', 'Caliente', '2025-02-10', 'Latino Music', 4.20, 1000),
(6, 13, 'Singolo', 'Neon Tokyo', '2025-06-01', 'Sony JP', 4.00, 210),
(7, 3, 'LP', 'Cyber Pulse', '2025-01-15', 'Warner', 4.80, 2700),
(8, 8, 'EP', 'Nordic Light', '2025-09-10', 'Oslo Records', 4.30, 1100),
(9, 14, 'Singolo', 'Down Under', '2026-01-10', 'Indie AU', 3.90, 190),
(10, 6, 'LP', 'Wood & Strings', '2024-04-12', 'Indie Records', 4.60, 2200),
(11, 15, 'EP', 'Club Berlin', '2025-05-05', 'Techno Rec', 4.10, 1050),
(12, 16, 'Singolo', 'Green Hills', '2026-02-15', 'Dublin Sound', 4.50, 230),
(13, 17, 'LP', 'Rio', '2025-11-20', 'Samba Music', 4.40, 2500),
(14, 20, 'LP', 'Fusion', '2026-03-01', 'Jazz Club', 4.90, 2300),
(15, 10, 'Singolo', 'Overdrive', '2026-04-01', 'UK indie', 4.20, 220);

-- -----------------------------------------------------
-- 5. CONTENUTI & BRANI (Corrispondenti ai brani degli album)
-- -----------------------------------------------------
INSERT INTO Contenuti (CodiceContenuto, Titolo, Durata, Descrizione, DataPubblicazione, TipoContenuto) VALUES
-- Album 1 (LP - 8 brani)
(1, 'Intro Dreams', 180, 'Intro', '2024-03-10', 'Brano'),
(2, 'Electric Soul', 300, 'Hit track', '2024-03-10', 'Brano'),
(3, 'Night Run', 270, 'Synthwave track', '2024-03-10', 'Brano'),
(4, 'Echoes', 250, 'Ambient track', '2024-03-10', 'Brano'),
(5, 'Shadows', 290, 'Rock ballad', '2024-03-10', 'Brano'),
(6, 'Fast Lane', 260, 'Fast rock', '2024-03-10', 'Brano'),
(7, 'Lost Time', 310, 'Slow tempo', '2024-03-10', 'Brano'),
(8, 'Outro Dreams', 200, 'Outro', '2024-03-10', 'Brano'),
-- Album 2 (EP - 4 brani)
(9, 'French Intro', 150, 'Live', '2024-06-15', 'Brano'),
(10, 'Paris Groove', 280, 'Live', '2024-06-15', 'Brano'),
(11, 'Chanson', 270, 'Live', '2024-06-15', 'Brano'),
(12, 'French Outro', 260, 'Live', '2024-06-15', 'Brano'),
-- Album 3 (Singolo - 1 brano)
(13, 'American Sky', 240, 'Single', '2024-01-05', 'Brano'),
-- Album 4 (LP - 8 brani)
(14, 'Raggi di Sole 1', 300, 'Pop song', '2024-05-20', 'Brano'),
(15, 'Raggi di Sole 2', 290, 'Pop song', '2024-05-20', 'Brano'),
(16, 'Raggi di Sole 3', 310, 'Pop song', '2024-05-20', 'Brano'),
(17, 'Raggi di Sole 4', 280, 'Pop song', '2024-05-20', 'Brano'),
(18, 'Raggi di Sole 5', 300, 'Pop song', '2024-05-20', 'Brano'),
(19, 'Raggi di Sole 6', 270, 'Pop song', '2024-05-20', 'Brano'),
(20, 'Raggi di Sole 7', 320, 'Pop song', '2024-05-20', 'Brano'),
(21, 'Raggi di Sole 8', 330, 'Pop song', '2024-05-20', 'Brano'),
-- Album 5 (EP - 4 brani)
(22, 'Caliente 1', 250, 'EP track', '2025-02-10', 'Brano'),
(23, 'Caliente 2', 260, 'EP track', '2025-02-10', 'Brano'),
(24, 'Caliente 3', 240, 'EP track', '2025-02-10', 'Brano'),
(25, 'Caliente 4', 250, 'EP track', '2025-02-10', 'Brano'),
-- Album 6 (Singolo - 1 brano)
(26, 'Neon Tokyo', 210, 'Single', '2025-06-01', 'Brano'),
-- Album 7 (LP - 8 brani)
(27, 'Cyber 1', 330, 'Electro', '2025-01-15', 'Brano'),
(28, 'Cyber 2', 340, 'Electro', '2025-01-15', 'Brano'),
(29, 'Cyber 3', 350, 'Electro', '2025-01-15', 'Brano'),
(30, 'Cyber 4', 320, 'Electro', '2025-01-15', 'Brano'),
(31, 'Cyber 5', 310, 'Electro', '2025-01-15', 'Brano'),
(32, 'Cyber 6', 300, 'Electro', '2025-01-15', 'Brano'),
(33, 'Cyber 7', 380, 'Electro', '2025-01-15', 'Brano'),
(34, 'Cyber 8', 370, 'Electro', '2025-01-15', 'Brano'),
-- Album 8 (EP - 4 brani)
(35, 'Nordic 1', 280, 'Electro EP', '2025-09-10', 'Brano'),
(36, 'Nordic 2', 270, 'Electro EP', '2025-09-10', 'Brano'),
(37, 'Nordic 3', 290, 'Electro EP', '2025-09-10', 'Brano'),
(38, 'Nordic 4', 260, 'Electro EP', '2025-09-10', 'Brano'),
-- Album 9 (Singolo - 1 brano)
(39, 'Down Under', 190, 'Single', '2026-01-10', 'Brano'),
-- Album 10 (LP - 8 brani)
(40, 'Wood 1', 280, 'Acoustic', '2024-04-12', 'Brano'),
(41, 'Wood 2', 270, 'Acoustic', '2024-04-12', 'Brano'),
(42, 'Wood 3', 290, 'Acoustic', '2024-04-12', 'Brano'),
(43, 'Wood 4', 260, 'Acoustic', '2024-04-12', 'Brano'),
(44, 'Wood 5', 300, 'Acoustic', '2024-04-12', 'Brano'),
(45, 'Wood 6', 270, 'Acoustic', '2024-04-12', 'Brano'),
(46, 'Wood 7', 280, 'Acoustic', '2024-04-12', 'Brano'),
(47, 'Wood 8', 250, 'Acoustic', '2024-04-12', 'Brano'),
-- Album 11 (EP - 4 brani)
(48, 'Berlin 1', 260, 'Techno EP', '2025-05-05', 'Brano'),
(49, 'Berlin 2', 270, 'Techno EP', '2025-05-05', 'Brano'),
(50, 'Berlin 3', 260, 'Techno EP', '2025-05-05', 'Brano'),
(51, 'Berlin 4', 260, 'Techno EP', '2025-05-05', 'Brano'),
-- Album 12 (Singolo - 1 brano)
(52, 'Green Hills', 230, 'Single', '2026-02-15', 'Brano'),
-- Album 13 (LP - 8 brani)
(53, 'Rio 1', 310, 'Samba', '2025-11-20', 'Brano'),
(54, 'Rio 2', 300, 'Samba', '2025-11-20', 'Brano'),
(55, 'Rio 3', 320, 'Samba', '2025-11-20', 'Brano'),
(56, 'Rio 4', 290, 'Samba', '2025-11-20', 'Brano'),
(57, 'Rio 5', 330, 'Samba', '2025-11-20', 'Brano'),
(58, 'Rio 6', 310, 'Samba', '2025-11-20', 'Brano'),
(59, 'Rio 7', 320, 'Samba', '2025-11-20', 'Brano'),
(60, 'Rio 8', 320, 'Samba', '2025-11-20', 'Brano'),
-- Album 14 (LP - 8 brani)
(61, 'Jazz 1', 290, 'Jazz', '2026-03-01', 'Brano'),
(62, 'Jazz 2', 280, 'Jazz', '2026-03-01', 'Brano'),
(63, 'Jazz 3', 300, 'Jazz', '2026-03-01', 'Brano'),
(64, 'Jazz 4', 270, 'Jazz', '2026-03-01', 'Brano'),
(65, 'Jazz 5', 290, 'Jazz', '2026-03-01', 'Brano'),
(66, 'Jazz 6', 280, 'Jazz', '2026-03-01', 'Brano'),
(67, 'Jazz 7', 300, 'Jazz', '2026-03-01', 'Brano'),
(68, 'Jazz 8', 290, 'Jazz', '2026-03-01', 'Brano'),
-- Album 15 (Singolo - 1 brano)
(69, 'Overdrive', 220, 'Single', '2026-04-01', 'Brano');

INSERT INTO Brani (CodiceBrano, CodiceAlbum, NumeroTraccia) VALUES
(1,1,1), (2,1,2), (3,1,3), (4,1,4), (5,1,5), (6,1,6), (7,1,7), (8,1,8),
(9,2,1), (10,2,2), (11,2,3), (12,2,4),
(13,3,1),
(14,4,1), (15,4,2), (16,4,3), (17,4,4), (18,4,5), (19,4,6), (20,4,7), (21,4,8),
(22,5,1), (23,5,2), (24,5,3), (25,5,4),
(26,6,1),
(27,7,1), (28,7,2), (29,7,3), (30,7,4), (31,7,5), (32,7,6), (33,7,7), (34,7,8),
(35,8,1), (36,8,2), (37,8,3), (38,8,4),
(39,9,1),
(40,10,1), (41,10,2), (42,10,3), (43,10,4), (44,10,5), (45,10,6), (46,10,7), (47,10,8),
(48,11,1), (49,11,2), (50,11,3), (51,11,4),
(52,12,1),
(53,13,1), (54,13,2), (55,13,3), (56,13,4), (57,13,5), (58,13,6), (59,13,7), (60,13,8),
(61,14,1), (62,14,2), (63,14,3), (64,14,4), (65,14,5), (66,14,6), (67,14,7), (68,14,8),
(69,15,1);

-- -----------------------------------------------------
-- 6. PODCAST E EPISODI (5 podcast con autori da 4, 5, 9, 18, 19)
-- -----------------------------------------------------
INSERT INTO Podcast (CodicePodcast, CodiceArtista, NomePodcast, DescrizionePodcast, Categoria) VALUES
(1, 4, 'Tech Inside UK', 'Global tech news.', 'Tecnologia'),
(2, 5, 'American Crime Files', 'True crime stories.', 'True Crime'),
(3, 9, 'Science & Cosmos', 'Philosophy & science.', 'Scienza'),
(4, 18, 'Ancient Empires', 'History of ancient worlds.', 'Storia'),
(5, 19, 'Mindfulness Daily', 'Mental health and wellness.', 'Benessere');

INSERT INTO Contenuti (CodiceContenuto, Titolo, Durata, Descrizione, DataPubblicazione, TipoContenuto) VALUES
(101, 'Ep 1: Global AI', 1800, 'AI revolution', '2024-02-10', 'Episodio'),
(102, 'Ep 2: Cloud Systems', 2000, 'Cloud architecture', '2024-02-17', 'Episodio'),
(103, 'Ep 1: The Missing Witness', 2200, 'US Crime case', '2025-03-01', 'Episodio'),
(104, 'Ep 1: Quantum Computing', 2500, 'Future tech', '2024-05-12', 'Episodio'),
(105, 'Ep 1: Rome Fall', 2400, 'Ancient history', '2024-05-19', 'Episodio'),
(106, 'Ep 1: Inner Peace', 2100, 'Relax guide', '2025-01-10', 'Episodio'),
(107, 'Ep 3: Cyber Security', 1900, 'Security trends', '2026-01-15', 'Episodio');

INSERT INTO Episodi (CodiceEpisodio, CodicePodcast, NumeroEpisodio) VALUES
(101, 1, 1), (102, 1, 2),
(103, 2, 1),
(104, 3, 1),
(105, 4, 1),
(106, 5, 1),
(107, 1, 3);

-- -----------------------------------------------------
-- 7. PLAYLIST E INCLUSIONI
-- -----------------------------------------------------
INSERT INTO Playlist (CodicePlaylist, Username, NomePlaylist, DataCreazione, Visibilita, Collaborativa) VALUES
(1, 'mario_rossi', 'I miei preferiti', '2024-03-15', 'Pubblica', TRUE),
(2, 'laura_bianchi', 'Relax Elettronico', '2025-02-01', 'Pubblica', FALSE),
(3, 'giovanni_verdi', 'Workout Rock', '2024-05-10', 'Privata', FALSE),
(4, 'anna_neri', 'Global Hits', '2026-03-02', 'Pubblica', TRUE);

INSERT INTO Inclusioni (CodiceBrano, CodicePlaylist) VALUES
(1, 1), (2, 1), (14, 1),
(27, 2), (28, 2), (35, 2),
(3, 3), (48, 3), (49, 3),
(61, 4), (62, 4), (13, 4);

-- -----------------------------------------------------
-- 8. COLLABORAZIONI
-- -----------------------------------------------------
INSERT INTO Collaborazioni (CodicePlaylist, Username) VALUES
(1, 'laura_bianchi'),
(4, 'giovanni_verdi');

-- -----------------------------------------------------
-- 9. ABBONAMENTI, PROMOZIONI E CODICI INVITO
-- -----------------------------------------------------
INSERT INTO Abbonamenti (CodiceAbbonamento, TipoAbbonamento, Durata, Costo) VALUES
(1, 'Mensile Standard', 1, 9.99),
(2, 'Annuale Premium', 12, 89.99),
(3, 'Famiglia Mensile', 1, 14.99),
(4, 'Studente Mensile', 1, 4.99);

INSERT INTO Promozioni (CodicePromozione, Nome, Descrizione, DataInizioPromo, DataFinePromo, TipoSconto, ValoreSconto, MesiRichiesti) VALUES
('PROMO20', 'Promo Estate 2026', 'Sconto del 20% sul piano annuale', '2026-06-01', '2026-08-31', 'Percentuale', 20.00, 12),
('WELCOME', 'Benvenuto', 'Sconto fisso di 5 Euro', '2026-01-01', '2026-12-31', 'Fisso', 5.00, 1),
('BLACKFRI', 'Black Friday', 'Sconto del 50% sul piano annuale', '2026-11-20', '2026-11-30', 'Percentuale', 50.00, 12),
('STUDENT', 'Student Promo', 'Sconto fisso speciale studenti', '2026-01-01', '2026-12-31', 'Fisso', 2.00, 1);

INSERT INTO ValiditaPromozioni (CodicePromozione, CodiceAbbonamento) VALUES
('PROMO20', 2),
('BLACKFRI', 2),
('WELCOME', 1),
('STUDENT', 4);

INSERT INTO CodiciInvito (Codice, DataGenerazione, Username) VALUES
('INV-MARIO123', '2024-03-15', 'mario_rossi'),
('INV-LAURA456', '2025-01-10', 'laura_bianchi'),
('INV-GIOVANNI789', '2024-05-10', 'giovanni_verdi'),
('INV-ANNA321', '2026-03-02', 'anna_neri'),
('INV-LUCA654', '2024-04-12', 'luca_gialli'),
('INV-SARA987', '2025-11-20', 'sara_romano'),
('INV-DAVIDE111', '2024-04-20', 'davide_colombo'),
('INV-ELENA222', '2024-05-30', 'elena_ferrari'),
('INV-MARCO333', '2024-03-18', 'marco_conte'),
('INV-CHIARA444', '2026-03-02', 'chiara_bruni');

-- -----------------------------------------------------
-- 10. SOTTOSCRIZIONI E TRANSAZIONI
-- -----------------------------------------------------
INSERT INTO Sottoscrizioni (CodiceSottoscrizione, Username, CodiceAbbonamento, CodicePromozione, CodiceInvito, DataInizio, DataFine, Stato, RinnovoAutomatico) VALUES
(1, 'mario_rossi', 1, 'WELCOME', 'INV-MARIO123', '2026-06-01', '2026-07-01', 'Attiva', TRUE),
(2, 'laura_bianchi', 4, 'STUDENT', 'INV-LAURA456', '2026-06-01', '2026-07-01', 'Attiva', TRUE),
(3, 'giovanni_verdi', 3, NULL, 'INV-GIOVANNI789', '2026-06-01', '2026-07-01', 'Attiva', FALSE);

INSERT INTO Transazioni (CodiceTransazione, CodiceSottoscrizione, Data, Importo, MetodoPagamento, Stato) VALUES
(1, 1, '2024-06-01 10:00:00', 7.99, 'Carta di Credito', 'Completata'),
(2, 2, '2024-11-25 14:30:00', 89.99, 'PayPal', 'Completata'),
(3, 3, '2026-06-01 10:15:00', 14.99, 'Carta di Credito', 'Completata');

-- -----------------------------------------------------
-- 11. LIKE BRANI E FOLLOW
-- -----------------------------------------------------
INSERT INTO LikeBrani (Username, CodiceBrano) VALUES
('mario_rossi', 2), ('mario_rossi', 14),
('laura_bianchi', 27), ('laura_bianchi', 35),
('giovanni_verdi', 40), ('giovanni_verdi', 53),
('anna_neri', 13), ('anna_neri', 61);

INSERT INTO Follow (Username, CodiceArtista, DataInizio, DataFine) VALUES
('mario_rossi', 1, '2024-03-12', NULL),
('mario_rossi', 7, '2024-05-20', NULL),
('laura_bianchi', 3, '2025-01-15', NULL),
('giovanni_verdi', 6, '2024-04-12', NULL),
('anna_neri', 13, '2024-05-25', NULL);

-- -----------------------------------------------------
-- 12. CANTARE E APPARTENENZE
-- -----------------------------------------------------
INSERT INTO Cantare (CodiceArtista, CodiceBrano) VALUES
(1,1), (1,2), (1,3), (1,4), (1,5), (1,6), (1,7), (1,8),
(11,9), (11,10), (11,11), (11,12),
(7,13),
(2,14), (2,15), (2,16), (2,17), (2,18), (2,19), (2,20), (2,21),
(12,22), (12,23), (12,24), (12,25),
(13,26),
(3,27), (3,28), (3,29), (3,30), (3,31), (3,32), (3,33), (3,34),
(8,35), (8,36), (8,37), (8,38),
(14,39),
(6,40), (6,41), (6,42), (6,43), (6,44), (6,45), (6,46), (6,47),
(15,48), (15,49), (15,50), (15,51),
(16,52),
(17,53), (17,54), (17,55), (17,56), (17,57), (17,58), (17,59), (17,60),
(20,61), (20,62), (20,63), (20,64), (20,65), (20,66), (20,67), (20,68),
(10,69);

INSERT INTO Appartenenze (CodiceBrano, NomeGenere) VALUES
(1, 'Rock'), (2, 'Rock'), (3, 'Rock'), (4, 'Rock'), (5, 'Rock'), (6, 'Rock'), (7, 'Rock'), (8, 'Rock'),
(9, 'Pop'), (10, 'Pop'), (11, 'Pop'), (12, 'Pop'),
(13, 'Pop'), (14, 'Pop'), (15, 'Pop'), (16, 'Pop'), (17, 'Pop'), (18, 'Pop'), (19, 'Pop'), (20, 'Pop'), (21, 'Pop'),
(22, 'Pop'), (23, 'Pop'), (24, 'Pop'), (25, 'Pop'), (26, 'Pop'),
(27, 'Elettronica'), (28, 'Elettronica'), (29, 'Elettronica'), (30, 'Elettronica'), (31, 'Elettronica'), (32, 'Elettronica'), (33, 'Elettronica'), (34, 'Elettronica'),
(35, 'Elettronica'), (36, 'Elettronica'), (37, 'Elettronica'), (38, 'Elettronica'), (39, 'Acustica'),
(40, 'Acustica'), (41, 'Acustica'), (42, 'Acustica'), (43, 'Acustica'), (44, 'Acustica'), (45, 'Acustica'), (46, 'Acustica'), (47, 'Acustica'),
(48, 'Elettronica'), (49, 'Elettronica'), (50, 'Elettronica'), (51, 'Elettronica'), (52, 'Folk'),
(53, 'Pop'), (54, 'Pop'), (55, 'Pop'), (56, 'Pop'), (57, 'Pop'), (58, 'Pop'), (59, 'Pop'), (60, 'Pop'),
(61, 'Jazz'), (62, 'Jazz'), (63, 'Jazz'), (64, 'Jazz'), (65, 'Jazz'), (66, 'Jazz'), (67, 'Jazz'), (68, 'Jazz'),
(69, 'Rock');

-- -----------------------------------------------------
-- 13. RECENSIONI (Voti compresi tra 1 e 5)
-- -----------------------------------------------------
INSERT INTO Recensioni (Username, CodiceAlbum, Voto, Commento, DataRecensione) VALUES
('mario_rossi', 1, 5, 'Album eccezionale!', '2024-03-12'),
('mario_rossi', 4, 4, 'Molto orecchiabile.', '2024-05-22'),
('laura_bianchi', 1, 4, 'Bellissimi suoni.', '2024-04-01'),
('laura_bianchi', 7, 5, 'Capolavoro elettronico!', '2025-01-20'),
('giovanni_verdi', 10, 5, 'Rilassante e profondo.', '2024-04-15'),
('anna_neri', 4, 5, 'La voce è spettacolare.', '2024-06-01'),
('luca_gialli', 7, 4, 'Ottimi beat.', '2025-02-01');

-- -----------------------------------------------------
-- 14. EVENTI ASCOLTO (Distribuiti tra 2024, 2025 e 2026)
-- Metà utenti hanno 20-30 ascolti, l'altra metà almeno 7
-- -----------------------------------------------------
INSERT INTO EventiAscolto (Username, CodiceContenuto, DataOra, Dispositivo, DurataEvento) VALUES
-- Utenti molto attivi (>20 ascolti): mario_rossi, laura_bianchi, giovanni_verdi, anna_neri, luca_gialli
-- mario_rossi (25 ascolti)
('mario_rossi', 1, '2024-03-15 10:00:00', 'Smartphone', 180),
('mario_rossi', 2, '2024-03-15 10:03:00', 'Smartphone', 300),
('mario_rossi', 3, '2024-04-10 14:30:00', 'PC', 270),
('mario_rossi', 14, '2024-06-01 09:15:00', 'Smartphone', 300),
('mario_rossi', 15, '2024-06-01 09:20:00', 'Smartphone', 290),
('mario_rossi', 101, '2024-02-11 21:00:00', 'Smart Speaker', 1800),
('mario_rossi', 27, '2025-01-20 18:00:00', 'Tablet', 330),
('mario_rossi', 28, '2025-01-20 18:06:00', 'Tablet', 340),
('mario_rossi', 35, '2025-09-12 11:00:00', 'Smartphone', 280),
('mario_rossi', 40, '2024-04-16 12:00:00', 'PC', 280),
('mario_rossi', 41, '2024-04-16 12:05:00', 'PC', 270),
('mario_rossi', 53, '2025-11-21 15:00:00', 'Smartphone', 310),
('mario_rossi', 61, '2026-03-02 10:00:00', 'PC', 290),
('mario_rossi', 62, '2026-03-02 10:05:00', 'PC', 280),
('mario_rossi', 13, '2024-01-06 08:00:00', 'Smartphone', 240),
('mario_rossi', 26, '2025-06-02 14:00:00', 'Tablet', 210),
('mario_rossi', 39, '2026-01-11 19:00:00', 'Smartphone', 190),
('mario_rossi', 52, '2026-02-16 09:00:00', 'PC', 230),
('mario_rossi', 69, '2026-04-02 16:00:00', 'Smartphone', 220),
('mario_rossi', 102, '2024-02-18 21:00:00', 'Smart Speaker', 2000),
('mario_rossi', 103, '2025-03-02 21:00:00', 'Smart Speaker', 2200),
('mario_rossi', 104, '2024-05-13 21:00:00', 'Smart Speaker', 2500),
('mario_rossi', 105, '2024-05-20 21:00:00', 'Smart Speaker', 2400),
('mario_rossi', 106, '2025-01-11 21:00:00', 'Smart Speaker', 2100),
('mario_rossi', 107, '2026-01-16 21:00:00', 'Smart Speaker', 1900),

-- laura_bianchi (22 ascolti)
('laura_bianchi', 7, '2025-01-25 12:00:00', 'Smartphone', 310),
('laura_bianchi', 27, '2025-01-26 13:00:00', 'PC', 330),
('laura_bianchi', 28, '2025-01-26 13:06:00', 'PC', 340),
('laura_bianchi', 29, '2025-01-26 13:12:00', 'PC', 350),
('laura_bianchi', 35, '2025-09-11 14:00:00', 'Smartphone', 280),
('laura_bianchi', 36, '2025-09-11 14:05:00', 'Smartphone', 270),
('laura_bianchi', 48, '2025-05-06 10:00:00', 'Tablet', 260),
('laura_bianchi', 49, '2025-05-06 10:05:00', 'Tablet', 270),
('laura_bianchi', 61, '2026-03-03 16:00:00', 'PC', 290),
('laura_bianchi', 62, '2026-03-03 16:05:00', 'PC', 280),
('laura_bianchi', 1, '2024-03-11 11:00:00', 'Smartphone', 180),
('laura_bianchi', 2, '2024-03-11 11:05:00', 'Smartphone', 300),
('laura_bianchi', 14, '2024-05-21 12:00:00', 'PC', 300),
('laura_bianchi', 15, '2024-05-21 12:05:00', 'PC', 290),
('laura_bianchi', 53, '2025-11-22 17:00:00', 'Smartphone', 310),
('laura_bianchi', 54, '2025-11-22 17:05:00', 'Smartphone', 300),
('laura_bianchi', 103, '2025-03-05 21:00:00', 'Smart Speaker', 2200),
('laura_bianchi', 101, '2024-02-12 21:00:00', 'Smart Speaker', 1800),
('laura_bianchi', 104, '2024-05-14 21:00:00', 'Smart Speaker', 2500),
('laura_bianchi', 105, '2024-05-21 21:00:00', 'Smart Speaker', 2400),
('laura_bianchi', 106, '2025-01-12 21:00:00', 'Smart Speaker', 2100),
('laura_bianchi', 107, '2026-01-17 21:00:00', 'Smart Speaker', 1900),

-- giovanni_verdi (20 ascolti)
('giovanni_verdi', 40, '2024-04-15 10:00:00', 'PC', 280),
('giovanni_verdi', 41, '2024-04-15 10:05:00', 'PC', 270),
('giovanni_verdi', 53, '2025-11-25 18:00:00', 'Smartphone', 310),
('giovanni_verdi', 54, '2025-11-25 18:05:00', 'Smartphone', 300),
('giovanni_verdi', 3, '2024-03-15 15:00:00', 'PC', 270),
('giovanni_verdi', 5, '2024-03-15 15:05:00', 'PC', 290),
('giovanni_verdi', 6, '2024-03-15 15:10:00', 'PC', 260),
('giovanni_verdi', 48, '2025-05-08 12:00:00', 'Smartphone', 260),
('giovanni_verdi', 49, '2025-05-08 12:05:00', 'Smartphone', 270),
('giovanni_verdi', 61, '2026-03-05 14:00:00', 'PC', 290),
('giovanni_verdi', 62, '2026-03-05 14:05:00', 'PC', 280),
('giovanni_verdi', 13, '2024-01-08 09:00:00', 'Smartphone', 240),
('giovanni_verdi', 26, '2025-06-03 11:00:00', 'PC', 210),
('giovanni_verdi', 39, '2026-01-12 16:00:00', 'Smartphone', 190),
('giovanni_verdi', 104, '2024-05-15 21:00:00', 'Smart Speaker', 2500),
('giovanni_verdi', 101, '2024-02-13 21:00:00', 'Smart Speaker', 1800),
('giovanni_verdi', 102, '2024-02-20 21:00:00', 'Smart Speaker', 2000),
('giovanni_verdi', 103, '2025-03-03 21:00:00', 'Smart Speaker', 2200),
('giovanni_verdi', 106, '2025-01-13 21:00:00', 'Smart Speaker', 2100),
('giovanni_verdi', 107, '2026-01-18 21:00:00', 'Smart Speaker', 1900),

-- anna_neri (20 ascolti)
('anna_neri', 14, '2024-05-25 10:00:00', 'Smartphone', 300),
('anna_neri', 15, '2024-05-25 10:05:00', 'Smartphone', 290),
('anna_neri', 61, '2026-03-04 11:00:00', 'Tablet', 290),
('anna_neri', 62, '2026-03-04 11:05:00', 'Tablet', 280),
('anna_neri', 13, '2024-01-07 10:00:00', 'Smartphone', 240),
('anna_neri', 22, '2025-02-11 15:00:00', 'PC', 250),
('anna_neri', 23, '2025-02-11 15:05:00', 'PC', 260),
('anna_neri', 26, '2025-06-02 16:00:00', 'Smartphone', 210),
('anna_neri', 39, '2026-01-11 12:00:00', 'Tablet', 190),
('anna_neri', 52, '2026-02-16 14:00:00', 'Smartphone', 230),
('anna_neri', 1, '2024-03-12 10:00:00', 'PC', 180),
('anna_neri', 2, '2024-03-12 10:05:00', 'PC', 300),
('anna_neri', 27, '2025-01-16 11:00:00', 'Smartphone', 330),
('anna_neri', 28, '2025-01-16 11:05:00', 'Smartphone', 340),
('anna_neri', 40, '2024-04-13 14:00:00', 'Tablet', 280),
('anna_neri', 101, '2024-02-12 21:00:00', 'Smart Speaker', 1800),
('anna_neri', 102, '2024-02-19 21:00:00', 'Smart Speaker', 2000),
('anna_neri', 103, '2025-03-04 21:00:00', 'Smart Speaker', 2200),
('anna_neri', 106, '2025-01-14 21:00:00', 'Smart Speaker', 2100),
('anna_neri', 107, '2026-01-19 21:00:00', 'Smart Speaker', 1900),

-- luca_gialli (20 ascolti)
('luca_gialli', 27, '2025-01-18 10:00:00', 'PC', 330),
('luca_gialli', 28, '2025-01-18 10:05:00', 'PC', 340),
('luca_gialli', 29, '2025-01-18 10:10:00', 'PC', 350),
('luca_gialli', 35, '2025-09-15 16:00:00', 'Smartphone', 280),
('luca_gialli', 36, '2025-09-15 16:05:00', 'Smartphone', 270),
('luca_gialli', 1, '2024-03-14 12:00:00', 'PC', 180),
('luca_gialli', 2, '2024-03-14 12:05:00', 'PC', 300),
('luca_gialli', 14, '2024-05-22 14:00:00', 'Smartphone', 300),
('luca_gialli', 40, '2024-04-14 15:00:00', 'PC', 280),
('luca_gialli', 53, '2025-11-21 11:00:00', 'Smartphone', 310),
('luca_gialli', 61, '2026-03-02 18:00:00', 'PC', 290),
('luca_gialli', 13, '2024-01-09 10:00:00', 'Smartphone', 240),
('luca_gialli', 26, '2025-06-04 12:00:00', 'PC', 210),
('luca_gialli', 39, '2026-01-13 14:00:00', 'Smartphone', 190),
('luca_gialli', 52, '2026-02-17 15:00:00', 'PC', 230),
('luca_gialli', 101, '2024-02-13 21:00:00', 'Smart Speaker', 1800),
('luca_gialli', 102, '2024-02-21 21:00:00', 'Smart Speaker', 2000),
('luca_gialli', 104, '2024-05-16 21:00:00', 'Smart Speaker', 2500),
('luca_gialli', 106, '2025-01-15 21:00:00', 'Smart Speaker', 2100),
('luca_gialli', 107, '2026-01-20 21:00:00', 'Smart Speaker', 1900),

-- Utenti standard (>7 ascolti): sara_romano, davide_colombo, elena_ferrari, marco_conte, chiara_bruni
-- sara_romano (8 ascolti)
('sara_romano', 53, '2025-11-26 10:00:00', 'Smartphone', 310),
('sara_romano', 54, '2025-11-26 10:05:00', 'Smartphone', 300),
('sara_romano', 1, '2024-03-15 11:00:00', 'PC', 180),
('sara_romano', 14, '2024-05-23 12:00:00', 'Smartphone', 300),
('sara_romano', 27, '2025-01-19 14:00:00', 'Tablet', 330),
('sara_romano', 61, '2026-03-03 15:00:00', 'PC', 290),
('sara_romano', 103, '2025-03-06 21:00:00', 'Smart Speaker', 2200),
('sara_romano', 107, '2026-01-21 21:00:00', 'Smart Speaker', 1900),

-- davide_colombo (8 ascolti)
('davide_colombo', 40, '2024-04-20 10:00:00', 'PC', 280),
('davide_colombo', 41, '2024-04-20 10:05:00', 'PC', 270),
('davide_colombo', 1, '2024-03-16 12:00:00', 'Smartphone', 180),
('davide_colombo', 14, '2024-05-24 14:00:00', 'PC', 300),
('davide_colombo', 27, '2025-01-21 16:00:00', 'Smartphone', 330),
('davide_colombo', 53, '2025-11-27 18:00:00', 'Tablet', 310),
('davide_colombo', 104, '2024-05-17 21:00:00', 'Smart Speaker', 2500),
('davide_colombo', 107, '2026-01-22 21:00:00', 'Smart Speaker', 1900),

-- elena_ferrari (8 ascolti)
('elena_ferrari', 14, '2024-05-30 10:00:00', 'Smartphone', 300),
('elena_ferrari', 15, '2024-05-30 10:05:00', 'Smartphone', 290),
('elena_ferrari', 1, '2024-03-17 11:00:00', 'PC', 180),
('elena_ferrari', 27, '2025-01-22 12:00:00', 'Tablet', 330),
('elena_ferrari', 40, '2024-04-21 14:00:00', 'Smartphone', 280),
('elena_ferrari', 61, '2026-03-04 16:00:00', 'PC', 290),
('elena_ferrari', 101, '2024-02-14 21:00:00', 'Smart Speaker', 1800),
('elena_ferrari', 107, '2026-01-23 21:00:00', 'Smart Speaker', 1900),

-- marco_conte (8 ascolti)
('marco_conte', 27, '2025-01-18 10:00:00', 'PC', 330),
('marco_conte', 28, '2025-01-18 10:05:00', 'PC', 340),
('marco_conte', 1, '2024-03-18 12:00:00', 'Smartphone', 180),
('marco_conte', 14, '2024-05-25 14:00:00', 'PC', 300),
('marco_conte', 40, '2024-04-22 16:00:00', 'Tablet', 280),
('marco_conte', 53, '2025-11-28 18:00:00', 'Smartphone', 310),
('marco_conte', 102, '2024-02-22 21:00:00', 'Smart Speaker', 2000),
('marco_conte', 107, '2026-01-24 21:00:00', 'Smart Speaker', 1900),

-- chiara_bruni (8 ascolti)
('chiara_bruni', 61, '2026-03-02 10:00:00', 'Smartphone', 290),
('chiara_bruni', 62, '2026-03-02 10:05:00', 'Smartphone', 280),
('chiara_bruni', 1, '2024-03-19 11:00:00', 'PC', 180),
('chiara_bruni', 14, '2024-05-26 12:00:00', 'Tablet', 300),
('chiara_bruni', 27, '2025-01-23 14:00:00', 'Smartphone', 330),
('chiara_bruni', 40, '2024-04-23 15:00:00', 'PC', 280),
('chiara_bruni', 106, '2025-01-16 21:00:00', 'Smart Speaker', 2100),
('chiara_bruni', 107, '2026-01-25 21:00:00', 'Smart Speaker', 1900);
