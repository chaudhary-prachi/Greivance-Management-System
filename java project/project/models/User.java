package project.models;

/**
 * Represents a citizen user in the Public Grievance Sorting System.
 * This class stores basic account and profile information.
 */
public class User {

    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phone;

    /**
     * Creates an empty user.
     */
    public User() {
    }

    /**
     * Creates a user with all required details.
     *
     * @param fullName full name of the user
     * @param username login username
     * @param password login password
     * @param email email address
     * @param phone phone number
     */
    public User(String fullName, String username, String password, String email, String phone) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Converts the user object into one line for storage in users.txt.
     *
     * @return comma-separated user data
     */
    public String toFileString() {
        return fullName + "," + username + "," + password + "," + email + "," + phone;
    }

    /**
     * Creates a User object from a line stored in users.txt.
     *
     * @param line file line data
     * @return parsed User object, or null if the line is invalid
     */
    public static User fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",");
        if (parts.length < 5) {
            return null;
        }

        return new User(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim());
    }
}
