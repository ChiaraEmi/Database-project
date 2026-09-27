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
('INV_MARIO', '2024-03-15', 'mario_rossi'),
('INV_LAURA', '2025-01-10', 'laura_bianchi'),
('INV_GIOVANNI', '2024-05-10', 'giovanni_verdi'),
('INV_ANNA', '2026-03-02', 'anna_neri'),
('INV_LUCA', '2024-04-12', 'luca_gialli'),
('INV_SARA', '2025-11-20', 'sara_romano'),
('INV_DAVIDE', '2024-04-20', 'davide_colombo'),
('INV_ELENA', '2024-05-30', 'elena_ferrari'),
('INV_MARCO', '2024-03-18', 'marco_conte'),
('INV_CHIARA', '2026-03-02', 'chiara_bruni');

-- -----------------------------------------------------
-- 10. SOTTOSCRIZIONI E TRANSAZIONI
-- -----------------------------------------------------
INSERT INTO Sottoscrizioni (CodiceSottoscrizione, Username, CodiceAbbonamento, CodicePromozione, CodiceInvito, DataInizio, DataFine, Stato, RinnovoAutomatico) VALUES
(1, 'mario_rossi', 1, 'WELCOME', NULL, '2026-06-01', '2026-07-01', 'Scaduta', TRUE),
(2, 'laura_bianchi', 4, 'STUDENT', NULL, '2026-06-01', '2026-07-01', 'Scaduta', TRUE),
(3, 'giovanni_verdi', 3, NULL, 'INV-GIOVANNI789', '2026-06-01', '2026-07-01', 'Scaduta', FALSE);

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
