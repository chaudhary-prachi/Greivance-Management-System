package project.utils;

import java.util.List;

/**
 * Generates unique IDs for complaints.
 * This class keeps ID creation simple and file-based.
 */
public class UniqueIDGenerator {

    private static final String COMPLAINT_FILE = "project/data/complaints.txt";
    private static final String PREFIX = "CMP";
    private static final int START_NUMBER = 1001;

    /**
     * Generates a new unique complaint ID by scanning the complaint file.
     * If the file is empty, the first ID will be CMP1001.
     *
     * @return unique complaint ID
     */
    public static String generateComplaintId() {
        FileManager.ensureFileExists(COMPLAINT_FILE);
        List<String> lines = FileManager.readAllLines(COMPLAINT_FILE);

        int maxNumber = START_NUMBER - 1;

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|", -1);
            if (parts.length > 0) {
                String id = parts[0].trim();
                if (id.startsWith(PREFIX)) {
                    try {
                        int number = Integer.parseInt(id.substring(PREFIX.length()));
                        if (number > maxNumber) {
                            maxNumber = number;
                        }
                    } catch (NumberFormatException ignored) {
                        // Ignore badly formatted IDs and continue searching.
                    }
                }
            }
        }

        return PREFIX + (maxNumber + 1);
    }

    /**
     * Checks whether a complaint ID already exists in the complaint file.
     *
     * @param complaintId complaint ID to check
     * @return true if the ID exists
     */
    public static boolean complaintIdExists(String complaintId) {
        FileManager.ensureFileExists(COMPLAINT_FILE);
        List<String> lines = FileManager.readAllLines(COMPLAINT_FILE);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|", -1);
            if (parts.length > 0 && parts[0].trim().equalsIgnoreCase(complaintId)) {
                return true;
            }
        }
        return false;
    }
}
