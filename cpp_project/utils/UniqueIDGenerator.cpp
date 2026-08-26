#include "UniqueIDGenerator.h"
#include "FileManager.h"
#include <vector>
#include <sstream>
#include <iostream>

namespace project {
namespace utils {

const std::string UniqueIDGenerator::COMPLAINT_FILE = "data/complaints.txt";
const std::string UniqueIDGenerator::PREFIX = "CMP";

std::string UniqueIDGenerator::generateComplaintId() {
    FileManager::ensureFileExists(COMPLAINT_FILE);
    std::vector<std::string> lines = FileManager::readAllLines(COMPLAINT_FILE);

    int maxNumber = START_NUMBER - 1;

    for (const std::string& line : lines) {
        if (line.empty()) continue;

        std::vector<std::string> parts;
        std::stringstream ss(line);
        std::string part;
        while (std::getline(ss, part, '|')) {
            parts.push_back(part);
        }

        if (!parts.empty()) {
            std::string id = parts[0];
            if (id.find(PREFIX) == 0) {
                try {
                    int number = std::stoi(id.substr(PREFIX.length()));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (const std::exception& e) {
                    // Ignore badly formatted IDs and continue searching.
                }
            }
        }
    }

    return PREFIX + std::to_string(maxNumber + 1);
}

bool UniqueIDGenerator::complaintIdExists(const std::string& complaintId) {
    FileManager::ensureFileExists(COMPLAINT_FILE);
    std::vector<std::string> lines = FileManager::readAllLines(COMPLAINT_FILE);

    for (const std::string& line : lines) {
        if (line.empty()) continue;
        
        std::vector<std::string> parts;
        std::stringstream ss(line);
        std::string part;
        while (std::getline(ss, part, '|')) {
            parts.push_back(part);
        }
        
        if (!parts.empty() && parts[0] == complaintId) {
            return true;
        }
    }
    return false;
}

}
}
