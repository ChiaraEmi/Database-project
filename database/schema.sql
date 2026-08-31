CREATE DATABASE IF NOT EXISTS soundwave;
USE soundwave;

CREATE TABLE IF NOT EXISTS Utenti (
    Username VARCHAR(50) PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL,
    Cognome VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    DataNascita DATE NOT NULL,
    Paese VARCHAR(50) NOT NULL,
<<<<<<< HEAD
    CreditoBonus INT DEFAULT 0
=======
    CreditoBonus INT NOT NULL DEFAULT 0
>>>>>>> origin/main
);

CREATE TABLE IF NOT EXISTS Artisti (
    CodiceArtista INT AUTO_INCREMENT PRIMARY KEY,
    NomeDArte VARCHAR(100) NOT NULL,
    Nome VARCHAR(50),
    Cognome VARCHAR(50),
    DataNascita DATE NOT NULL,
    PaeseProvenienza VARCHAR(50) NOT NULL,
    Biografia TEXT,
    AnnoInizioAttivita YEAR NOT NULL,
    TipoArtista ENUM('Cantante', 'Autore Podcast') NOT NULL
);

CREATE TABLE IF NOT EXISTS Contenuti (
    CodiceContenuto INT AUTO_INCREMENT PRIMARY KEY,
    Titolo VARCHAR(150) NOT NULL,
    Durata INT NOT NULL, -- Espresso in secondi
    Descrizione TEXT,
    DataPubblicazione DATE NOT NULL,
    TipoContenuto ENUM('Brano', 'Episodio') NOT NULL
);

