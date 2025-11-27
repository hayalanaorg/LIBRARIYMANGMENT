package library;

/**
 * Represents a CD media item.
 */
public class CD {

    private String title;
    private String artist;
    private boolean available = true;

    public CD(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markBorrowed() {
        this.available = false;
    }

    public void markReturned() {
        this.available = true;
    }

    @Override
    public String toString() {
        return "CD: " + title + " by " + artist;
    }
}
