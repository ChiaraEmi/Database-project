package soundwave.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.sql.Statement;

/**
 * Represents a promotion entity within the Soundwave system.
 */
public final class Promotion {

    private final int promotioncode;
    private final String name;
    private final String description;
    private final LocalDate beginDate;
    private final LocalDate endDate;
    private final String discountType; // Type of promotion, e.g., "percentage" or "value"
    private final double discountValue; // Value of the discount, either as a percentage or a fixed amount
    private final Integer requiredMonths; // Optional field for the month when the promotion was requested

    /**
     * Creates a new Promotion instance.
     * 
     * @param promotioncode the unique code of the promotion
     * @param name          the name of the promotion
     * @param description   the description of the promotion
     * @param beginDate     the start date of the promotion
     * @param endDate       the end date of the promotion
     * @param discountType  the type of discount (e.g., "percentage" or "value")
     * @param discountValue the value of the discount
     * @param requiredMonths optional field for the month when the promotion was requested
     */
    public Promotion(
            final int promotioncode,
            final String name,
            final String description,
            final LocalDate beginDate,
            final LocalDate endDate,
            final String discountType,
            final double discountValue,
            final Integer requiredMonths
    ) {
        this.promotioncode = promotioncode;
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.beginDate = Objects.requireNonNull(beginDate, "Begin date cannot be null");
        this.endDate = Objects.requireNonNull(endDate, "End date cannot be null");
        this.discountType = Objects.requireNonNull(discountType, "Discount type cannot be null");
        this.discountValue = Objects.requireNonNull(discountValue, "Discount value cannot be null");
        this.requiredMonths = requiredMonths;
    }

    /**
     * Returns the unique code of the promotion.
     *
     * @return the promotion code
     */
    public int getPromotionCode() {
        return promotioncode;
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
     * Returns the optional field for the month when the promotion was requested.
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
        return (currentDate.isEqual(beginDate) || currentDate.isAfter(beginDate)) &&
               (currentDate.isEqual(endDate) || currentDate.isBefore(endDate));
    }

    /**
     * Calculates the discounted price based on the original price and the promotion's discount type and value.
     *
     * @param originalPrice the original price before discount
     * @return the discounted price
     */
    public double calculateDiscountedPrice(final double originalPrice) {
        if ("Percentuale".equals(discountType)) {
            return originalPrice * (1- discountValue / 100.0);
        } else if ("Fisso".equals(discountType)) {
            return originalPrice - discountValue;
        } else {
            throw new IllegalArgumentException("Unknown discount type: " + discountType);
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null || !(other instanceof Promotion)) {
            return false;
        } 
        
        final var p = (Promotion) other;
        return p.promotioncode == this.promotioncode && Double.compare(p.discountValue, this.discountValue) == 0
                && Objects.equals(p.name, this.name)
                && Objects.equals(p.description, this.description)
                && Objects.equals(p.beginDate, this.beginDate)
                && Objects.equals(p.endDate, this.endDate)
                && Objects.equals(p.discountType, this.discountType)
                && Objects.equals(p.requiredMonths, this.requiredMonths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.promotioncode, this.name, this.description, this.beginDate, this.endDate, this.discountType, this.discountValue, this.requiredMonths);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Promotion",
            List.of(
                Printer.field("promotioncode", this.promotioncode),
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

        private DAO() {}

        /**
         * Inserts a new promotion into the database and associates it with the specified subscription plans.
         *
         * @param connection            the database connection
         * @param name                  the name of the promotion
         * @param description           the description of the promotion
         * @param beginDate             the start date of the promotion
         * @param endDate               the end date of the promotion
         * @param discountType          the type of discount (e.g., "percentage" or "value")
         * @param discountValue         the value of the discount
         * @param requiredMonths        optional field for the month when the promotion was requested
         * @param subscriptionPlanCodes list of subscription plan codes to associate with the promotion
         * @return the generated promotion code
         */
        public static int insertPromotion(final Connection connection, final String name, final String description, final LocalDate beginDate, final LocalDate endDate, final String discountType, final double discountValue, final Integer requiredMonths, final List<Integer> subscriptionPlanCodes) {
            boolean autoCommit = true;
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);
            
                //1. Insert the promotion and get the generated promotion code  
                int promotionCode;
                try (var statement = DAOUtils.prepareWithKeys(connection, Queries.INSERT_PROMOTIONAL_CAMPAIGN,Statement.RETURN_GENERATED_KEYS, name, description, Date.valueOf(beginDate), Date.valueOf(endDate), discountType, discountValue, requiredMonths)) {
                    statement.executeUpdate();

                    try (var generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            promotionCode = generatedKeys.getInt(1);
                        } else {
                            throw new DAOException("Unable to retrieve generated promotion code.");
                        }
                    }
                }

                //2. Insert the promotion-plan associations
                if(subscriptionPlanCodes != null && !subscriptionPlanCodes.isEmpty()) {
                    try (var statement = connection.prepareStatement(Queries.INSERT_PROMOTIONAL_VALIDITY)) {
                        for (int planCode : subscriptionPlanCodes) {
                            statement.setInt(1, promotionCode);
                            statement.setInt(2, planCode);
                            statement.addBatch();
                        }
                        statement.executeBatch();
                    }
                }
                connection.commit();
                return promotionCode;
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
                    throw new DAOException(e);
                }
            }
        }
    }
}

