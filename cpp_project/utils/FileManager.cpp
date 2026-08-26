#include "FileManager.h"
#include <fstream>
#include <iostream>
#include <filesystem>

namespace project {
namespace utils {

void FileManager::ensureFileExists(const std::string& filePath) {
    std::filesystem::path path(filePath);
    if (path.has_parent_path()) {
        std::filesystem::create_directories(path.parent_path());
    }
    if (!std::filesystem::exists(path)) {
        std::ofstream file(path);
        if (!file.is_open()) {
            std::cerr << "Error creating file: " << filePath << std::endl;
        }
    }
}

std::vector<std::string> FileManager::readAllLines(const std::string& filePath) {
    ensureFileExists(filePath);
    std::vector<std::string> lines;
    std::ifstream file(filePath);
    if (file.is_open()) {
        std::string line;
        while (std::getline(file, line)) {
            // Trim carriage return if exists (for windows compat)
            if (!line.empty() && line.back() == '\r') {
                line.pop_back();
            }
            if (!line.empty()) {
                lines.push_back(line);
            }
        }
    } else {
        std::cerr << "Error reading file: " << filePath << std::endl;
    }
    return lines;
}

void FileManager::writeAllLines(const std::string& filePath, const std::vector<std::string>& lines) {
    ensureFileExists(filePath);
    std::ofstream file(filePath, std::ios::trunc);
    if (file.is_open()) {
        for (const auto& line : lines) {
            file << line << "\n";
        }
    } else {
        std::cerr << "Error writing file: " << filePath << std::endl;
    }
}

void FileManager::appendLine(const std::string& filePath, const std::string& line) {
    ensureFileExists(filePath);
    std::ofstream file(filePath, std::ios::app);
    if (file.is_open()) {
        file << line << "\n";
    } else {
        std::cerr << "Error appending file: " << filePath << std::endl;
    }
}

void FileManager::rewriteFile(const std::string& filePath, const std::vector<std::string>& updatedLines) {
    writeAllLines(filePath, updatedLines);
}

}
}
