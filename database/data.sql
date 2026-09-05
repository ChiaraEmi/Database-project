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
('claire_d', 'Claire', 'Dubois', 'claire.dubois@email.com', 'hash_pass6', '1995-11-03', 'Francia', 5),
('davide_99', 'Davide', 'Neri', 'davide.neri@email.com', 'hash_pass7', '1999-04-14', 'Italia', 15),
('sofia_b', 'Sofia', 'Conti', 'sofia.conti@email.com', 'hash_pass8', '2002-12-05', 'Italia', 0),
('marco_p', 'Marco', 'Piras', 'marco.piras@email.com', 'hash_pass9', '1997-06-19', 'Italia', 0),
('federica_m', 'Federica', 'Mancini', 'federica.mancini@email.com', 'hash_pass10', '2003-02-11', 'Italia', 10),
('luca_t', 'Luca', 'Trevisan', 'luca.trevisan@email.com', 'hash_pass11', '1994-09-25', 'Italia', 0),
('martina_r', 'Martina', 'Riva', 'martina.riva@email.com', 'hash_pass12', '2000-10-04', 'Italia', 5);

-- 2. ARTISTI
INSERT INTO Artisti (CodiceArtista, NomeDArte, Nome, Cognome, DataNascita, PaeseProvenienza, Biografia, AnnoInizioAttivita, TipoArtista) VALUES
(1, 'Elodie', 'Elodie', 'Di Patrizi', '1990-05-03', 'Italia', 'Cantante pop italiana', 2015, 'Cantante'),
(2, 'Måneskin', NULL, NULL, NULL, 'Italia', 'Rock band di fama internazionale', 2016, 'Band'),
(3, 'Marco Mengoni', 'Marco', 'Mengoni', '1988-12-25', 'Italia', 'Vincitore di Sanremo', 2009, 'Cantante'),
(4, 'Gianluca Gazzoli', 'Gianluca', 'Gazzoli', '1988-08-18', 'Italia', 'Host del podcast BSMT', 2014, 'Autore Podcast'),
(5, 'The Weeknd', 'Abel', 'Tesfaye', '1990-02-16', 'Canada', 'Artista R&B e Pop di successo globale', 2010, 'Cantante'),
(6, 'Lex Fridman', 'Lex', 'Fridman', '1983-08-15', 'USA', 'Host del Lex Fridman Podcast', 2018, 'Autore Podcast'),
(7, 'Cesare Cremonini', 'Cesare', 'Cremonini', '1980-03-27', 'Italia', 'Cantautore storico italiano', 1999, 'Cantante'),
(8, 'Dua Lipa', 'Dua', 'Lipa', '1995-08-22', 'Regno Unito', 'Pop star internazionale', 2015, 'Cantante'),
(9, 'Pinguini Tattici Nucleari', NULL, NULL, NULL, 'Italia', 'Band pop-rock italiana', 2010, 'Band'),
(10, 'Fabri Fibra', 'Fabrizio', 'Tarducci', '1976-10-17', 'Italia', 'Pioniere del rap italiano', 1996, 'Cantante');

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
(1, 'Promo Estate 2026', 'Sconto del 20% sul piano annuale', '2026-06-01', '2026-08-31', 'Percentuale', 20.00, 12),
(2, 'Benvenuto', 'Sconto fisso di 5 Euro', '2026-01-01', '2026-12-31', 'Fisso', 5.00, 1),
(3, 'Black Friday', 'Sconto del 50% sul piano annuale', '2026-11-20', '2026-11-30', 'Percentuale', 50.00, 12),
(4, 'Student Promo', 'Sconto fisso speciale studenti', '2026-01-01', '2026-12-31', 'Fisso', 2.00, 1);

