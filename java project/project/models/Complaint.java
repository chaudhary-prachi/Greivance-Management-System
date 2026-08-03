package project.models;

/**
 * Represents a complaint submitted by a citizen.
 * This class stores complaint details and supports file-based storage.
 */
public class Complaint {

    private String complaintId;
    private String username;
    private String topic;
    private String priority;
    private String description;
    private String location;
    private String department;
    private String date;
    private String time;
    private String status;
    private String adminRemark;

    /**
     * Creates an empty complaint.
     */
    public Complaint() {
    }

    /**
     * Creates a complaint with all details.
     *
     * @param complaintId unique complaint ID
     * @param username owner username
     * @param topic complaint topic
     * @param priority priority level
     * @param description complaint description
     * @param location complaint location
     * @param department concerned department
     * @param date complaint date
     * @param time complaint time
     * @param status current status
     * @param adminRemark remark from admin
     */
    public Complaint(String complaintId, String username, String topic, String priority, String description,
                     String location, String department, String date, String time, String status, String adminRemark) {
        this.complaintId = complaintId;
        this.username = username;
        this.topic = topic;
        this.priority = priority;
        this.description = description;
        this.location = location;
        this.department = department;
        this.date = date;
        this.time = time;
        this.status = status;
        this.adminRemark = adminRemark;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }

    /**
     * Converts the complaint to a pipe-separated line for complaints.txt.
     *
     * @return complaint line for file storage
     */
    public String toFileString() {
        return complaintId + "|" + username + "|" + topic + "|" + priority + "|" + description + "|"
                + location + "|" + department + "|" + date + "|" + time + "|" + status + "|" + adminRemark;
    }

    /**
     * Builds a Complaint object from one file line in complaints.txt.
     *
     * @param line stored complaint line
     * @return Complaint object, or null if the line is invalid
     */
    public static Complaint fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\|", -1);
        if (parts.length < 11) {
            return null;
        }

        return new Complaint(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[4].trim(),
                parts[5].trim(),
                parts[6].trim(),
                parts[7].trim(),
                parts[8].trim(),
                parts[9].trim(),
                parts[10].trim()
        );
    }
}
