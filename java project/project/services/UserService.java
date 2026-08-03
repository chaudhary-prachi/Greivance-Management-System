package project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import project.models.User;
import project.utils.FileManager;
import project.utils.InputValidator;

/**
 * Handles user registration, login, and profile-related operations.
 * All user data is stored in a simple text file.
 */
public class UserService {

    private static final String USER_FILE = "project/data/users.txt";

    /**
     * Creates the service and ensures the user file exists.
     */
    public UserService() {
        FileManager.ensureFileExists(USER_FILE);
    }

    /**
     * Loads all users from users.txt.
     *
     * @return list of users
     */
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        List<String> lines = FileManager.readAllLines(USER_FILE);

        for (String line : lines) {
            User user = User.fromFileString(line);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Finds a user by username.
     *
     * @param username username to search
     * @return user object or null
     */
    public User findUserByUsername(String username) {
        if (InputValidator.isEmpty(username)) {
            return null;
        }

        for (User user : loadUsers()) {
            if (user.getUsername().equalsIgnoreCase(username.trim())) {
                return user;
            }
        }
        return null;
    }

    /**
     * Checks whether a username already exists.
     *
     * @param username username to check
     * @return true if username already exists
     */
    public boolean usernameExists(String username) {
        return findUserByUsername(username) != null;
    }

    /**
     * Registers a new user after validating input.
     *
     * @param scanner scanner for console input
     * @return newly created user, or null if registration fails
     */
    public User registerUser(Scanner scanner) {
        System.out.print("Enter Full Name: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Confirm Password: ");
        String confirmPassword = scanner.nextLine().trim();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();

        if (InputValidator.isEmpty(fullName) || InputValidator.isEmpty(username) || InputValidator.isEmpty(password)
                || InputValidator.isEmpty(confirmPassword) || InputValidator.isEmpty(email) || InputValidator.isEmpty(phone)) {
            System.out.println("All fields are required.");
            return null;
        }

        if (!InputValidator.isValidUsername(username)) {
            System.out.println("Username must be at least 4 characters long.");
            return null;
        }

        if (usernameExists(username)) {
            System.out.println("Username already exists.");
            return null;
        }

        if (!InputValidator.isValidPassword(password)) {
            System.out.println("Password length must be at least 6 characters.");
            return null;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return null;
        }

        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return null;
        }

        if (!InputValidator.isValidPhone(phone)) {
            System.out.println("Phone number must contain exactly 10 digits.");
            return null;
        }

        User user = new User(fullName, username, password, email, phone);
        FileManager.appendLine(USER_FILE, user.toFileString());
        System.out.println("User registered successfully.");
        return user;
    }

    /**
     * Authenticates user login credentials.
     *
     * @param username entered username
     * @param password entered password
     * @return matching user if credentials are correct, otherwise null
     */
    public User loginUser(String username, String password) {
        if (InputValidator.isEmpty(username) || InputValidator.isEmpty(password)) {
            return null;
        }

        for (User user : loadUsers()) {
            if (user.getUsername().equalsIgnoreCase(username.trim()) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Counts how many complaints a user has submitted.
     * This method reads the complaints file directly.
     *
     * @param username username to count for
     * @return number of complaints submitted
     */
    public int countComplaintsForUser(String username) {
        if (InputValidator.isEmpty(username)) {
            return 0;
        }

        ComplaintService complaintService = new ComplaintService();
        return complaintService.getComplaintsByUsername(username).size();
    }
}
