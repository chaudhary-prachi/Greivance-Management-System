package project.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import project.models.Complaint;
import project.utils.FileManager;
import project.utils.InputValidator;
import project.utils.UniqueIDGenerator;

/**
 * Handles all complaint-related operations.
 * This class uses file handling, searching, sorting, queue, and priority queue logic.
 */
public class ComplaintService {

    private static final String COMPLAINT_FILE = "project/data/complaints.txt";
    private static final List<String> DEPARTMENTS = List.of(
            "Electricity", "Road", "Water", "Garbage", "Police",
            "Transport", "Education", "Health", "Others"
    );

    /**
     * Creates the service and ensures the complaint file exists.
     */
    public ComplaintService() {
        FileManager.ensureFileExists(COMPLAINT_FILE);
    }

    /**
     * Loads all complaints from complaints.txt.
     *
     * @return list of complaints
     */
    public List<Complaint> loadComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        List<String> lines = FileManager.readAllLines(COMPLAINT_FILE);

        for (String line : lines) {
            Complaint complaint = Complaint.fromFileString(line);
            if (complaint != null) {
                complaints.add(complaint);
            }
        }
        return complaints;
    }

    /**
     * Saves all complaints back to the file.
     *
     * @param complaints complaint list to save
     */
    public void saveComplaints(List<Complaint> complaints) {
        List<String> lines = new ArrayList<>();
        for (Complaint complaint : complaints) {
            lines.add(complaint.toFileString());
        }
        FileManager.writeAllLines(COMPLAINT_FILE, lines);
    }

    /**
     * Searches for a complaint using complaint ID.
     * This is a linear search.
     *
     * @param complaintId complaint ID to search
     * @return matching complaint or null
     */
    public Complaint findComplaintById(String complaintId) {
        if (InputValidator.isEmpty(complaintId)) {
            return null;
        }

        for (Complaint complaint : loadComplaints()) {
            if (complaint.getComplaintId().equalsIgnoreCase(complaintId.trim())) {
                return complaint;
            }
        }
        return null;
    }

    /**
     * Gets all complaints submitted by a specific username.
     *
     * @param username username to search
     * @return user complaints
     */
    public List<Complaint> getComplaintsByUsername(String username) {
        List<Complaint> result = new ArrayList<>();
        if (InputValidator.isEmpty(username)) {
            return result;
        }

        for (Complaint complaint : loadComplaints()) {
            if (complaint.getUsername().equalsIgnoreCase(username.trim())) {
                result.add(complaint);
            }
        }
        return result;
    }

    /**
     * Gets complaints filtered by status.
     *
     * @param status complaint status
     * @return filtered list
     */
    public List<Complaint> getComplaintsByStatus(String status) {
        List<Complaint> result = new ArrayList<>();
        if (InputValidator.isEmpty(status)) {
            return result;
        }

        for (Complaint complaint : loadComplaints()) {
            if (complaint.getStatus().equalsIgnoreCase(status.trim())) {
                result.add(complaint);
            }
        }
        return result;
    }

    /**
     * Submits a new complaint for the logged-in user.
     *
     * @param scanner scanner for console input
     * @param username logged-in username
     * @return created complaint, or null if submission fails
     */
    public Complaint submitComplaint(Scanner scanner, String username) {
        if (InputValidator.isEmpty(username)) {
            System.out.println("Invalid user.");
            return null;
        }

        System.out.print("Enter Topic: ");
        String topic = scanner.nextLine().trim();

        System.out.println("Choose Priority:");
        System.out.println("1. Low");
        System.out.println("2. Medium");
        System.out.println("3. High");
        System.out.print("Enter choice: ");
        String priorityChoice = scanner.nextLine().trim();

        System.out.print("Enter Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter Location: ");
        String location = scanner.nextLine().trim();

        System.out.println("Choose Department:");
        for (int i = 0; i < DEPARTMENTS.size(); i++) {
            System.out.println((i + 1) + ". " + DEPARTMENTS.get(i));
        }
        System.out.print("Enter choice: ");
        String departmentChoice = scanner.nextLine().trim();

        if (InputValidator.isEmpty(topic) || InputValidator.isEmpty(description) || InputValidator.isEmpty(location)) {
            System.out.println("All fields are required.");
            return null;
        }

        String priority = getPriorityFromChoice(priorityChoice);
        if (priority == null) {
            System.out.println("Invalid priority choice.");
            return null;
        }

        String department = getDepartmentFromChoice(departmentChoice);
        if (department == null) {
            System.out.println("Invalid department choice.");
            return null;
        }

        String complaintId = UniqueIDGenerator.generateComplaintId();
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String status = "Pending";
        String adminRemark = "Not Reviewed";

        Complaint complaint = new Complaint(complaintId, username.trim(), topic, priority, description,
                location, department, date, time, status, adminRemark);

        FileManager.appendLine(COMPLAINT_FILE, complaint.toFileString());
        System.out.println("Complaint Submitted Successfully");
        System.out.println("Complaint ID : " + complaintId);
        return complaint;
    }

    /**
     * Updates only a pending complaint.
     *
     * @param complaintId complaint ID to edit
     * @param scanner console scanner
     * @return true if updated successfully
     */
    public boolean editPendingComplaint(String complaintId, Scanner scanner) {
        List<Complaint> complaints = loadComplaints();
        boolean updated = false;

        for (Complaint complaint : complaints) {
            if (complaint.getComplaintId().equalsIgnoreCase(complaintId.trim())) {
                if (!"Pending".equalsIgnoreCase(complaint.getStatus())) {
                    System.out.println("Only pending complaints can be edited.");
                    return false;
                }

                System.out.print("Enter New Topic: ");
                String topic = scanner.nextLine().trim();

                System.out.println("Choose New Priority:");
                System.out.println("1. Low");
                System.out.println("2. Medium");
                System.out.println("3. High");
                System.out.print("Enter choice: ");
                String priorityChoice = scanner.nextLine().trim();

                System.out.print("Enter New Description: ");
                String description = scanner.nextLine().trim();

                System.out.print("Enter New Location: ");
                String location = scanner.nextLine().trim();

                System.out.println("Choose New Department:");
                for (int i = 0; i < DEPARTMENTS.size(); i++) {
                    System.out.println((i + 1) + ". " + DEPARTMENTS.get(i));
                }
                System.out.print("Enter choice: ");
                String departmentChoice = scanner.nextLine().trim();

                String priority = getPriorityFromChoice(priorityChoice);
                String department = getDepartmentFromChoice(departmentChoice);

                if (InputValidator.isEmpty(topic) || InputValidator.isEmpty(description) || InputValidator.isEmpty(location)
                        || priority == null || department == null) {
                    System.out.println("Invalid input. Complaint not updated.");
                    return false;
                }

                complaint.setTopic(topic);
                complaint.setPriority(priority);
                complaint.setDescription(description);
                complaint.setLocation(location);
                complaint.setDepartment(department);
                updated = true;
                break;
            }
        }

        if (updated) {
            saveComplaints(complaints);
            System.out.println("Complaint updated successfully.");
        } else {
            System.out.println("Complaint not found.");
        }
        return updated;
    }

    /**
     * Deletes a complaint only if it is still pending.
     *
     * @param complaintId complaint ID to delete
     * @return true if deleted
     */
    public boolean deletePendingComplaint(String complaintId) {
        List<Complaint> complaints = loadComplaints();
        List<Complaint> updatedList = new ArrayList<>();
        boolean deleted = false;

        for (Complaint complaint : complaints) {
            if (complaint.getComplaintId().equalsIgnoreCase(complaintId.trim())) {
                if (!"Pending".equalsIgnoreCase(complaint.getStatus())) {
                    System.out.println("Only pending complaints can be deleted.");
                    return false;
                }
                deleted = true;
                continue;
            }
            updatedList.add(complaint);
        }

        if (deleted) {
            saveComplaints(updatedList);
            System.out.println("Complaint deleted successfully.");
        } else {
            System.out.println("Complaint not found.");
        }
        return deleted;
    }

    /**
     * Updates status for a complaint.
     *
     * @param complaintId complaint ID
     * @param newStatus new status value
     * @return true if updated
     */
    public boolean updateComplaintStatus(String complaintId, String newStatus) {
        List<Complaint> complaints = loadComplaints();
        boolean updated = false;

        for (Complaint complaint : complaints) {
            if (complaint.getComplaintId().equalsIgnoreCase(complaintId.trim())) {
                complaint.setStatus(newStatus);
                updated = true;
                break;
            }
        }

        if (updated) {
            saveComplaints(complaints);
        }
        return updated;
    }

    /**
     * Updates the admin remark for a complaint.
     *
     * @param complaintId complaint ID
     * @param remark admin remark text
     * @return true if updated
     */
    public boolean updateAdminRemark(String complaintId, String remark) {
        List<Complaint> complaints = loadComplaints();
        boolean updated = false;

        for (Complaint complaint : complaints) {
            if (complaint.getComplaintId().equalsIgnoreCase(complaintId.trim())) {
                complaint.setAdminRemark(remark);
                updated = true;
                break;
            }
        }

        if (updated) {
            saveComplaints(complaints);
        }
        return updated;
    }

    /**
     * Returns all complaints sorted by a selected field.
     *
     * @param sortBy field name
     * @return sorted complaints
     */
    public List<Complaint> sortComplaints(String sortBy) {
        List<Complaint> complaints = loadComplaints();

        Comparator<Complaint> comparator;
        if ("priority".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparingInt(c -> getPriorityRank(c.getPriority()));
        } else if ("date".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Complaint::getDate).thenComparing(Complaint::getTime);
        } else if ("department".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Complaint::getDepartment, String.CASE_INSENSITIVE_ORDER);
        } else if ("status".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Complaint::getStatus, String.CASE_INSENSITIVE_ORDER);
        } else {
            comparator = Comparator.comparing(Complaint::getComplaintId, String.CASE_INSENSITIVE_ORDER);
        }

        Collections.sort(complaints, comparator);
        return complaints;
    }

    /**
     * Returns pending complaints in a queue.
     *
     * @return queue of pending complaints
     */
    public Queue<Complaint> getPendingQueue() {
        Queue<Complaint> queue = new LinkedList<>();
        for (Complaint complaint : loadComplaints()) {
            if ("Pending".equalsIgnoreCase(complaint.getStatus())) {
                queue.offer(complaint);
            }
        }
        return queue;
    }

    /**
     * Returns complaints in priority queue order.
     * Highest priority is processed first.
     *
     * @return priority queue of complaints
     */
    public PriorityQueue<Complaint> getPriorityQueue() {
        PriorityQueue<Complaint> queue = new PriorityQueue<>(Comparator
                .comparingInt((Complaint c) -> getPriorityRank(c.getPriority())));

        queue.addAll(loadComplaints());
        return queue;
    }

    /**
     * Returns dashboard statistics.
     *
     * @return map of summary counts
     */
    public Map<String, Integer> getDashboardStatistics() {
        List<Complaint> complaints = loadComplaints();
        Map<String, Integer> stats = new HashMap<>();

        stats.put("Total", complaints.size());
        stats.put("Pending", 0);
        stats.put("Accepted", 0);
        stats.put("Resolved", 0);
        stats.put("Rejected", 0);
        stats.put("High", 0);
        stats.put("Medium", 0);
        stats.put("Low", 0);

        for (Complaint complaint : complaints) {
            incrementCount(stats, complaint.getStatus());
            incrementCount(stats, complaint.getPriority());
        }

        return stats;
    }

    /**
     * Prints a full complaint detail on the console.
     *
     * @param complaint complaint to display
     */
    public void printComplaintDetails(Complaint complaint) {
        if (complaint == null) {
            System.out.println("Complaint Not Found");
            return;
        }

        System.out.println("--------------------------------------------------");
        System.out.println("Complaint ID   : " + complaint.getComplaintId());
        System.out.println("Username       : " + complaint.getUsername());
        System.out.println("Topic          : " + complaint.getTopic());
        System.out.println("Priority       : " + complaint.getPriority());
        System.out.println("Description    : " + complaint.getDescription());
        System.out.println("Location       : " + complaint.getLocation());
        System.out.println("Department     : " + complaint.getDepartment());
        System.out.println("Date           : " + complaint.getDate());
        System.out.println("Time           : " + complaint.getTime());
        System.out.println("Status         : " + complaint.getStatus());
        System.out.println("Admin Remark   : " + complaint.getAdminRemark());
        System.out.println("--------------------------------------------------");
    }

    /**
     * Converts a priority selection into text.
     *
     * @param choice menu choice
     * @return Low, Medium, High, or null
     */
    private String getPriorityFromChoice(String choice) {
        if ("1".equals(choice)) {
            return "Low";
        }
        if ("2".equals(choice)) {
            return "Medium";
        }
        if ("3".equals(choice)) {
            return "High";
        }
        return null;
    }

    /**
     * Converts a department selection into text.
     *
     * @param choice menu choice
     * @return department name or null
     */
    private String getDepartmentFromChoice(String choice) {
        try {
            int index = Integer.parseInt(choice);
            if (index >= 1 && index <= DEPARTMENTS.size()) {
                return DEPARTMENTS.get(index - 1);
            }
        } catch (NumberFormatException ignored) {
            // invalid choice, handled by returning null
        }
        return null;
    }

    /**
     * Returns a numeric rank for priority sorting.
     * High comes first, then Medium, then Low.
     *
     * @param priority priority text
     * @return rank number
     */
    private int getPriorityRank(String priority) {
        if ("High".equalsIgnoreCase(priority)) {
            return 1;
        }
        if ("Medium".equalsIgnoreCase(priority)) {
            return 2;
        }
        return 3;
    }

    /**
     * Increments a count in the statistics map.
     *
     * @param stats stats map
     * @param key key to increment
     */
    private void incrementCount(Map<String, Integer> stats, String key) {
        if (key == null) {
            return;
        }

        if (stats.containsKey(key)) {
            stats.put(key, stats.get(key) + 1);
        }
    }
}
