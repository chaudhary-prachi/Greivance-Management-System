package project.utils;

/**
 * Provides simple input validation methods for the application.
 * These methods help keep user input clean and beginner friendly.
 */
public class InputValidator {

    /**
     * Checks whether a string is null or empty.
     *
     * @param value input text
     * @return true if blank, otherwise false
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates a username.
     * Username must be at least 4 characters long.
     *
     * @param username username text
     * @return true if valid
     */
    public static boolean isValidUsername(String username) {
        return !isEmpty(username) && username.trim().length() >= 4;
    }

    /**
     * Validates a password.
     * Password must be at least 6 characters long.
     *
     * @param password password text
     * @return true if valid
     */
    public static boolean isValidPassword(String password) {
        return !isEmpty(password) && password.length() >= 6;
    }

    /**
     * Validates an email address using a simple pattern.
     *
     * @param email email text
     * @return true if valid
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Validates an Indian-style phone number.
     * Phone number must contain exactly 10 digits.
     *
     * @param phone phone text
     * @return true if valid
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return phone.matches("[0-9]{10}");
    }

    /**
     * Validates complaint topic or description style fields.
     *
     * @param value input text
     * @return true if valid
     */
    public static boolean isNonEmptyText(String value) {
        return !isEmpty(value);
    }
}
