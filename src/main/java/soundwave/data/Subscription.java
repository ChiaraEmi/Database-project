package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.sql.Statement;

/**
 * Represents a subscription entity within the Soundwave system.
 */
public final class Subscription {
    private final int code;
    private final String username;
    private final int subscriptionPlanCode;
    private final Integer promotionCode;
    private final String inviteCode;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String status;
    private final boolean autoRenew;

    /**
     * Constructs a new Subscription instance.
     *
     * @param code                 the unique code of the subscription.
     * @param username             the username associated with the subscription.
     * @param subscriptionPlanCode the code of the subscription plan.
     * @param promotionCode        the code of any promotion applied (can be null).
     * @param inviteCode           the invite code used for the subscription (can be null).
     * @param startDate            the start date of the subscription.
     * @param endDate              the end date of the subscription.
     * @param status               the current status of the subscription.
     * @param autoRenew            whether the subscription is set to auto-renew.
     */
    public Subscription(final int code, final String username, final int subscriptionPlanCode, final Integer promotionCode, final String inviteCode, 
                        final LocalDate startDate, final LocalDate endDate, final String status, final boolean autoRenew) {

        this.code = Objects.requireNonNull(code, "Code can not be null");
        this.username = Objects.requireNonNull(username, "Username can not be null");
        this.subscriptionPlanCode = Objects.requireNonNull(subscriptionPlanCode, "Subscription plan code can not be null");
        this.promotionCode = promotionCode; // can be null
        this.inviteCode = inviteCode; // can be null
        this.startDate = Objects.requireNonNull(startDate, "Start date can not be null");
        this.endDate = Objects.requireNonNull(endDate, "End date can not be null");
        this.status = Objects.requireNonNull(status, "Status can not be null");
        this.autoRenew = autoRenew;
    }

    /**
     * Gets the unique code of the subscription.
     *
     * @return the subscription code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the username associated with the subscription.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the code of the subscription plan.
     *
     * @return the subscription plan code.
     */
    public int getSubscriptionPlanCode() {
        return subscriptionPlanCode;
    }

    /**
     * Gets the code of any promotion applied to the subscription.
     *
     * @return the promotion code, or null if none was applied.
     */
    public Integer getPromotionCode() {
        return promotionCode;
    }

    /**
     * Gets the invite code used for the subscription.
     *
     * @return the invite code, or null if none was used.
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Gets the start date of the subscription.
     *
     * @return the start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Gets the end date of the subscription.
     *
     * @return the end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Gets the current status of the subscription.
     *
     * @return the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Checks if the subscription is set to auto-renew.
     *
     * @return true if the subscription is set to auto-renew, false otherwise.
     */
    public boolean isAutoRenew() {
        return autoRenew;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } else if (other == null || !(other instanceof Subscription)) {
            return false;
        } 
        
