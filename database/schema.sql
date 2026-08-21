CREATE DATABASE IF NOT EXISTS soundwave;
USE soundwave;

CREATE TABLE IF NOT EXISTS Utenti (
    Username VARCHAR(50) PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL,
    Cognome VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    DataNascita DATE NOT NULL,
    Paese VARCHAR(50),
    CreditoBonus INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Artisti (
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
);

CREATE TABLE IF NOT EXISTS Promozioni (
);

CREATE TABLE IF NOT EXISTS CodiciInvito (
);

CREATE TABLE IF NOT EXISTS Sottoscrizioni (
);

CREATE TABLE IF NOT EXISTS Transazioni (
);

CREATE TABLE IF NOT EXISTS Album (
);

CREATE TABLE IF NOT EXISTS Brani (
);

CREATE TABLE IF NOT EXISTS Generi (
);

CREATE TABLE IF NOT EXISTS Recensioni (
);

CREATE TABLE IF NOT EXISTS Cantare (
);

CREATE TABLE IF NOT EXISTS Appartenenze (
);

CREATE TABLE IF NOT EXISTS Inclusione (
);

CREATE TABLE IF NOT EXISTS LikeBrani (
);

CREATE TABLE IF NOT EXISTS Follow (
);

CREATE TABLE IF NOT EXISTS ValiditaPromozioni (
);