CREATE TABLE IF NOT EXISTS Podcast (
    CodicePodcast INT AUTO_INCREMENT PRIMARY KEY,
    CodiceArtista INT NOT NULL,
    NomePodcast VARCHAR(100) NOT NULL,
    DescrizionePodcast TEXT,
    Categoria VARCHAR(50) NOT NULL,
    FOREIGN KEY (CodiceArtista) REFERENCES Artisti(CodiceArtista) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Episodi (
    CodiceEpisodio INT PRIMARY KEY,
    CodicePodcast INT NOT NULL,
    NumeroEpisodio INT NOT NULL,
    UNIQUE (CodicePodcast, NumeroEpisodio),
    FOREIGN KEY (CodiceEpisodio) REFERENCES Contenuti(CodiceContenuto) ON DELETE CASCADE,
    FOREIGN KEY (CodicePodcast) REFERENCES Podcast(CodicePodcast) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Playlist (
    CodicePlaylist INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL,
    NomePlaylist VARCHAR(100) NOT NULL,
    DataCreazione DATETIME DEFAULT CURRENT_TIMESTAMP,
    Visibilita ENUM('Pubblica', 'Privata') DEFAULT 'Privata',
    Collaborativa BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (Username) REFERENCES Utenti(Username) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Collaborazioni (
    CodicePlaylist INT NOT NULL,
    Username VARCHAR(50) NOT NULL,
    PRIMARY KEY (CodicePlaylist, Username),
    FOREIGN KEY (CodicePlaylist) REFERENCES Playlist(CodicePlaylist) ON DELETE CASCADE,
    FOREIGN KEY (Username) REFERENCES Utenti(Username) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS EventiAscolto (
    Username VARCHAR(50) NOT NULL,
    CodiceContenuto INT NOT NULL,
    DataOra DATETIME DEFAULT CURRENT_TIMESTAMP,
    Dispositivo VARCHAR(50),
    DurataEvento INT NOT NULL,
    PRIMARY KEY (Username, CodiceContenuto, DataOra),
    FOREIGN KEY (Username) REFERENCES Utenti(Username) ON DELETE CASCADE,
    FOREIGN KEY (CodiceContenuto) REFERENCES Contenuti(CodiceContenuto) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Abbonamenti (
    CodiceAbbonamento INT AUTO_INCREMENT PRIMARY KEY,
    TipoAbbonamento VARCHAR(50) NOT NULL,
    Durata INT NOT NULL, -- in mesi
    Costo DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS Promozioni (
    CodicePromozione INT AUTO_INCREMENT PRIMARY KEY,
    Nome VARCHAR(100) NOT NULL,
    Descrizione TEXT,
    DataInizioPromo DATE NOT NULL,
    DataFinePromo DATE NOT NULL,
    TipoSconto ENUM('Percentuale', 'Fisso') NOT NULL,
    ValoreSconto DECIMAL(10,2) NOT NULL
    MesiRichiesti INT
);

CREATE TABLE IF NOT EXISTS CodiciInvito (
    Codice VARCHAR(50) PRIMARY KEY,
    DataGenerazione DATE NOT NULL DEFAULT CURRENT_DATE,
    Username VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (Username) REFERENCES Utenti(Username) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Sottoscrizioni (
    CodiceSottoscrizione INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL,
    CodiceAbbonamento INT NOT NULL,
    CodicePromozione INT,
    CodiceInvito VARCHAR(50),
    DataInizio DATE NOT NULL,
    DataFine DATE NOT NULL,
    Stato ENUM('Attiva', 'Scaduta') NOT NULL,
    RinnovoAutomatico BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (Username) REFERENCES Utenti(Username), 
    FOREIGN KEY (CodiceAbbonamento) REFERENCES Abbonamenti(CodiceAbbonamento),
    FOREIGN KEY (CodicePromozione) REFERENCES Promozioni(CodicePromozione),
    FOREIGN KEY (CodiceInvito) REFERENCES CodiciInvito(Codice)
);

CREATE TABLE IF NOT EXISTS Transazioni (
    CodiceTransazione INT AUTO_INCREMENT PRIMARY KEY,
    CodiceSottoscrizione INT NOT NULL,
    Data DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Importo DECIMAL(10,2) NOT NULL,
    MetodoPagamento VARCHAR(50) NOT NULL,
    Stato ENUM('Completata', 'Fallita') NOT NULL,

    Foreign KEY (CodiceSottoscrizione) REFERENCES Sottoscrizioni(CodiceSottoscrizione)
);

CREATE TABLE IF NOT EXISTS Album (
    CodiceAlbum INT AUTO_INCREMENT PRIMARY KEY,
    CodiceArtista INT NOT NULL,
    TitoloAlbum VARCHAR(100) NOT NULL,
    AnnoPubblicazione YEAR NOT NULL,
    CasaDiscografica VARCHAR(50) NOT NULL,
    MediaVoti DECIMAL(4,2) DEFAULT 0.00,
    DurataTotale INT DEFAULT 0, --secondi
    UNIQUE(CodiceArtista, TitoloAlbum),
    FOREIGN KEY (CodiceArtista) REFERENCES Artisti(CodiceArtista) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Brani (
    CodiceBrano INT PRIMARY KEY,
    CodiceAlbum INT NOT NULL,
    NumeroTraccia INT NOT NULL,
    UNIQUE(CodiceAlbum,NumeroTraccia),
    FOREIGN KEY (CodiceBrano) REFERENCES Contenuti(CodiceContenuto) ON DELETE CASCADE,
    FOREIGN KEY (CodiceAlbum) REFERENCES Album(CodiceAlbum) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Generi (
    NomeGenere VARCHAR(100) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Recensioni (
    Username VARCHAR(50) NOT NULL,
    CodiceAlbum INT NOT NULL,
    Voto INT NOT NULL CHECK (Voto BETWEEN 1 AND 10),
    Commento VARCHAR(150),
    DataRecensione DATE DEFAULT (CURRENT_DATE),
    PRIMARY KEY(Username, CodiceAlbum),
    FOREIGN KEY (Username) REFERENCES Utenti(Username) ON DELETE CASCADE,
    FOREIGN KEY (CodiceAlbum) REFERENCES Album(CodiceAlbum) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Cantare (
    CodiceArtista INT NOT NULL,
    CodiceBrano INT NOT NULL,
    PRIMARY KEY (CodiceArtista, CodiceBrano),
    FOREIGN KEY (CodiceArtista) REFERENCES Artisti(CodiceArtista) ON DELETE CASCADE,
    FOREIGN KEY (CodiceBrano) REFERENCES Brani(CodiceBrano) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Appartenenze (
    CodiceBrano INT NOT NULL,
    NomeGenere VARCHAR(100) NOT NULL,
    PRIMARY KEY (CodiceBrano, NomeGenere),
    FOREIGN KEY (CodiceBrano) REFERENCES Brani(CodiceBrano) ON DELETE CASCADE,
    FOREIGN KEY (NomeGenere) REFERENCES Generi(NomeGenere) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Inclusione (
);

CREATE TABLE IF NOT EXISTS LikeBrani (
    Username VARCHAR(50) NOT NULL,
    CodiceBrano INT NOT NULL,
    PRIMARY KEY (Username, CodiceBrano),
    FOREIGN KEY (Username) REFERENCES Utenti(Username) ON DELETE CASCADE,
    FOREIGN KEY (CodiceBrano) REFERENCES Brani(CodiceBrano) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Follow (
    Username VARCHAR(50) NOT NULL,
    CodiceArtista INT NOT NULL,
    DataInizio DATE NOT NULL,
    DataFine DATE,
    PRIMARY KEY (Username, CodiceArtista),
    FOREIGN KEY (Username) REFERENCES Utenti(Username) ON DELETE CASCADE,
    FOREIGN KEY (CodiceArtista) REFERENCES Artisti(CodiceArtista) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ValiditaPromozioni (
    CodicePromozione INT NOT NULL,
    CodiceAbbonamento INT NOT NULL,
    PRIMARY KEY (CodicePromozione, CodiceAbbonamento),
    FOREIGN KEY (CodicePromozione) REFERENCES Promozioni(CodicePromozione) ON DELETE CASCADE,
    FOREIGN KEY (CodiceAbbonamento) REFERENCES Abbonamenti(CodiceAbbonamento) ON DELETE CASCADE
);