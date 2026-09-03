package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a user entity within the Soundwave system.
 */
public final class User {

    private static final String FIELD_USERNAME = "Username";

    private final String username;
    private final String name;
    private final String surname;
    private final String email;
    private final String password;
    private final LocalDate birthDate;
    private final String country;
    private final int bonusCredit;

    /**
     * Creates a new User instance.
     * 
     * @param username    the unique username of the user.
     * @param name        the name of the user.
     * @param surname     the surname of the user.
     * @param email       the email address of the user.
     * @param password    the password of the user.
     * @param birthDate   the birth date of the user.
     * @param country     the country of origin of the user.
     * @param bonusCredit the bonus credit associated with the user.
     */
    public User(
            final String username,
            final String name,
            final String surname,
            final String email,
            final String password,
            final LocalDate birthDate,
            final String country,
            final int bonusCredit
    ) {
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.surname = Objects.requireNonNull(surname, "Surname cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
        this.birthDate = birthDate;
        this.country = Objects.requireNonNull(country, "Country cannot be null");
        this.bonusCredit = bonusCredit;
    }

    /**
     * Returns the username of the user.
     * 
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the name of the user.
     * 
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the surname of the user.
     * 
     * @return the surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Returns the email of the user.
     * 
     * @return the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the password of the user.
     * 
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the birth date of the user.
     * 
     * @return the birth date.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Returns the country of the user.
     * 
     * @return the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the bonus credit of the user.
     * 
     * @return the bonus credit.
     */
    public int getBonusCredit() {
        return bonusCredit;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof User) {
            final var u = (User) other;
            return this.username.equals(u.username);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "User",
            List.of(
                Printer.field("username", this.username),
                Printer.field("name", this.name),
                Printer.field("surname", this.surname),
                Printer.field("email", this.email),
                Printer.field("password", this.password),
                Printer.field("birthDate", this.birthDate),
                Printer.field("country", this.country),
                Printer.field("bonusCredit", this.bonusCredit)
            )
        );
    }

    /**
     * Data Access Object for managing User records in the database.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Retrieves a list of all users from the database.
         * 
         * @param connection the active database connection.
         * @return a list of users.
         * @throws SQLException if a database error occurs.
         */
        public static List<User> list(final Connection connection) throws SQLException {
            Objects.requireNonNull(connection, "Connection cannot be null");
            final var users = new ArrayList<User>();
            final String query = "SELECT " + FIELD_USERNAME 
                                + ", Nome, Cognome, Email, Password, DataNascita, Paese, CreditoBonus FROM Utenti";

            try (var statement = connection.createStatement();
                 var resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    final LocalDate localDate = resultSet.getObject("DataNascita", LocalDate.class);

                    users.add(new User(
                        resultSet.getString(FIELD_USERNAME),
                        resultSet.getString("Nome"),
                        resultSet.getString("Cognome"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password"),
                        localDate,
                        resultSet.getString("Paese"),
                        resultSet.getInt("CreditoBonus")
                    ));
                }
            }
            return users;
        }

        /**
         * Finds a specific user by their unique username.
         * 
         * @param connection the active database connection.
         * @param username   the username to search for.
         * @return an Optional containing the user if found, or empty otherwise.
         * @throws SQLException if a database error occurs.
         */
        public static Optional<User> find(final Connection connection, final String username) throws SQLException {
            Objects.requireNonNull(connection, "Connection cannot be null");
            Objects.requireNonNull(username, "Username cannot be null");
            final String query = "SELECT " + FIELD_USERNAME 
                + ", Nome, Cognome, Email, Password, DataNascita, Paese, CreditoBonus "
                + "FROM Utenti WHERE " + FIELD_USERNAME + " = ?";

            try (var statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        final LocalDate localDate = resultSet.getObject("DataNascita", LocalDate.class);

                        return Optional.of(new User(
                            resultSet.getString(FIELD_USERNAME),
                            resultSet.getString("Nome"),
                            resultSet.getString("Cognome"),
                            resultSet.getString("Email"),
                            resultSet.getString("Password"),
                            localDate,
                            resultSet.getString("Paese"),
                            resultSet.getInt("CreditoBonus")
                        ));
                    }
                }
            }
            return Optional.empty();
        }

        /**
         * Retrieves users with a number of listens above the average for the given year.
         *
         * @param connection the database connection.
         * @param year the year to check.
         * @return a list of strings representing users and their play counts.
         */
        public static List<String> getUsersAboveAverageListens(final Connection connection, final int year) {
            final List<String> users = new ArrayList<>();
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_USERS_ABOVE_AVG_LISTENS, year, year);
                 var resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    users.add("Utente: " + resultSet.getString(FIELD_USERNAME) 
                                        + " - Ascolti: " + resultSet.getInt("NumeroAscolti"));
                }

            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return users;
        }
    }
}
