package soundwave.data;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Represents an invite code entity within the Soundwave system.
 */
public final class InviteCode {
    private final String code;
    private final LocalDate generationDate;
    private final String username;

    /**
     * Creates a new InviteCode instance.
     * 
     * @param code           the unique invite code
     * @param generationDate the date when the invite code was generated
     * @param username       the username of the user who generated the invite code
     */
    public InviteCode(final String code, final LocalDate generationDate, final String username) {
        this.code = Objects.requireNonNull(code, "Code cannot be null");
        this.generationDate = Objects.requireNonNull(generationDate, "Generation date cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
    }

    /**
     * Returns the invite code.
     * 
     * @return the invite code
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the generation date of the invite code.
     * 
     * @return the generation date
     */
    public LocalDate getGenerationDate() {
        return generationDate;
    }

    /**
     * Returns the username of the user who generated the invite code.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        } else if (o instanceof InviteCode) {
            final var c = (InviteCode) o;
            return this.code.equals(c.code);
        } else {
            return false;
        }
        
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "InviteCode",
            List.of(
                Printer.field("code", this.code),
                Printer.field("generationDate", this.generationDate),
                Printer.field("username", this.username)
            )
        );
    }

    /**
     * A static inner class for database access operations related to InviteCode.
     */
    public static final class DAO {
        private DAO() {}
    }
}