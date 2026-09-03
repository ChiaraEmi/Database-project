package soundwave.data;

import java.util.List;
import java.util.Objects;

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
        this.durationMonths = Objects.requireNonNull(durationMonths, "Duration months cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
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

    /**
     * Checks if the plan is a monthly plan.
     *
     * @return true if the plan is monthly, false otherwise.
     */
    public boolean isMonthlyPlan() {
        return durationMonths == 1;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } else if (other == null || !(other instanceof Plan)) {
            return false;
        }
        final Plan a = (Plan) other;
        return this.planCode == a.planCode && a.typePlan.equals(this.typePlan) && this.durationMonths == a.durationMonths && this.price == a.price;
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
        private DAO() {}

        
    }
}
