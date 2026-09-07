package soundwave.model;

import soundwave.data.Podcast;
import soundwave.data.Promotion;
import soundwave.data.Queries;
import soundwave.data.SongInput;
import soundwave.data.Subscription;
import soundwave.data.User;
import soundwave.data.Playlist;
import soundwave.data.Album;
import soundwave.data.Artist;
import soundwave.data.DAOException;
import soundwave.data.DAOUtils;
import soundwave.data.Episode;
import soundwave.data.Follow;
import soundwave.data.Genre;
import soundwave.data.InviteCode;
import soundwave.data.LikeBrani;
import soundwave.data.ListeningEvent;
import soundwave.data.Plan;
import soundwave.data.Review;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Implementation of the {@link Model} interface backed by a relational database
 * using JDBC data access objects.
 */
public final class DBModel implements Model {

    private static final Logger LOGGER = Logger.getLogger(DBModel.class.getName());

    private final Connection connection;

    /**
     * Constructs a new DBModel instance with the given database connection.
     *
     * @param connection the active database connection.
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2", 
        justification = "The database connection is managed externally and cannot be defensively copied."
    )
    public DBModel(final Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
    }

    @Override
    public User findUser(final String username) {
        try {
            return User.DAO.find(this.connection, username).orElse(null);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to find user: " + username, e);
            return null;
        }
    }

    @Override
    public void insertPromotion(final String code, final String name, final String description, 
                                final LocalDate startDate, final LocalDate endDate, final String discountType, 
                                final double discountValue, final Integer requiredMonths, final List<Integer> planCodes) {
        Promotion.DAO.insertPromotion(connection, code, name, description, startDate, endDate, 
                                             discountType, discountValue, requiredMonths, planCodes);
    }

    @Override
    public void renewSubscriptionNow(final String username, final int subscriptionCode) {
        // Verifica che la sottoscrizione sia attiva
        try (var stmt = DAOUtils.prepare(connection, Queries.CHECK_ACTIVE_SUBSCRIPTION, username);
            var rs = stmt.executeQuery()) {
            if (!rs.next()) {
                throw new DAOException("Nessuna sottoscrizione attiva trovata.");
            }
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
        
        // Simula pagamento (per ora sempre successo)
        // In futuro si può integrare con un sistema di pagamento reale
        boolean paymentSuccess = true;
        Subscription.DAO.renew(connection, subscriptionCode, "Carta di Credito", paymentSuccess);
    }

    @Override
    public void toggleAutoRenew(final String username, final int subscriptionCode, 
                            final boolean enabled) {
        // Sceglie la query giusta in base al valore enabled
        String query = enabled ? Queries.ENABLE_RENEWAL : Queries.CANCEL_RENEWAL;
        try (var stmt = DAOUtils.prepare(connection, query, subscriptionCode)) {
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Sottoscrizione non trovata o non attiva.");
            }
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean getAutoRenewStatus(final String username, final int subscriptionCode) {
        try (var stmt = DAOUtils.prepare(connection, Queries.CHECK_AUTO_RENEW_STATUS, subscriptionCode);
            var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean("RinnovoAutomatico");
            }
            throw new DAOException("Sottoscrizione non trovata.");
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
    }

    public int[] processAutoRenewals() {
        int renewed = 0;
        int failed = 0;
        int expired = 0;

        try {
            // 1. Trova le sottoscrizioni da rinnovare
            try (var stmt = connection.createStatement();
                var rs = stmt.executeQuery(Queries.FIND_AUTO_RENEWALS)) {
                
                while (rs.next()) {
                    int subCode = rs.getInt("CodiceSottoscrizione");
                    String username = rs.getString("Username");

                    System.out.println("Rinnovo automatico per sub #" + subCode + 
                                    " (" + username );

                    // 2. Simula il pagamento (90% di successo per test)
                    boolean paymentSuccess = Math.random() < 0.0;

                    Subscription.DAO.renew(connection, subCode, username, paymentSuccess);
                    
                    if (paymentSuccess) {
                        renewed++;
                        System.out.println("Rinnovo automatico completato per sub #" + subCode);
                    } else {
                        failed++;
                        System.out.println("Rinnovo automatico FALLITO per sub #" + subCode);
                        //3. Se fallisce, scade immediatamente la sottoscrizione
                        try (var expireStmt = DAOUtils.prepare(connection, Queries.EXPIRE_SUBSCRIPTION, subCode)) {
                            expireStmt.executeUpdate();
                        }
                        System.out.println("Sottoscrizione #" + subCode + " portata a Scaduta");
                    }
                }
                
            }
            
            // 4. Marca come scadute le altre sottoscrizioni senza rinnovo
            try (var stmt = connection.createStatement()) {
                expired = stmt.executeUpdate(Queries.EXPIRE_EXPIRED_SUBSCRIPTIONS);
            }
            
            System.out.println("completata: " + renewed + " rinnovate, " + failed + " fallite, " + expired + " scadute");
            
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
        
        return new int[]{renewed, failed, expired};




    }







    @Override
    public int insertArtist(final String stageName, final String name, final String surname, 
                            final LocalDate birthDate, final String provenanceCountry, 
                            final String biography, final int startYear, final String artistType) {
        return Artist.DAO.insert(this.connection, stageName, name, surname, birthDate, 
                                 provenanceCountry, biography, startYear, artistType);
    }

    @Override
    public int insertAlbumWithSongs(final int artistCode, final String title, final String releaseDate,
                                    final String recordCompany, final List<SongInput> songs) {
        return Album.DAO.insertAlbumWithSongs(this.connection, artistCode, title, releaseDate, recordCompany, songs);
    }

    @Override
    public List<Artist> getAlbumArtists() {
        return Artist.DAO.getAlbumArtists(this.connection);
    }

    @Override
    public List<Artist> getPodcastAuthors() {
        return Artist.DAO.getPodcastAuthors(this.connection);
    }

    @Override
    public boolean isPodcastAuthor(final int artistCode) {
        return Artist.DAO.isPodcastAuthor(this.connection, artistCode);
    }

    @Override
    public int insertPodcast(final int artistCode, final String name, final String description, final String category) {
        return Podcast.DAO.insert(connection, artistCode, name, description, category);
    }

    @Override
    public List<Podcast> getPodcasts() {
        return Podcast.DAO.selectAll(this.connection);
    }

    @Override
    public int insertEpisode(final int podcastCode, final String title, final int duration, 
                           final String description, final int episodeNumber) {
        return Episode.DAO.insert(connection, podcastCode, title, duration, description, episodeNumber);
    }

    @Override
    public int insertPlaylist(final String username, final String playlistName, final String visibility, 
                                final boolean isCollaborative) {
        return Playlist.DAO.insert(connection, username, playlistName, visibility, isCollaborative);
    }

    @Override
    public List<Playlist> getUserPlaylists(final String username) {
        return Playlist.DAO.getUserPlaylists(this.connection, username);
    }

    @Override
    public boolean addTrackToPlaylist(final String username, final int playlistCode, final int trackCode) {
        return Playlist.DAO.addTrackWithPermission(this.connection, username, playlistCode, trackCode);
    }

    @Override
    public boolean removeTrackFromPlaylist(final String username, final int playlistCode, final int trackCode) {
        return Playlist.DAO.removeTrack(this.connection, username, playlistCode, trackCode);
    }

    @Override
    public List<LikeBrani> getLikedTracks(final String username) {
        return LikeBrani.DAO.getLikedTracks(this.connection, username);
    }

    @Override
    public List<String> getFollowedArtists(final String username) {
        final List<String> artists = new ArrayList<>();
        try (var stmt = DAOUtils.prepare(connection, Queries.SELECT_FOLLOWED_ARTISTS_BY_USER, username);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                artists.add(rs.getString("NomeDArte"));
            }
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
        return artists;
    }

    @Override
    public void likeTrack(final String username, final int trackCode) {
        LikeBrani.DAO.likeTrack(this.connection, username, trackCode);
    }

    @Override
    public boolean unlikeTrack(final String username, final int trackCode) {
        return LikeBrani.DAO.unlikeTrack(this.connection, username, trackCode);
    }

    @Override
    public void insertListeningEvent(final String username, final int contentCode, final String device, final int eventDuration) {
        ListeningEvent.DAO.insert(connection, username, contentCode, device, eventDuration);
    }

    @Override
    public List<User> loadUsers() {
        try {
            return User.DAO.list(this.connection);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to load users from the database.", e);
            return List.of();
        }
    }

    @Override
    public Object[] getPersonalTotals(final String username, final int year) {
        try (var stmt = DAOUtils.prepare(connection, Queries.SELECT_PERSONAL_TOTALS_YEAR, username, year);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                final int totalListens = rs.getInt("TotaleAscolti");
                final int totalSeconds = rs.getInt("TotaleSecondi"); // Gestisce anche eventuali NULL se non ci sono ascolti
                return new Object[]{totalListens, totalSeconds};
            }
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
        return new Object[]{0, 0};
    }

    @Override
    public List<Object[]> getPersonalTopTracks(final String username, final int year) {
        final List<Object[]> tracks = new ArrayList<>();
        try (var stmt = DAOUtils.prepare(connection, Queries.SELECT_PERSONAL_TOP_TRACKS, username, year);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                tracks.add(new Object[]{
                    rs.getInt("CodiceContenuto"),
                    rs.getString("Titolo"),
                    rs.getInt("NumeroAscolti")
                });
            }
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
        return tracks;
    }

    @Override
    public List<Object[]> getPersonalTopArtists(final String username, final int year) {
        final List<Object[]> artists = new ArrayList<>();
        // Nota: La query ha due COUNT/UNION con l'username, quindi passiamo l'username due volte seguito dall'anno
        try (var stmt = DAOUtils.prepare(connection, Queries.SELECT_PERSONAL_TOP_ARTISTS, username, username, year);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                artists.add(new Object[]{
                    rs.getInt("CodiceArtista"),
                    rs.getString("NomeDArte"),
                    rs.getInt("NumeroAscolti")
                });
            }
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
        return artists;
    }

    @Override
    public String getPersonalTopGenre(final String username, final int year) {
        try (var stmt = DAOUtils.prepare(connection, Queries.SELECT_PERSONAL_TOP_GENRE, username, year);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("NomeGenere");
            }
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
        return "-";
    }

    @Override
    public String getMostPlayedArtist(final int year) {
        return Artist.DAO.getMostPlayedArtist(this.connection, year);
    }

    @Override
    public String getMostPlayedGenre(final int year) {
        return Genre.DAO.getMostPlayedGenre(this.connection, year);
    }

    @Override
    public List<String> getUsersAboveAverageListens(final int year) {
        return User.DAO.getUsersAboveAverageListens(this.connection, year);
    }

    @Override
    public List<String> getAlbumsAboveGlobalAverage() {
        return Album.DAO.getAlbumsAboveGlobalAverage(this.connection);
    }

    @Override
    public List<Plan> getSubscriptioPlans() {
        return Plan.DAO.listAll(connection);
    }

    @Override
    public int activateSubscription(final String username, final int planCode, final String paymentMethod, 
                                    final String promoCode, final String inviteCode, final boolean autoRenew) {

        if (planCode <= 0) {
            throw new DAOException("Piano di abbonamento non valido");
        }
        
        //caso 1: attivazione con codice promozionale
        if (promoCode != null && !promoCode.trim().isEmpty()) {
            return Subscription.DAO.insertWithPromotion(connection, username, planCode,
                promoCode.trim(), autoRenew, paymentMethod);
        }
        //caso 2: attivazione con codice invito  
        if (inviteCode != null && !inviteCode.trim().isEmpty()) {
            return Subscription.DAO.insertWithInvite(connection, username, planCode, inviteCode.trim(), autoRenew, paymentMethod);
        }
        //caso 3: attivazione standard
        return Subscription.DAO.insertStandard(connection, username, planCode, autoRenew, paymentMethod);
    }

    @Override
    public boolean verifyInviteCode(final String inviteCode) {
        return InviteCode.DAO.exists(connection, inviteCode);
    }

    @Override
    public Object[] verifyPromotionCode(final String promoCode, final int planCode) {
        try (var stmt = DAOUtils.prepare(connection, Queries.CHECK_PROMOTION_VALIDITY, promoCode, planCode);
            var rs = stmt.executeQuery()) {
            if (rs.next()) {
                final String discountType = rs.getString("TipoSconto");
                final double discountValue = rs.getDouble("ValoreSconto");
                final double originalPrice = rs.getDouble("Costo");
                return new Object[]{true, discountValue, discountType, originalPrice};
            }
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
        return new Object[]{false, 0, null, 0};
    }

    @Override
    public List<Object[]> getSubscriptionData(final String username) {
        final List<Object[]> subscriptions = new ArrayList<>();
        
        try (var stmt = DAOUtils.prepare(connection, Queries.SELECT_SUBSCRIPTIONS_WITH_TRANSACTIONS, username);
            var rs = stmt.executeQuery()) {
            
            int currentSub = -1; //corrente sottoscrizione
            List<String> transactions = new ArrayList<>();
            Object[] currentData = null;
            
            while (rs.next()) {
                final int subCode = rs.getInt("CodiceSottoscrizione");
                
                if (currentSub != subCode) {
                    if (currentData != null) {
                        currentData[8] = new ArrayList<>(transactions);
                        subscriptions.add(currentData);
                    }
                    
                    currentSub = subCode;
                    transactions = new ArrayList<>();
                    
                    currentData = new Object[] {
                        subCode,
                        rs.getString("TipoAbbonamento"),
                        rs.getDate("DataInizio") != null ? rs.getDate("DataInizio").toString() : "-",
                        rs.getDate("DataFine") != null ? rs.getDate("DataFine").toString() : "-",
                        rs.getString("StatoSottoscrizione"),
                        rs.getBoolean("RinnovoAutomatico"),
                        rs.getString("CodicePromozione"),
                        rs.getString("CodiceInvito"),
                        transactions
                    };
                }
                
                if (rs.getObject("CodiceTransazione") != null) {
                    final String trans = String.format("%s | €%.2f | %s",
                        rs.getTimestamp("DataTransazione") != null ? rs.getTimestamp("DataTransazione").toString() : "-",
                        rs.getDouble("Importo"),
                        rs.getString("StatoTransazione") != null ? rs.getString("StatoTransazione") : "-"
                    );
                    transactions.add(trans);
                }
            }
            
            if (currentData != null) {
                currentData[8] = new ArrayList<>(transactions);
                subscriptions.add(currentData);
            }
            
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
        return subscriptions;
    }

    @Override
    public void addLike(final String username, final int contentCode) {
        soundwave.data.Like.DAO.addLike(this.connection, username, contentCode);
    }

    @Override
    public List<String> getLikedSongs(final String username) {
        return soundwave.data.Like.DAO.getLikedSongs(this.connection, username);
    }

    @Override
    public List<String> getSongsByGenre(final String genre) {
        return soundwave.data.Genre.DAO.getSongsByGenre(this.connection, genre);
    }

    @Override
    public void removeLike(final String username, final int contentCode) {
        soundwave.data.Like.DAO.removeLike(this.connection, username, contentCode);
    }

    @Override
    public List<Artist> getArtistsByPartialName(final String query) {
        return soundwave.data.Artist.DAO.getByPartialStageName(this.connection, query);
    }

    @Override
    public Artist getArtistByCode(final int artistCode) throws DAOException {
        return Artist.DAO.getByCode(this.connection, artistCode);
    }

    /* --- Implementazione dei nuovi metodi richiesti dall'interfaccia Model --- */

