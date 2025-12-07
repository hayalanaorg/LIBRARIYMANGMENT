package library;

/**
 * Represents a CD media item in the library system.
 * <p>
 * This class stores information about a CD, including its title,
 * the performing artist, and its current availability status.
 * CDs can be borrowed and returned similarly to books.
 * </p>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-06
 */
public class CD {

    /** The title of the CD. */
    private String title;

    /** The performer or artist of the CD. */
    private String artist;

    /** Indicates whether the CD is available for borrowing. */
    private boolean available = true;

    /**
     * Creates a new {@code CD} instance with the given title and artist.
     *
     * @param title  the name of the CD
     * @param artist the performing artist
     */
    public CD(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    /**
     * Returns the title of the CD.
     *
     * @return the CD's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the name of the performing artist.
     *
     * @return the CD's artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Checks if the CD is available for borrowing.
     *
     * @return {@code true} if available, {@code false} if borrowed
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Marks the CD as borrowed, making it unavailable.
     */
    public void markBorrowed() {
        this.available = false;
    }

    /**
     * Marks the CD as returned and available again.
     */
    public void markReturned() {
        this.available = true;
    }

    /**
     * Returns a readable text representation of the CD.
     *
     * @return a string formatted as {@code "CD: title by artist"}
     */
    @Override
    public String toString() {
        return "CD: " + title + " by " + artist;
    }
}
