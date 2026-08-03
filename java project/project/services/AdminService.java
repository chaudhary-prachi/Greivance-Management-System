package project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import project.models.Complaint;
import project.utils.FileManager;
import project.utils.InputValidator;

/**
 * Handles administrator authentication and complaint management actions.
 * The default admin account is stored in admin.txt.
 */
public class AdminService {

    private static final String ADMIN_FILE = "project/data/admin.txt";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    private final ComplaintService complaintService;

    /**
     * Creates the service and ensures admin credentials are available.
     */
    public AdminService() {
        this.complaintService = new ComplaintService();
        initializeDefaultAdmin();
    }

    /**
     * Makes sure admin.txt contains the default admin account.
     */
    private void initializeDefaultAdmin() {
        FileManager.ensureFileExists(ADMIN_FILE);
        List<String> lines = FileManager.readAllLines(ADMIN_FILE);
        if (lines.isEmpty()) {
            FileManager.appendLine(ADMIN_FILE, DEFAULT_USERNAME + "," + DEFAULT_PASSWORD);
        }
    }

    /**
     * Validates admin login.
     *
     * @param username admin username
     * @param password admin password
     * @return true if login is correct
     */
    public boolean loginAdmin(String username, String password) {
        if (InputValidator.isEmpty(username) || InputValidator.isEmpty(password)) {
            return false;
        }

        List<String> lines = FileManager.readAllLines(ADMIN_FILE);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                String fileUsername = parts[0].trim();
                String filePassword = parts[1].trim();
                if (fileUsername.equalsIgnoreCase(username.trim()) && filePassword.equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Shows all complaints.
     *
     * @return list of complaints
     */
    public List<Complaint> viewAllComplaints() {
        return complaintService.loadComplaints();
    }

    /**
     * Searches complaint by ID.
     *
     * @param complaintId complaint ID
     * @return complaint or null
     */
    public Complaint searchComplaintById(String complaintId) {
        return complaintService.findComplaintById(complaintId);
    }

    /**
     * Gets complaints filtered by status.
     *
     * @param status complaint status
     * @return filtered list
     */
    public List<Complaint> viewByStatus(String status) {
        return complaintService.getComplaintsByStatus(status);
    }

    /**
     * Sorts complaints by a selected field.
     *
     * @param sortBy sorting field
     * @return sorted list
     */
    public List<Complaint> sortComplaints(String sortBy) {
        return complaintService.sortComplaints(sortBy);
    }

    /**
     * Changes the status of a complaint.
     *
     * @param complaintId complaint ID
     * @param newStatus new status
     * @return true if updated
     */
    public boolean changeComplaintStatus(String complaintId, String newStatus) {
        return complaintService.updateComplaintStatus(complaintId, newStatus);
    }

    /**
     * Updates admin remark on a complaint.
     *
     * @param complaintId complaint ID
     * @param remark admin remark
     * @return true if updated
     */
    public boolean giveAdminRemark(String complaintId, String remark) {
        return complaintService.updateAdminRemark(complaintId, remark);
    }

    /**
     * Gets dashboard statistics for the admin.
     *
     * @return map of counts
     */
    public Map<String, Integer> getStatistics() {
        return complaintService.getDashboardStatistics();
    }

    /**
     * Returns pending complaints as a queue.
     *
     * @return queue of pending complaints
     */
    public Queue<Complaint> getPendingQueue() {
        return complaintService.getPendingQueue();
    }

    /**
     * Returns recent complaints in reverse file order.
     *
     * @param limit number of complaints to return
     * @return recent complaints list
     */
    public List<Complaint> getRecentComplaints(int limit) {
        List<Complaint> complaints = complaintService.loadComplaints();
        List<Complaint> recent = new ArrayList<>();

        for (int i = complaints.size() - 1; i >= 0 && recent.size() < limit; i--) {
            recent.add(complaints.get(i));
        }

        return recent;
    }

    /**
     * Processes the highest priority complaint first.
     *
     * @return complaint with the highest priority, or null if none exist
     */
    public Complaint getHighestPriorityComplaint() {
        java.util.PriorityQueue<Complaint> queue = complaintService.getPriorityQueue();
        return queue.poll();
    }

    /**
     * Displays a list of complaints in a simple table format.
     *
     * @param complaints complaints to display
     */
    public void printComplaintTable(List<Complaint> complaints) {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-12s %-18s %-10s %-10s %-14s %-12s%n",
                "ComplaintID", "Topic", "Priority", "Status", "Department", "Date");
        System.out.println("--------------------------------------------------------------------------------");

        for (Complaint complaint : complaints) {
            System.out.printf("%-12s %-18s %-10s %-10s %-14s %-12s%n",
                    complaint.getComplaintId(),
                    shorten(complaint.getTopic(), 17),
                    complaint.getPriority(),
                    complaint.getStatus(),
                    shorten(complaint.getDepartment(), 13),
                    complaint.getDate());
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    /**
     * Displays a single complaint in detail.
     *
     * @param complaint complaint to display
     */
    public void printComplaintDetails(Complaint complaint) {
        complaintService.printComplaintDetails(complaint);
    }

    /**
     * Reads a line of text from the console and returns trimmed input.
     *
     * @param scanner console scanner
     * @param prompt prompt text
     * @return trimmed user input
     */
    public String readInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Shortens text for table output.
     *
     * @param text input text
     * @param maxLength maximum length
     * @return shortened text
     */
    private String shorten(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, Math.max(0, maxLength - 3)) + "...";
    }
}
