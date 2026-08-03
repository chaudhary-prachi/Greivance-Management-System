package project.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all file operations for the application.
 * This class keeps file handling simple and reusable for beginners.
 */
public class FileManager {

    /**
     * Ensures that a file exists. If not, it creates the file.
     *
     * @param filePath path of the file
     */
    public static void ensureFileExists(String filePath) {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + filePath);
        }
    }

    /**
     * Reads all lines from a text file.
     *
     * @param filePath path of the file
     * @return list of lines read from the file
     */
    public static List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();
        ensureFileExists(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
        }

        return lines;
    }

    /**
     * Writes all lines to a text file by replacing old contents.
     *
     * @param filePath path of the file
     * @param lines data to write
     */
    public static void writeAllLines(String filePath, List<String> lines) {
        ensureFileExists(filePath);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                if (i < lines.size() - 1) {
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + filePath);
        }
    }

    /**
     * Appends a single line to the end of a text file.
     *
     * @param filePath path of the file
     * @param line line to append
     */
    public static void appendLine(String filePath, String line) {
        ensureFileExists(filePath);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            if (new File(filePath).length() > 0) {
                bw.newLine();
            }
            bw.write(line);
        } catch (IOException e) {
            System.out.println("Error appending file: " + filePath);
        }
    }

    /**
     * Deletes a file line-by-line by rewriting the file without the target line.
     *
     * @param filePath path of the file
     * @param updatedLines data to keep in the file
     */
    public static void rewriteFile(String filePath, List<String> updatedLines) {
        writeAllLines(filePath, updatedLines);
    }
}
