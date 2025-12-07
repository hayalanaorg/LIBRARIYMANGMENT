package library;

/**
 * Represents the different access levels a system user (especially an Admin)
 * can have within the library application.
 * <p>
 * This enum is used to distinguish between a regular administrator and a
 * super administrator with higher privileges.
 * </p>
 *
 * <h2>Roles:</h2>
 * <ul>
 *     <li><b>ADMIN:</b> Standard administrator with basic system permissions.</li>
 *     <li><b>SUPER_ADMIN:</b> Higher-level administrator with extended
 *     privileges such as managing other admins or performing restricted operations.</li>
 * </ul>
 *
 * @author 
 *      Lana Omar (Documented)
 * @version 1.0
 * @since 2025-12-07
 */
public enum Role {

    /** Highest-level administrator with full control. */
    SUPER_ADMIN,

    /** Standard administrator with regular system access. */
    ADMIN
}
