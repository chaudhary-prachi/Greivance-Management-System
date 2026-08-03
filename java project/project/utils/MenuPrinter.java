package project.utils;

/**
 * Prints neat ASCII menus and messages for the terminal application.
 * Keeping display code here makes the service classes easier to read.
 */
public class MenuPrinter {

    /**
     * Prints the application welcome screen.
     */
    public static void printWelcomeScreen() {
        System.out.println("==========================================");
        System.out.println("      PUBLIC GRIEVANCE SORTING SYSTEM");
        System.out.println("==========================================");
        System.out.println();
        System.out.println("1. User Login");
        System.out.println("2. User Signup");
        System.out.println("3. Admin Login");
        System.out.println("4. Exit");
        System.out.println();
    }

    /**
     * Prints the user dashboard menu.
     */
    public static void printUserDashboard() {
        System.out.println("------------------------------------------");
        System.out.println("               USER DASHBOARD");
        System.out.println("------------------------------------------");
        System.out.println("1. Submit Complaint");
        System.out.println("2. View My Complaints");
        System.out.println("3. Search Complaint by ID");
        System.out.println("4. Edit Pending Complaint");
        System.out.println("5. Delete Pending Complaint");
        System.out.println("6. Profile");
        System.out.println("7. Logout");
        System.out.println("------------------------------------------");
    }

    /**
     * Prints the admin dashboard menu.
     */
    public static void printAdminDashboard() {
        System.out.println("------------------------------------------");
        System.out.println("              ADMIN DASHBOARD");
        System.out.println("------------------------------------------");
        System.out.println("1. View All Complaints");
        System.out.println("2. Search Complaint");
        System.out.println("3. View Pending");
        System.out.println("4. View Accepted");
        System.out.println("5. View Resolved");
        System.out.println("6. View Rejected");
        System.out.println("7. Sort Complaints");
        System.out.println("8. Change Complaint Status");
        System.out.println("9. Give Admin Remark");
        System.out.println("10. Dashboard Statistics");
        System.out.println("11. Logout");
        System.out.println("------------------------------------------");
    }

    /**
     * Prints a short greeting for a logged-in user.
     *
     * @param name user's full name
     */
    public static void printWelcomeBack(String name) {
        System.out.println("------------------------------------");
        System.out.println("Hello, " + name);
        System.out.println("Welcome Back");
        System.out.println("------------------------------------");
    }

    /**
     * Prints a goodbye message.
     */
    public static void printGoodbye() {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("   Thank you for using the system.");
        System.out.println("             Goodbye!");
        System.out.println("==========================================");
    }

    /**
     * Prints a simple loading animation style message.
     *
     * @param message message to show
     */
    public static void printLoading(String message) {
        System.out.println(message + " ...");
    }

    /**
     * Prints a section heading.
     *
     * @param title heading text
     */
    public static void printSection(String title) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("  " + title);
        System.out.println("==========================================");
    }
}
