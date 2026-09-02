package soundwave.data;

import java.util.List;
import java.util.Objects;

/**
 * Data structure used as input for inserting a song within an album transaction (OP 8).
 */
public final class SongInput {

    public final String title;
    public final int duration;
    public final String description;
    public final int trackNumber;
    public final int artistCodeForSong;
    public final List<String> genres;

    /**
     * Constructs a new SongInput instance.
     *
     * @param title the song title.
     * @param duration the duration in seconds.
     * @param description the description.
     * @param trackNumber the track number.
     * @param artistCodeForSong the artist code.
     * @param genres the list of genres.
     */
    public SongInput(
            final String title,
            final int duration,
            final String description,
            final int trackNumber,
            final int artistCodeForSong,
            final List<String> genres
    ) {
        this.title = title == null ? "" : title;
        this.duration = duration;
        this.description = description == null ? "" : description;
        this.trackNumber = trackNumber;
        this.artistCodeForSong = artistCodeForSong;
        this.genres = genres == null ? List.of() : List.copyOf(genres);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof SongInput s) {
            return s.duration == this.duration
                    && s.trackNumber == this.trackNumber
                    && s.artistCodeForSong == this.artistCodeForSong
                    && s.title.equals(this.title)
                    && s.description.equals(this.description)
                    && s.genres.equals(this.genres);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.title, this.duration, this.description, 
                            this.trackNumber, this.artistCodeForSong, this.genres);
    }
}
