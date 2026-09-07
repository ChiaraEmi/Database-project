package soundwave.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Represents a promotion entity within the Soundwave system.
 */
public final class Promotion {
    private static final Logger LOG = Logger.getLogger(Promotion.class.getName());

    private final String promotionCode;
    private final String name;
    private final String description;
    private final LocalDate beginDate;
    private final LocalDate endDate;
    private final String discountType; // Type of promotion, e.g., "percentage" or "value"
    private final double discountValue; 
    private final Integer requiredMonths;

    /**
     * Creates a new Promotion instance.
     * 
     * @param promotionCode the unique code of the promotion.
     * @param name          the name of the promotion.
     * @param description   the description of the promotion.
     * @param beginDate     the start date of the promotion.
     * @param endDate       the end date of the promotion.
     * @param discountType  the type of discount.
     * @param discountValue the value of the discount.
     * @param requiredMonths optional field for the required months.
     */
    public Promotion(
            final String promotionCode,
            final String name,
            final String description,
            final LocalDate beginDate,
            final LocalDate endDate,
            final String discountType,
            final double discountValue,
            final Integer requiredMonths
    ) {
        this.promotionCode = Objects.requireNonNull(promotionCode, "Promotion code cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.beginDate = Objects.requireNonNull(beginDate, "Begin date cannot be null");
        this.endDate = Objects.requireNonNull(endDate, "End date cannot be null");
        this.discountType = Objects.requireNonNull(discountType, "Discount type cannot be null");
        this.discountValue = discountValue;
        this.requiredMonths = requiredMonths;
    }

    /**
     * Returns the unique code of the promotion.
     *
     * @return the promotion code
     */
    public String getPromotionCode() {
        return promotionCode;
    }

    /**
     * Returns the name of the promotion.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the promotion.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the start date of the promotion.
     *
     * @return the begin date
     */
    public LocalDate getBeginDate() {
        return beginDate;
    }

    /**
     * Returns the end date of the promotion.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns the type of discount for the promotion.
     *
     * @return the discount type
     */
    public String getDiscountType() {
        return discountType;
    }

    /**
     * Returns the value of the discount for the promotion.
     *
     * @return the discount value
     */
    public double getDiscountValue() {
        return discountValue;
    }

    /**
     * Returns the optional field for the required months.
     *
     * @return the required months
     */
    public Integer getRequiredMonths() {
        return requiredMonths;
    }

    /**
     * Checks if the promotion is currently active.
     *
     * @return true if the promotion is active, false otherwise
     */
    public boolean isActive() {
        final var currentDate = LocalDate.now();
        return (currentDate.isEqual(beginDate) || currentDate.isAfter(beginDate)) 
                && (currentDate.isEqual(endDate) || currentDate.isBefore(endDate));
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null || !(other instanceof Promotion)) {
            return false;
        } 

        final var p = (Promotion) other;
        return Double.compare(p.discountValue, this.discountValue) == 0
                && Objects.equals(p.promotionCode, this.promotionCode)
                && Objects.equals(p.name, this.name)
                && Objects.equals(p.description, this.description)
                && Objects.equals(p.beginDate, this.beginDate)
                && Objects.equals(p.endDate, this.endDate)
                && Objects.equals(p.discountType, this.discountType)
                && Objects.equals(p.requiredMonths, this.requiredMonths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.promotionCode, this.name, this.description, this.beginDate, 
                            this.endDate, this.discountType, this.discountValue, this.requiredMonths);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Promotion",
            List.of(
                Printer.field("promotionCode", this.promotionCode),
                Printer.field("name", this.name),
                Printer.field("description", this.description),
                Printer.field("beginDate", this.beginDate),
                Printer.field("endDate", this.endDate),
                Printer.field("discountType", this.discountType),
                Printer.field("discountValue", this.discountValue),
                Printer.field("requiredMonths", this.requiredMonths)
            )
        );
    }

    /**
     * Data Access Object for managing Promotion records in the database.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Inserts a new promotion into the database and associates it with the specified subscription plans.
         *
         * @param connection            the database connection.
         * @param code                  the unique string code of the promotion.
         * @param name                  the name of the promotion.
         * @param description           the description of the promotion.
         * @param beginDate             the start date of the promotion.
         * @param endDate               the end date of the promotion.
         * @param discountType          the type of discount (e.g., "Percentuale" or "Fisso").
         * @param discountValue         the value of the discount.
         * @param requiredMonths        optional field for the required months.
         * @param subscriptionPlanCodes list of subscription plan codes to associate with the promotion.
         * 
         * @return                      an integer indicating success (1).
         */
        public static int insertPromotion(
                final Connection connection, 
                final String code, 
                final String name, 
                final String description, 
                final LocalDate beginDate, 
                final LocalDate endDate, 
                final String discountType, 
                final double discountValue, 
                final Integer requiredMonths, 
                final List<Integer> subscriptionPlanCodes
        ) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                //2. Insert promotion
                try (var statement = DAOUtils.prepare(
                                                    connection, Queries.INSERT_PROMOTIONAL_CAMPAIGN, 
                                                    code, name, description, Date.valueOf(beginDate), 
                                                    Date.valueOf(endDate), discountType, discountValue, 
                                                    requiredMonths)) {
                    statement.executeUpdate();
                }

                // 2. Insert the promotion-plan associations
                if (subscriptionPlanCodes != null && !subscriptionPlanCodes.isEmpty()) {
                    try (var statement = connection.prepareStatement(Queries.INSERT_PROMOTIONAL_VALIDITY)) {
                        for (final int planCode : subscriptionPlanCodes) {
                            statement.setString(1, code);
                            statement.setInt(2, planCode);
                            statement.addBatch();
                        }
                        statement.executeBatch();
                    }
                }

                connection.commit();
                return 1;
            } catch (final SQLException e) {
                try {
                    connection.rollback();
                } catch (final SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
                throw new DAOException(e);
            } finally {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (final SQLException e) {
                    LOG.log(Level.SEVERE, "Failed to reset auto-commit to " + autoCommit, e);
                }
            }
        }
    }
}
