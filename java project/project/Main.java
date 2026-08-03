package project;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import project.models.Complaint;
import project.models.User;
import project.services.AdminService;
import project.services.ComplaintService;
import project.services.UserService;
import project.utils.MenuPrinter;

/**
 * Main entry point for the Public Grievance Sorting System.
 * This class connects all menus and services for the terminal application.
 */
public class Main {

    /**
     * Starts the console application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            UserService userService = new UserService();
            ComplaintService complaintService = new ComplaintService();
            AdminService adminService = new AdminService();

            System.out.println();
            System.out.println("*******************************************");
            System.out.println("      PUBLIC GRIEVANCE SORTING SYSTEM");
            System.out.println("*******************************************");

            boolean running = true;
            while (running) {
                MenuPrinter.printWelcomeScreen();
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> userLogin(scanner, userService, complaintService);
                    case "2" -> userService.registerUser(scanner);
                    case "3" -> adminLogin(scanner, adminService);
                    case "4" -> {
                        MenuPrinter.printGoodbye();
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    /**
     * Handles user login.
     *
     * @param scanner console scanner
     * @param userService user service
     * @param complaintService complaint service
     */
    private static void userLogin(Scanner scanner, UserService userService, ComplaintService complaintService) {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        User user = userService.loginUser(username, password);
        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        MenuPrinter.printWelcomeBack(user.getFullName());
        userDashboard(scanner, user, userService, complaintService);
    }