        final var s = (Subscription) other;
        return this.code == s.code 
            && this.username.equals(s.username) 
            && this.subscriptionPlanCode == s.subscriptionPlanCode 
            && Objects.equals(this.promotionCode, s.promotionCode) 
            && Objects.equals(this.inviteCode, s.inviteCode) 
            && this.startDate.equals(s.startDate) 
            && this.endDate.equals(s.endDate) 
            && this.status.equals(s.status) 
            && this.autoRenew == s.autoRenew;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Subscription",
            List.of(
                Printer.field("code", this.code),
                Printer.field("username", this.username),
                Printer.field("subscriptionPlanCode", this.subscriptionPlanCode),
                Printer.field("promotionCode", this.promotionCode),
                Printer.field("inviteCode", this.inviteCode),
                Printer.field("startDate", this.startDate),
                Printer.field("endDate", this.endDate),
                Printer.field("status", this.status),
                Printer.field("autoRenew", this.autoRenew)
            )
        );
    }

    /**
     * DAO class for Subscription.
     */
    public static final class DAO {
        private DAO() {}

        /**
         * OP 2.1
         * Inserts a new standard subscription into the database.
         *
         * @param connection           the database connection.
         * @param username             the username associated with the subscription.
         * @param subscriptionPlanCode the code of the subscription plan.
         * @param autoRenew            whether the subscription should auto-renew.
         * @return the generated subscription code.
         */
        public static int insertStandard(final Connection connection, final String username, final int subscriptionPlanCode, final boolean autoRenew, final String paymentMethod) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                //1. Verifica che l'utente che non abbia già una sottoscrizione attiva
                try (var statement = DAOUtils.prepare(connection, Queries.CHECK_ACTIVE_SUBSCRIPTION, username);
                     var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        throw new DAOException("User already has an active subscription");
                    }
                }
                
                //3. Inserisce la sottoscrizione
                int subscriptionCode;
                try (var statement = DAOUtils.prepareWithKeys(connection, Queries.INSERT_SUBSCRIPTION_STANDARD, Statement.RETURN_GENERATED_KEYS, username, autoRenew, subscriptionPlanCode)) {
                    statement.executeUpdate();

                    try (var rs = statement.getGeneratedKeys()) {
                        if (rs.next()) {
                            subscriptionCode = rs.getInt(1);
                        } else {
                            throw new DAOException("Inserting subscription failed, no ID obtained.");
                        }
                    }
                }

                //4. Inserisce la transazione per il pagamento della sottoscrizione
                Transaction.DAO.insertStandard(connection, subscriptionCode, paymentMethod, subscriptionPlanCode);

                connection.commit();
                return subscriptionCode;
            } catch (final SQLException e) {
                try {
                    connection.rollback();
                } catch (final SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw new DAOException(e);
            } finally {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (final SQLException e) {
                    throw new DAOException(e);
                }
            }
        }

        /**
         * OP 2.2
         * Inserts a new subscription into the database using a promotion code.
         * @param connection 
         * @param username
         * @param subscriptionPlanCode
         * @param promotionCode
         * @param autoRenew
         * @param paymentMethod
         * @return the generated subscription code.
         * @throws DAOException if any database operation fails.
         */
        public static int insertWithPromotion(final Connection connection, final String username, final int subscriptionPlanCode, final int promotionCode, final boolean autoRenew, final String paymentMethod) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                //1. Verifica che l'utente che non abbia già una sottoscrizione attiva
                try (var statement = DAOUtils.prepare(connection, Queries.CHECK_ACTIVE_SUBSCRIPTION, username);
                     var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        throw new DAOException("User already has an active subscription");
                    }
                }
                
                //2. Verifica che il codice promozionale sia valido per il piano selezionato e calcola il prezzo scontato
                double discountedPrice = 0.0;
                try (var statement = DAOUtils.prepare(connection, Queries.CHECK_PROMOTION_VALIDITY, promotionCode, subscriptionPlanCode);
                     var resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new DAOException("Promotion code is not valid for the selected plan");
                    }

                    String discountType = resultSet.getString("TipoSconto");
                    double discountValue = resultSet.getDouble("ValoreSconto");
                    double originalPrice = resultSet.getDouble("Prezzo");

                    if ("Percentuale".equals(discountType)) {
                        discountedPrice = originalPrice * (1- discountValue / 100.0);
                    } else if ("Fisso".equals(discountType)) {
                        discountedPrice = Math.max(0, originalPrice - discountValue);
                    } 

                }

                //3. Inserisce la sottoscrizione con promozione
                int subscriptionCode;
                try (var statement = DAOUtils.prepareWithKeys(connection, Queries.INSERT_SUBSCRIPTION_PROMOTIONAL, Statement.RETURN_GENERATED_KEYS, username, autoRenew, promotionCode, subscriptionPlanCode)) {
                    statement.executeUpdate();

                    try (var rs = statement.getGeneratedKeys()) {
                        if (rs.next()) {
                            subscriptionCode = rs.getInt(1);
                        } else {
                            throw new DAOException("Inserting subscription failed, no ID obtained.");
                        }
                    }
                }

                //4. Inserisce la transazione per il pagamento della sottoscrizione
                Transaction.DAO.insertWithPromotion(connection, subscriptionCode, discountedPrice, paymentMethod);

                connection.commit();
                return subscriptionCode;
            } catch (final SQLException e) {
                try {
                    connection.rollback();
                } catch (final SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw new DAOException(e);
            } finally {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (final SQLException e) {
                    throw new DAOException(e);
                }
            }
        }

        /**
         * OP 2.3
         * Inserts a new subscription into the database using an invite code.
         * @param connection
         * @param username
         * @param subscriptionPlanCode
         * @param inviteCode
         * @param autoRenew
         * @param paymentMethod
         * @return the generated subscription code.
         */
        public static int insertWithInvite(final Connection connection, final String username, final int subscriptionPlanCode, final String inviteCode, final boolean autoRenew, final String paymentMethod) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                //1. Verifica che l'utente che non abbia già una sottoscrizione attiva
                try (var statement = DAOUtils.prepare(connection, Queries.CHECK_ACTIVE_SUBSCRIPTION, username);
                     var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        throw new DAOException("User already has an active subscription");
                    }
                }
                
                //2. Verifica se è la prima sottoscrizione dell'utente
                try (var statement = DAOUtils.prepare(connection, Queries.CHECK_IS_FIRST_SUBSCRIPTION, username);
                     var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        throw new DAOException("Invite code can be only used for the first subscription");
                    }
                }

                //3. Verifica che il codice invito esista e recupera il proprietario dell'invito
                String inviterUsername;
                try (var statement = DAOUtils.prepare(connection, Queries.CHECK_INVITECODE, inviteCode);
                     var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        inviterUsername = resultSet.getString("Username");
                    } else {
                        throw new DAOException("Invite code does not exist");
                    }
                }

                //4. Non si può usare un codice invito di un utente che sta richiedendo la sottoscrizione
                if (inviterUsername.equals(username)) {
                    throw new DAOException("User cannot use their own invite code");
                }

                //5. Inserisce la sottoscrizione con invito
                int subscriptionCode;
                try (var statement = DAOUtils.prepareWithKeys(connection, Queries.INSERT_SUBSCRIPTION_INVITE, Statement.RETURN_GENERATED_KEYS, username, autoRenew, inviteCode, subscriptionPlanCode)) {
                    statement.executeUpdate();

                    try (var rs = statement.getGeneratedKeys()) {
                        if (rs.next()) {
                            subscriptionCode = rs.getInt(1);
                        } else {
                            throw new DAOException("Inserting subscription failed, no ID obtained.");
                        }
                    }
                }

                //6. Inserisce la transazione per il pagamento della sottoscrizione
                Transaction.DAO.insertWithInvite(connection, subscriptionCode, paymentMethod, subscriptionPlanCode);

                //7. Aggiorna il credito bonus dell'utente che ha invitato
                //User.DAO.updateBonusCredit(connection, inviterUsername, 10);
                
                connection.commit();
                return subscriptionCode;
            } catch (final SQLException e) {
                try {
                    connection.rollback();
                } catch (final SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw new DAOException(e);
            } finally {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (final SQLException e) {
                    throw new DAOException(e);
                }
            }
        }

        /**
         * OP 3.1
         * Checks if the user has an active subscription that is eligible for renewal.
         * If such a subscription exists, it returns an Optional containing the Subscription object.
         * @param connection
         * @param subscriptionCode
         * @return an Optional containing the Subscription object if eligible for renewal, or an empty Optional otherwise.
         * @throws DAOException if any database operation fails.
         */
        public static Optional<Subscription> findForRenewal(final Connection connection, final int subscriptionCode) {
            
            try (var statement = DAOUtils.prepare(connection, Queries.CHECK_SUBSCRIPTION_RENEWAL, subscriptionCode);
                 var resultSet = statement.executeQuery()) {
    
                if (resultSet.next()) {
                    return Optional.of(new Subscription(
                            resultSet.getInt("CodiceSottoscrizione"),
                            resultSet.getString("Username"),
                            resultSet.getInt("CodiceAbbonamento"),
                            resultSet.getObject("CodicePromozione") != null ? resultSet.getInt("CodicePromozione") : null,
                            resultSet.getString("CodiceInvito"),
                            resultSet.getDate("DataInizio").toLocalDate(),
                            resultSet.getDate("DataFine").toLocalDate(),
                            resultSet.getString("Stato"),
                            resultSet.getBoolean("RinnovoAutomatico")
                        ));
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return Optional.empty();
        }

        /**
         * OP 3.1
         * Renews an existing subscription by extending its end date and recording the renewal transaction.
         * @param connection the database connection.
         * @param subscriptionCode the code of the subscription to renew.
         * @param paymentMethod the payment method used for the renewal.
         * @param paymentSuccess indicates whether the payment was successful.
         * @throws DAOException if any database operation fails.
         */
        public static void renew(final Connection connection, final int subscriptionCode, final String paymentMethod, final boolean paymentSuccess) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                //1. Se il pagamento è andato a buon fine, estende DataFine
                if (paymentSuccess) {
                    try (var statement = DAOUtils.prepare(connection, Queries.RENEW_SUBSCRIPTION, subscriptionCode)) {
                            statement.executeUpdate();
                    }
                }
                //2. Registra la transazione
                String status = paymentSuccess ? "Completata" : "Fallita";
                Transaction.DAO.insertRenewal(connection, subscriptionCode, paymentMethod, status);
                
                connection.commit();
            } catch (final SQLException e) {
                try {
                    connection.rollback();
                } catch (final SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw new DAOException(e);
            } finally {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (final SQLException e) {
                    throw new DAOException(e);
                }
            }
        }

        /**
         * OP 3.2
         * Disable the auto renew of a specific Subscription
         * @param connection
         * @param subscriptionCode
         * @throws DAOException if a database error occurs
         */
        public static void disableAutoRenew(final Connection connection, final int subscriptionCode) {
            try (var statement = DAOUtils.prepare(connection, Queries.CANCEL_RENEWAL, subscriptionCode)) {
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DAOException("Subscription not found or not active.");
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }


        /**
         * Marca una sottoscrizione come scaduta
         * @param connection
         * @param subscriptionCode
         * @@throws DAOException if a database error occurs
         */
        public static void expireSubscription(final Connection connection, final int subscriptionCode) {
            try (var statement = DAOUtils.prepare(connection, Queries.EXPIRE_SUBSCRIPTION, subscriptionCode)) {
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DAOException("Subscription not found");
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * OP 4.1
         * Subscribe a monthly plan with credit bonus
         * @param connection
         * @param username
         * @param subscriptionPlanCode
         * @param autoRenew
         * @return the genereated subscription code
         * @throws DAOException if a database error occurs
         */
        public static int redeemBonusForNew(final Connection connection, final String username, final int subscriptionPlanCode, final boolean autoRenew) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                //1. Verifica che l'utente abbiamo almeno 2 crediti bonus
                try (var smt = DAOUtils.prepare(connection, Queries.CHECK_BONUS_CREDIT, username);
                     var rs = smt.executeQuery()) {
                    if (!rs.next()) {
                        throw new DAOException("Insufficient bonus credits. You need at least 2.");
                    }
                }

                //2.Verifica che l'abbonamento sia mensile
                if (!Plan.DAO.isMonthlyPlan(connection, subscriptionPlanCode)) {
                    throw new DAOException("Bonus redemption is only available for monthly plans.");
                }

                //3. Verifica che l'utente non abbia già una sottoscrizione attiva
                try (var stmt = DAOUtils.prepare(connection, Queries.CHECK_ACTIVE_SUBSCRIPTION, username);
                     var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        throw new DAOException("User already has an active subscription.");
                    }
                }

                //4.Inserisce la sottoscrizione
                int subscriptionCode;

                try (var stmt = DAOUtils.prepareWithKeys(connection, Queries.INSERT_SUBSCRIPTION_BONUS_CREDIT, Statement.RETURN_GENERATED_KEYS, username, subscriptionPlanCode, autoRenew)) {
                    stmt.executeUpdate();

                    try (var rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            subscriptionCode = rs.getInt(1);
                        } else {
                            throw new DAOException("User already has an active subscription.");
                        }
                    }
                    
                }

                //5. Registrazione la transazione a costo zero
                Transaction.DAO.insertBonusCredit(connection, subscriptionCode);

                //6. Decrementa i crediti bonus di 2
                User.DAO.decrementBonusCredit(connection, username);

                connection.commit();
                return subscriptionCode;
            } catch (final SQLException e) {
                try {
                    connection.rollback();
                } catch (final SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw new DAOException(e);
            } finally {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (final SQLException e) {
                    throw new DAOException(e);
                }
            }
        }

        /**
         * OP 4.2
         * Renew monthly subscription with credit bonus
         * @param connection
         * @param username
         * @param subscriptionCode
         */
        public static void renewWithBonus(final Connection connection, final String username, final int subscriptionCode) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                //1. Verifica che l'utente abbiamo almeno 2 crediti bonus
                try (var smt = DAOUtils.prepare(connection, Queries.CHECK_BONUS_CREDIT, username);
                     var rs = smt.executeQuery()) {
                    if (!rs.next()) {
                        throw new DAOException("Insufficient bonus credits. You need at least 2.");
                    }
                }

                //2.Verifica che la sottoscrizione sia attiva e mensile
                try (var stmt = DAOUtils.prepare(connection, Queries.CHECK_ACTIVE_MONTHLY_SUBSCRIPTION, subscriptionCode, username);
                     var rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new DAOException("Subscription not found, not active, or not a monthly plan.");
                    }
                }

                //3. Rinnova la sottoscrizione
                try (var stmt = DAOUtils.prepare(connection, Queries.EXTEND_SUBSCRIPTION_BONUS_CREDIT, subscriptionCode, username)) {
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected == 0) {
                        throw new DAOException("Failed to renew subscription.");
                    }                    
                }

                //4. Registrazione la transazione a costo zero
                Transaction.DAO.insertBonusCredit(connection, subscriptionCode);

                //5. Decrementa i crediti bonus di 2
                User.DAO.decrementBonusCredit(connection, username);

                connection.commit();
            } catch (final SQLException e) {
                try {
                    connection.rollback();
                } catch (final SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw new DAOException(e);
            } finally {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (final SQLException e) {
                    throw new DAOException(e);
                }
            }
        }
    }
}