    @Override
    public List<Album> getAlbumsByPartialTitle(final String query) {
        return Album.DAO.getByPartialTitle(this.connection, query);
    }

    @Override
    public Album.DAO.AlbumWithSongs getAlbumWithSongs(final int albumCode) {
        return Album.DAO.getAlbumWithSongs(this.connection, albumCode);
    }

    @Override
    public List<String> getAlbumReviews(final int albumCode) {
        final List<Review> reviews = Review.DAO.getReviewsForAlbum(this.connection, albumCode);
        final List<String> reviewStrings = new ArrayList<>();
        for (final Review r : reviews) {
            reviewStrings.add("Utente: " + r.getUsername() + " - Voto: " + r.getRating() + " - Commento: " + r.getComment());
        }
        return reviewStrings;
    }

    @Override
    public void insertOrUpdateReview(final String username, final int albumCode, final int rating, final String comment) {
        Review.DAO.insertOrUpdate(this.connection, username, albumCode, rating, comment);
    }

    @Override
    public void followArtist(final String username, final int artistCode) {
        Follow.DAO.followArtist(this.connection, username, artistCode, java.time.LocalDate.now());
    }

    @Override 
    public int getBonusCredits(final String username) {
        return User.DAO.getBonusCredit(this.connection, username);
    }

