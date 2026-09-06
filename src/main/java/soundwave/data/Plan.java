package soundwave.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents a subscription plan in the system.
 */
public final class Plan {
    private final int planCode;
    private final String typePlan;
    private final int durationMonths;
    private final double price;

    /**
     * Constructs a new Plan instance.
     *
     * @param planCode       the unique code of the plan.
     * @param typePlan       the type of the plan.
     * @param durationMonths the duration of the plan in months.
     * @param price          the price of the plan.
     */
    public Plan(final int planCode, final String typePlan, final int durationMonths, final double price) {
        this.planCode = planCode;
        this.typePlan = Objects.requireNonNull(typePlan, "Type plan cannot be null");
        this.durationMonths = durationMonths;
        this.price = price;
    }

    /**
     * Gets the unique code of the plan.
     *
     * @return the plan code.
     */
    public int getPlanCode() {
        return planCode;
    }

    /**
     * Gets the type of the plan.
     *
     * @return the type of the plan.
     */
    public String getTypePlan() {
        return typePlan;
    }

    /**
     * Gets the duration of the plan in months.
     *
     * @return the duration in months.
     */
    public int getDurationMonths() {
        return durationMonths;
    }

    /**
     * Gets the price of the plan.
     *
     * @return the price of the plan.
     */
    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Plan)) {
            return false;
        }
        final Plan a = (Plan) other;
        return this.planCode == a.planCode && a.typePlan.equals(this.typePlan) 
                && this.durationMonths == a.durationMonths && this.price == a.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.planCode, this.typePlan, this.durationMonths, this.price);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Plan",
            List.of(
                Printer.field("planCode", this.planCode),
                Printer.field("typePlan", this.typePlan),
                Printer.field("durationMonths", this.durationMonths),
                Printer.field("price", this.price)
            )
        );
    }

    /**
     * DAO class for Plan.
     */
    public static final class DAO {
        private DAO() { }

        /**
         * Checks if a given plan code corresponds to a monthly subscription.
         *
         * @param connection the database connection.
         * @param code       the plan code to check.
         * 
         * @return true if the plan is a monthly subscription, false otherwise.
         */
        public static boolean isMonthlyPlan(final Connection connection, final int code) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.CHECK_MONTHLY_SUBSCRIPTION, code);
                var resultSet = statement.executeQuery()
                ) {
                    return resultSet.next();
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        public static List<Plan> listAll(final Connection connection) {
            Objects.requireNonNull(connection, "Connection cannot be null");
            final List<Plan> plans = new ArrayList<>();
            final String query =  """
                                    SELECT CodiceAbbonamento, TipoAbbonamento, Durata, Costo
                                    FROM Abbonamenti
                                    ORDER BY Durata, Costo
                                  """;
            
            try (var stmt = connection.createStatement();
                 var rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    plans.add(new Plan(
                        rs.getInt("CodiceAbbonamento"),
                        rs.getString("TipoAbbonamento"),
                        rs.getInt("Durata"),
                        rs.getDouble("Costo")
                    ));
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return plans;
        }

    }
}
