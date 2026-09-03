package soundwave.data;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.sql.Connection;


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
        } else if (o == null || !(o instanceof InviteCode)) {
            return false;
        } 

        final var c = (InviteCode) o;
        return this.code.equals(c.code);
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

        /**
         * OP1
         * Generates a new invite code for the specified username and inserts it into the database.
         * 
         * @param connection the database connection
         * @param username   the username for which to generate the invite code
         * @return the generated invite code
         * @throws DAOException if a database access error occurs
         */
        public static String generate(final Connection connection, final String username) {
            final var code = generateCode(username);
            try (
                var statement = DAOUtils.prepare(connection, Queries.INSERT_INVITECODE, code, username)
            ) {
                statement.executeUpdate();
                return code;
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

        /**
         * OP 2.3
         * Finds an invite code in the database by its code value.
         * @param connection
         * @param code
         * @return
         */
        public static Optional<InviteCode> findByCode(final Connection connection, final String code) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.CHECK_INVITECODE, code);
                var resultSet = statement.executeQuery()
            ) {
                if (resultSet.next()) {
                    final var sqlDate = resultSet.getDate("DataGenerazione");
                    final var username = resultSet.getString("Username");
                    return Optional.of(new InviteCode(code, sqlDate != null ? sqlDate.toLocalDate() : null, username));
                } 
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return Optional.empty();
        }

        /**
         * Checks if an invite code exists in the database.
         * @param connection the database connection
         * @param code the invite code to check
         * @return true if the invite code exists, false otherwise
         * @throws DAOException if a database access error occurs
         */
        public static boolean exists(final Connection connection, final String code) {
            return findByCode(connection, code).isPresent();
        }

        /**
         * Retrieves the username of the user who generated a specific invite code.
         * 
         * @param connection the database connection
         * @param code       the invite code to look up
         * @return an Optional containing the username if found, or empty if not found
         */
        public static Optional<String> getOwnerUsername(final Connection connection, final String code) {
            return findByCode(connection, code).map(InviteCode::getUsername);
        }

        /**
         * Validates if the provided invite code is associated with the specified username.
         * 
         * @param connection      the database connection
         * @param code            the invite code to validate
         * @param currentUsername the username to check against the invite code's owner
         * @return true if the invite code is valid for the given username, false otherwise
         */
        public static boolean isValidForUse(final Connection connection, final String code, final String currentUsername) {
            var inviteCode = findByCode(connection, code);
            if (inviteCode.isEmpty()) {
                return false;
            }
            return !inviteCode.get().getUsername().equals(currentUsername);
        }

        /**
         * Generates a unique invite code based on the username and current timestamp.
         * 
         * @param username the username for which to generate the invite code
         * @return a unique invite code
         */
        private static String generateCode(final String username) {
            final var timestamp = System.currentTimeMillis();
            final var hash = Integer.toHexString((username + timestamp).hashCode());
            return "SW" + hash.toUpperCase().substring(0, Math.min(8, hash.length()));
        }
    }

}