-- 6. CONTENUTI (Brani ed Episodi)
INSERT INTO Contenuti (CodiceContenuto, Titolo, Durata, Descrizione, DataPubblicazione, TipoContenuto) VALUES
-- Brani
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
-- Episodi Podcast
(6, 'Episodio 1 - Ospite Speciale', 3600, 'Intervista esclusiva', '2026-03-15', 'Episodio'),
(7, 'Episodio 1 - Introduzione all AI', 2700, 'Chiacchierata informale', '2026-03-22', 'Episodio'),
(12, 'Episodio 2 - Dietro le quinte della musica', 3100, 'Seconda puntata speciale', '2026-03-29', 'Episodio'),
(16, 'Episodio 2 - Futuro della Robotica', 3400, 'Discussione avanzata con esperti', '2026-04-05', 'Episodio');

-- 7. PODCAST
INSERT INTO Podcast (CodicePodcast, CodiceArtista, NomePodcast, DescrizionePodcast, Categoria) VALUES
(1, 4, 'Passa dal BSMT', 'Interviste a personaggi del mondo dello spettacolo', 'Intrattenimento'),
(2, 6, 'Lex Fridman Podcast', 'Conversazioni su AI, scienza e tecnologia', 'Tecnologia');

-- 8. EPISODI
INSERT INTO Episodi (CodiceEpisodio, CodicePodcast, NumeroEpisodio) VALUES
(6, 1, 1),
(12, 1, 2),
(7, 2, 1),
(16, 2, 2);

-- 9. ALBUM
INSERT INTO Album (CodiceAlbum, CodiceArtista, TitoloAlbum, DataPubblicazione, CasaDiscografica, MediaVoti, DurataTotale) VALUES
(101, 1, 'OK. Respira', '2023-07-11', 'Island Records', 8.50, 345),
(102, 2, 'RUSH!', '2023-12-01', 'Epic Records', 9.10, 441),
(103, 3, 'Materia (Pelle)', '2022-08-03', 'Epic Records', 8.80, 225),
(104, 7, 'Logico', '2014-05-27', 'Pressing Line', 9.00, 255),
(105, 8, 'Future Nostalgia', '2020-03-27', 'Warner Records', 9.50, 203),
(106, 5, 'After Hours', '2020-03-20', 'Republic Records', 9.60, 200),
(107, 9, 'Ah dieren', '2019-04-12', 'Sony Music', 8.90, 395),
(108, 10, 'Fenomeno', '2017-03-31', 'Universal Music', 9.20, 220);

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
('mario88', 101, 9, 'Album pop fantastico e moderno.', '2026-02-10'),
('luisa_g', 102, 10, 'Energia pura, il miglior album rock dell anno!', '2026-02-15'),
('davide_99', 105, 10, 'Capolavoro pop moderno!', '2026-02-20'),
('sofia_b', 104, 8, 'Molto orecchiabile e nostalgico.', '2026-02-22'),
('mario88', 106, 10, 'Un capolavoro R&B imperdibile.', '2026-02-25'),
('elena_v', 107, 9, 'Grandi classici indie italiani.', '2026-03-01'),
('alex_smith', 108, 9, 'Testi profondi e grandi basi.', '2026-03-02');

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
(1, 2),
(2, 1),
(3, 2),
(4, 4);

-- 21. SOTTOSCRIZIONI
INSERT INTO Sottoscrizioni (CodiceSottoscrizione, Username, CodiceAbbonamento, CodicePromozione, CodiceInvito, DataInizio, DataFine, Stato, RinnovoAutomatico) VALUES
(1, 'mario88', 2, 1, 'INV_MARIO', '2026-01-01', '2027-01-01', 'Attiva', TRUE),
(2, 'davide_99', 1, 2, 'INV_DAVIDE', '2026-02-01', '2026-03-01', 'Attiva', FALSE),
(3, 'sofia_b', 4, 4, 'INV_SOFIA', '2026-02-10', '2026-03-10', 'Attiva', TRUE);