    @Override
    public int redeemBonus(final String username, final int planCode, final boolean autoRenew) {
        // Verifica che il piano sia mensile
        if (!Plan.DAO.isMonthlyPlan(connection, planCode)) {
            throw new DAOException("Il riscatto con crediti bonus è disponibile solo per piani mensili.");
        }
        
        // Verifica crediti bonus
        if (!User.DAO.hasEnoughBonusCredit(connection, username)) {
            throw new DAOException("Crediti bonus insufficienti. Servono almeno 2 crediti.");
        }
        
        // Se l'utente ha già una sottoscrizione attiva, rinnova
        // Altrimenti crea una nuova sottoscrizione
        try (var stmt = DAOUtils.prepare(connection, Queries.CHECK_ACTIVE_SUBSCRIPTION, username);
            var rs = stmt.executeQuery()) {
            if (rs.next()) {
                // 4.2 - Rinnovo con crediti bonus
                int subscriptionCode = rs.getInt("CodiceSottoscrizione");
                Subscription.DAO.renewWithBonus(connection, username, subscriptionCode);
                return subscriptionCode;
            } else {
                // 4.1 - Nuova sottoscrizione con crediti bonus
                return Subscription.DAO.redeemBonusForNew(connection, username, planCode, autoRenew);
            }
        } catch (final SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public String registerUser(final String username, final String name, final String surname,
                            final String email, final String password, 
                            final LocalDate birthDate, final String country) {
        return User.DAO.register(connection, username, name, surname, email, password, birthDate, country);
    }
}
