package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.List;

/**
 * Represents a transaction entity within the Soundwave system.
 */
public final class Transaction {
    private final int transactionCode;
    private final int subscriptionCode;
    private final LocalDateTime date;
    private final double amount;
    private final String paymentMethod;
    private final String status; //'Completa' o 'Fallita'

    /**
     * Creates a new Transaction instance.
     * @param transactionCode
     * @param subscriptionCode
     * @param date
     * @param amount
     * @param paymentMethod
     * @param status
     */
    public Transaction(final int transactionCode, final int subscriptionCode, final LocalDateTime date, final double amount, final String paymentMethod, final String status) {
        this.transactionCode = Objects.requireNonNull(transactionCode, "Transaction code cannot be null");
        this.subscriptionCode = Objects.requireNonNull(subscriptionCode, "Subscription code cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        this.paymentMethod = Objects.requireNonNull(paymentMethod, "Payment method cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    /**
     * Gets the unique code of the transaction.
     * @return the transaction code.
     */
    public int getTransactionCode() {
        return transactionCode;
    }

    /**
     * Gets the code of the associated subscription.
     * @return the subscription code.
     */
    public int getSubscriptionCode() {
        return subscriptionCode;
    }

    /**
     * Gets the date and time of the transaction.
     * @return the transaction date and time.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Gets the amount of the transaction.
     * @return the transaction amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the payment method used for the transaction.
     * @return the payment method.
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Gets the status of the transaction.
     * @return the transaction status.
     */
    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } else if (other == null || !(other instanceof Transaction)) {
            return false;
        }
        final var t = (Transaction) other;
        return transactionCode == t.transactionCode
                && subscriptionCode == t.subscriptionCode
                && Double.compare(t.amount, amount) == 0
                && date.equals(t.date)
                && paymentMethod.equals(t.paymentMethod)
                && status.equals(t.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionCode, subscriptionCode, date, amount, paymentMethod, status);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Transaction",
            List.of(
                Printer.field("transactionCode", this.transactionCode),
                Printer.field("subscriptionCode", this.subscriptionCode),
                Printer.field("date", this.date),
                Printer.field("amount", this.amount),
                Printer.field("paymentMethod", this.paymentMethod),
                Printer.field("status", this.status)
            )
        );
    }

    public static final class DAO {
        private DAO() {}

        /**
         * OP 2.1
         * Inserts a new standard transaction into the database.
         *
         * @param connection the database connection.
         * @param subscriptionCode the code of the associated subscription.
         * @param paymentMethod the payment method used for the transaction.
         * @param subscriptionPlanCode the code of the subscription plan.
         * @return the generated transaction code.
         */
        public static int insertStandard(final Connection connection, final int subscriptionCode, final String paymentMethod, final int subscriptionPlanCode) {
            try (
                var statement = DAOUtils.prepareWithKeys(connection, Queries.INSERT_TRANSACTION_STANDARD, Statement.RETURN_GENERATED_KEYS, subscriptionCode, paymentMethod, subscriptionPlanCode)
            ) {
                statement.executeUpdate();
                try (var resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    } else {
                        throw new DAOException("Failed to insert transaction");
                    }
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * OP 2.2
         * Inserts a new promotional transaction into the database.
         *
         * @param connection the database connection.
         * @param subscriptionCode the code of the associated subscription.
         * @param amount the amount of the transaction.
         * @param paymentMethod the payment method used for the transaction.
         * @return the generated transaction code.
         */
        public static int insertWithPromotion(final Connection connection, final int subscriptionCode, final double amount, final String paymentMethod) {
            try (
                var statement = DAOUtils.prepareWithKeys(connection, Queries.INSERT_TRANSACTION_PROMOTIONAL, Statement.RETURN_GENERATED_KEYS, subscriptionCode, amount, paymentMethod)
            ) {
                statement.executeUpdate();
                try (var resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    } else {
                        throw new DAOException("Failed to insert transaction");
                    }
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * OP 2.3
         * Inserts a new transaction with an invite into the database.
         *
         * @param connection the database connection.
         * @param subscriptionCode the code of the associated subscription.
         * @param paymentMethod the payment method used for the transaction.
         * @param subscriptionPlanCode the code of the subscription plan.
         * @return the generated transaction code.
         */
        public static int insertWithInvite(final Connection connection, final int subscriptionCode, final String paymentMethod, final int subscriptionPlanCode) {
            try (
                var statement = DAOUtils.prepareWithKeys(connection, Queries.INSERT_TRANSACTION_INVITE, Statement.RETURN_GENERATED_KEYS, subscriptionCode, paymentMethod, subscriptionPlanCode)
            ) {
                statement.executeUpdate();
                try (var resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    } else {
                        throw new DAOException("Failed to insert transaction");
                    }
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * OP 3
         * Inserts a new renewal transaction into the database.
         * @param connection
         * @param subscriptionCode
         * @param paymentMethod
         * @param status
         * @param subscriptionPlanCode
         * @return
         */
        public static int insertRenewal(final Connection connection, final int subscriptionCode, final String paymentMethod, final String status) {
            try (
                var statement = DAOUtils.prepareWithKeys(connection, Queries.INSERT_RENEWAL_TRANSACTION, Statement.RETURN_GENERATED_KEYS, subscriptionCode, paymentMethod, status)
            ) {
                statement.executeUpdate();
                try (var resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    } else {
                        throw new DAOException("Failed to insert transaction");
                    }
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * OP 4
         * Inserts a new transaction for bonus credit into the database.
         *
         * @param connection the database connection.
         * @param subscriptionCode the code of the associated subscription.
         * @return the generated transaction code.
         */
        public static int insertBonusCredit(final Connection connection, final int subscriptionCode) {
            try (
                var statement = DAOUtils.prepareWithKeys(connection, Queries.INSERT_TRANSACTION_BONUS_CREDIT, Statement.RETURN_GENERATED_KEYS, subscriptionCode)
            ) {
                statement.executeUpdate();
                try (var resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    } else {
                        throw new DAOException("Failed to insert transaction");
                    }
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }
    }

}
