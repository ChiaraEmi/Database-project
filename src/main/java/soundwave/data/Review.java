package soundwave.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Review entity.
 */
public final class Review {

    private final String username;
    private final int albumCode;
    private final int rating;
    private final String comment;
    private final String reviewDate;

    /**
     * Constructs a new Review instance.
     *
     * @param username the username of the reviewer.
     * @param albumCode the album code being reviewed.
     * @param rating the vote (1 to 10).
     * @param comment the review comment.
     * @param reviewDate the date of the review.
     */
    public Review(final String username, final int albumCode, final int rating, final String comment, final String reviewDate) {
        this.username = username == null ? "" : username;
        this.albumCode = albumCode;
        this.rating = rating;
        this.comment = comment == null ? "" : comment;
        this.reviewDate = reviewDate == null ? "" : reviewDate;
    }

    public String getUsername() {
        return username;
    }

    public int getAlbumCode() {
        return albumCode;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || !(other instanceof Review)) {
            return false;
        }
        final Review r = (Review) other;
        return this.albumCode == r.albumCode && this.username.equals(r.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username, this.albumCode);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "Review",
            List.of(
                Printer.field("username", this.username),
                Printer.field("albumCode", this.albumCode),
                Printer.field("rating", this.rating),
                Printer.field("comment", this.comment),
                Printer.field("reviewDate", this.reviewDate)
            )
        );
    }

    /**
     * Data Access Object for Review operations.
     */
    public static final class DAO {

        private DAO() { }

        /**
         * Inserts or updates a review in the database (OP 16).
         *
         * @param connection the database connection.
         * @param username the username.
         * @param albumCode the album code.
         * @param rating the rating (1 to 10).
         * @param comment the comment text.
         */
        public static void saveReview(final Connection connection, final String username, final int albumCode, final int rating, final String comment) {
            try (var statement = DAOUtils.prepare(connection, Queries.UPSERT_REVIEW, username, albumCode, rating, comment)) {
                statement.executeUpdate();
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
        }

       /**(
         * Retrieves all reviews for a specific album (OP 17.1).
         *
         * @param connection the database connection.
         * @param albumCode the album code.
         * @return a list of reviews for that album.
         */
        public static List<Review> getReviewsForAlbum(final Connection connection, final int albumCode) {
            final List<Review> reviews = new ArrayList<>();
            try (var statement = DAOUtils.prepare(connection, Queries.SELECT_REVIEWS_FOR_ALBUM, albumCode)) {
                try (var resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        reviews.add(new Review(
                            resultSet.getString("Username"),
                            albumCode,
                            resultSet.getInt("Voto"),
                            resultSet.getString("Commento"),
                            resultSet.getString("DataRecensione")
                        ));
                    }
                }
            } catch (final SQLException e) {
                throw new DAOException(e);
            }
            return reviews;
        }
    }
}