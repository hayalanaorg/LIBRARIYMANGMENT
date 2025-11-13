package library;

/**
 * Represents a CD media item that can be borrowed for 7 days.
 * Sprint 5 - US5.1
 */
public class CD {
    private String title;
    private String artist;
    private String id;
    private boolean available = true;

    public CD(String title, String artist, String id) {
        this.title = title;
        this.artist = artist;
        this.id = id;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getId() { return id; }

    public boolean isAvailable() { return available; }

    public void markBorrowed() { this.available = false; }

    public void markReturned() { this.available = true; }

    @Override
    public String toString() {
        return String.format("CD: %s by %s (%s) - %s",
                title, artist, id, available ? "Available" : "Borrowed");
    }
}
