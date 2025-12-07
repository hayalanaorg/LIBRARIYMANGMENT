package library;

/**
 * Observer interface for receiving notifications related to library events.
 * <p>
 * This interface is used in the Observer Design Pattern.
 * Implementations (such as {@link EmailNotifier}) react to events—
 * for example, when a member has overdue loans and reminders must be sent.
 * </p>
 *
 * <h2>Usage:</h2>
 * <pre>
 * Observer notifier = new EmailNotifier(emailService);
 * notifier.notify(member, "You have an overdue item!");
 * </pre>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public interface Observer {

    /**
     * Sends a notification to a specific member.
     *
     * @param m       the member who will receive the notification
     * @param message the message content to deliver
     */
    void notify(Member m, String message);
}