    /**
     * Handles admin login.
     *
     * @param scanner console scanner
     * @param adminService admin service
     */
    private static void adminLogin(Scanner scanner, AdminService adminService) {
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine().trim();

        if (adminService.loginAdmin(username, password)) {
            System.out.println("------------------------------------");
            System.out.println("Admin Login Successful");
            System.out.println("------------------------------------");
            adminDashboard(scanner, adminService);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    /**
     * Displays and handles the user dashboard.
     *
     * @param scanner console scanner
     * @param user logged-in user
     * @param userService user service
     * @param complaintService complaint service
     */
    private static void userDashboard(Scanner scanner, User user, UserService userService, ComplaintService complaintService) {
        boolean loggedIn = true;

        while (loggedIn) {
            MenuPrinter.printUserDashboard();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> complaintService.submitComplaint(scanner, user.getUsername());
                case "2" -> showUserComplaints(user, complaintService);
                case "3" -> searchComplaint(complaintService, scanner);
                case "4" -> editPendingComplaint(complaintService, scanner, user);
                case "5" -> deletePendingComplaint(complaintService, scanner, user);
                case "6" -> showProfile(user, userService);
                case "7" -> {
                    System.out.println("Logging out...");
                    loggedIn = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays and handles the admin dashboard.
     *
     * @param scanner console scanner
     * @param adminService admin service
     */
    private static void adminDashboard(Scanner scanner, AdminService adminService) {
        boolean loggedIn = true;

        while (loggedIn) {
            MenuPrinter.printAdminDashboard();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> adminService.printComplaintTable(adminService.viewAllComplaints());
                case "2" -> {
                    Complaint complaint = adminService.searchComplaintById(readText(scanner, "Enter Complaint ID: "));
                    adminService.printComplaintDetails(complaint);
                }
                case "3" -> adminService.printComplaintTable(adminService.viewByStatus("Pending"));
                case "4" -> adminService.printComplaintTable(adminService.viewByStatus("Accepted"));
                case "5" -> adminService.printComplaintTable(adminService.viewByStatus("Resolved"));
                case "6" -> adminService.printComplaintTable(adminService.viewByStatus("Rejected"));
                case "7" -> sortComplaints(scanner, adminService);
                case "8" -> changeComplaintStatus(scanner, adminService);
                case "9" -> giveAdminRemark(scanner, adminService);
                case "10" -> showStatistics(adminService);
                case "11" -> {
                    System.out.println("Admin logging out...");
                    loggedIn = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Shows all complaints submitted by the logged-in user.
     *
     * @param user current user
     * @param complaintService complaint service
     */
    private static void showUserComplaints(User user, ComplaintService complaintService) {
        List<Complaint> complaints = complaintService.getComplaintsByUsername(user.getUsername());
        if (complaints.isEmpty()) {
            System.out.println("No complaints found.");
            return;
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("%-12s %-18s %-10s %-10s %-14s %-12s%n",
                "ComplaintID", "Topic", "Priority", "Status", "Department", "Date");
        System.out.println("------------------------------------------------------------");

        for (Complaint complaint : complaints) {
            System.out.printf("%-12s %-18s %-10s %-10s %-14s %-12s%n",
                    complaint.getComplaintId(),
                    shorten(complaint.getTopic(), 17),
                    complaint.getPriority(),
                    complaint.getStatus(),
                    shorten(complaint.getDepartment(), 13),
                    complaint.getDate());
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Searches a complaint by complaint ID.
     *
     * @param complaintService complaint service
     * @param scanner console scanner
     */
    private static void searchComplaint(ComplaintService complaintService, Scanner scanner) {
        String complaintId = readText(scanner, "Enter Complaint ID: ");
        Complaint complaint = complaintService.findComplaintById(complaintId);
        complaintService.printComplaintDetails(complaint);
    }

    /**
     * Allows a user to edit only pending complaints they submitted.
     *
     * @param complaintService complaint service
     * @param scanner console scanner
     * @param user current user
     */
    private static void editPendingComplaint(ComplaintService complaintService, Scanner scanner, User user) {
        String complaintId = readText(scanner, "Enter Complaint ID to edit: ");
        Complaint complaint = complaintService.findComplaintById(complaintId);

        if (complaint == null) {
            System.out.println("Complaint not found.");
            return;
        }

        if (!complaint.getUsername().equalsIgnoreCase(user.getUsername())) {
            System.out.println("You can only edit your own complaints.");
            return;
        }

        complaintService.editPendingComplaint(complaintId, scanner);
    }

    /**
     * Allows a user to delete only pending complaints they submitted.
     *
     * @param complaintService complaint service
     * @param scanner console scanner
     * @param user current user
     */
    private static void deletePendingComplaint(ComplaintService complaintService, Scanner scanner, User user) {
        String complaintId = readText(scanner, "Enter Complaint ID to delete: ");
        Complaint complaint = complaintService.findComplaintById(complaintId);

        if (complaint == null) {
            System.out.println("Complaint not found.");
            return;
        }

        if (!complaint.getUsername().equalsIgnoreCase(user.getUsername())) {
            System.out.println("You can only delete your own complaints.");
            return;
        }

        if (!"Pending".equalsIgnoreCase(complaint.getStatus())) {
            System.out.println("Only pending complaints can be deleted.");
            return;
        }

        System.out.print("Are you sure you want to delete this complaint? (Y/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("Y")) {
            complaintService.deletePendingComplaint(complaintId);
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    /**
     * Displays a user's profile.
     *
     * @param user current user
     * @param userService user service
     */
    private static void showProfile(User user, UserService userService) {
        System.out.println("------------------------------------------");
        System.out.println("Name                  : " + user.getFullName());
        System.out.println("Username              : " + user.getUsername());
        System.out.println("Email                 : " + user.getEmail());
        System.out.println("Phone                 : " + user.getPhone());
        System.out.println("Complaints Submitted  : " + userService.countComplaintsForUser(user.getUsername()));
        System.out.println("------------------------------------------");
    }

    /**
     * Sorts complaints using admin selection.
     *
     * @param scanner console scanner
     * @param adminService admin service
     */
    private static void sortComplaints(Scanner scanner, AdminService adminService) {
        System.out.println("Sort by:");
        System.out.println("1. Priority");
        System.out.println("2. Date");
        System.out.println("3. Department");
        System.out.println("4. Status");
        System.out.println("5. Complaint ID");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();

        String sortBy = switch (choice) {
            case "1" -> "priority";
            case "2" -> "date";
            case "3" -> "department";
            case "4" -> "status";
            default -> "complaintId";
        };

        adminService.printComplaintTable(adminService.sortComplaints(sortBy));
    }

    /**
     * Changes complaint status.
     *
     * @param scanner console scanner
     * @param adminService admin service
     */
    private static void changeComplaintStatus(Scanner scanner, AdminService adminService) {
        String complaintId = readText(scanner, "Enter Complaint ID: ");
        System.out.println("Choose Status:");
        System.out.println("1. Pending");
        System.out.println("2. Accepted");
        System.out.println("3. Resolved");
        System.out.println("4. Rejected");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();

        String status = switch (choice) {
            case "1" -> "Pending";
            case "2" -> "Accepted";
            case "3" -> "Resolved";
            case "4" -> "Rejected";
            default -> null;
        };

        if (status == null) {
            System.out.println("Invalid status choice.");
            return;
        }

        if (adminService.changeComplaintStatus(complaintId, status)) {
            System.out.println("Complaint status updated successfully.");
        } else {
            System.out.println("Complaint not found.");
        }
    }

    /**
     * Adds an admin remark to a complaint.
     *
     * @param scanner console scanner
     * @param adminService admin service
     */
    private static void giveAdminRemark(Scanner scanner, AdminService adminService) {
        String complaintId = readText(scanner, "Enter Complaint ID: ");
        String remark = readText(scanner, "Enter Admin Remark: ");

        if (adminService.giveAdminRemark(complaintId, remark)) {
            System.out.println("Admin remark saved successfully.");
        } else {
            System.out.println("Complaint not found.");
        }
    }

    /**
     * Displays dashboard statistics.
     *
     * @param adminService admin service
     */
    private static void showStatistics(AdminService adminService) {
        Map<String, Integer> stats = adminService.getStatistics();

        System.out.println("==========================================");
        System.out.println("           DASHBOARD STATISTICS");
        System.out.println("==========================================");
        System.out.println("Total Complaints : " + stats.get("Total"));
        System.out.println("Pending          : " + stats.get("Pending"));
        System.out.println("Accepted         : " + stats.get("Accepted"));
        System.out.println("Resolved         : " + stats.get("Resolved"));
        System.out.println("Rejected         : " + stats.get("Rejected"));
        System.out.println("High Priority    : " + stats.get("High"));
        System.out.println("Medium Priority  : " + stats.get("Medium"));
        System.out.println("Low Priority     : " + stats.get("Low"));
        System.out.println("==========================================");
    }

    /**
     * Reads trimmed console input using a prompt.
     *
     * @param scanner console scanner
     * @param prompt prompt text
     * @return trimmed input
     */
    private static String readText(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Shortens text for table display.
     *
     * @param text input text
     * @param maxLength maximum length
     * @return shortened text
     */
    private static String shorten(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, Math.max(0, maxLength - 3)) + "...";
    }
}