-- 22. TRANSAZIONI
INSERT INTO Transazioni (CodiceTransazione, CodiceSottoscrizione, Data, Importo, MetodoPagamento, Stato) VALUES
(1, 1, '2026-01-01 10:00:00', 71.99, 'Carta di Credito', 'Completata'),
(2, 2, '2026-02-01 12:15:00', 4.99, 'PayPal', 'Completata'),
(3, 3, '2026-02-10 14:00:00', 2.99, 'Apple Pay', 'Completata');

-- 23. EVENTI ASCOLTO
INSERT INTO EventiAscolto (Username, CodiceContenuto, DataOra, Dispositivo, DurataEvento) VALUES
-- Anno 2024
('mario88', 1, '2024-02-10 14:20:00', 'Smartphone Android', 165),
('mario88', 3, '2024-04-12 18:30:00', 'Smartphone Android', 194),
('mario88', 8, '2024-06-20 21:10:00', 'MacBook', 255),
('mario88', 10, '2024-10-05 12:00:00', 'Smart Speaker', 200),
('davide_99', 9, '2024-03-15 09:00:00', 'iPhone', 203),
('davide_99', 5, '2024-07-22 15:45:00', 'iPhone', 225),
('davide_99', 2, '2024-09-11 20:30:00', 'iPhone', 180),
('luisa_g', 3, '2024-08-15 21:10:00', 'iPhone', 194),
('giovanni_k', 5, '2024-11-20 18:00:00', 'Desktop Windows', 225),
('elena_v', 1, '2024-01-18 11:20:00', 'MacBook', 165),
('claire_d', 10, '2024-12-01 19:00:00', 'Smartphone Android', 200),

-- Anno 2025
('mario88', 1, '2025-01-10 10:15:00', 'Smartphone Android', 165),
('mario88', 2, '2025-03-22 14:30:00', 'Smartphone Android', 180),
('mario88', 9, '2025-05-18 21:00:00', 'MacBook', 203),
('mario88', 10, '2025-08-12 11:20:00', 'Smart Speaker', 200),
('elena_v', 2, '2025-02-14 10:00:00', 'MacBook', 180),
('elena_v', 4, '2025-04-19 19:30:00', 'iPhone', 247),
('alex_smith', 9, '2025-06-30 22:15:00', 'Smart Speaker', 203),
('claire_d', 10, '2025-09-05 16:45:00', 'Smartphone Android', 200),
('davide_99', 8, '2025-10-11 15:10:00', 'iPhone', 255),
('sofia_b', 3, '2025-11-05 18:40:00', 'Desktop Windows', 194),
('mario88', 8, '2025-12-31 23:50:00', 'MacBook', 255),

-- Anno 2026
('mario88', 1, '2026-03-01 15:30:00', 'Smartphone Android', 165),
('mario88', 3, '2026-03-01 15:35:00', 'Smartphone Android', 194),
('luisa_g', 6, '2026-03-02 18:00:00', 'Desktop Windows', 3600),
('luisa_g', 12, '2026-03-02 19:10:00', 'Desktop Windows', 3100),
('davide_99', 9, '2026-03-03 09:15:00', 'iPhone', 203),
('sofia_b', 8, '2026-03-03 14:00:00', 'MacBook', 255),
('mario88', 10, '2026-03-03 21:00:00', 'Smart Speaker', 200),
('giovanni_k', 7, '2026-04-10 11:00:00', 'Smartphone Android', 2700),
('marco_p', 1, '2026-04-12 12:00:00', 'iPhone', 165),
('federica_m', 2, '2026-04-15 16:20:00', 'Smartphone Android', 180),
('marco_p', 3, '2026-04-16 18:00:00', 'iPhone', 194),
('luca_t', 13, '2026-05-01 10:00:00', 'Desktop Windows', 185),
('martina_r', 14, '2026-05-02 15:00:00', 'Smartphone Android', 210),
('luca_t', 15, '2026-05-03 18:30:00', 'iPhone', 220);
