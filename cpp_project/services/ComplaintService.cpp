#include "ComplaintService.h"
#include "../utils/FileManager.h"
#include "../utils/InputValidator.h"
#include "../utils/UniqueIDGenerator.h"
#include <iostream>
#include <algorithm>
#include <chrono>
#include <ctime>
#include <iomanip>
#include <sstream>

namespace project {
namespace services {

ComplaintService::ComplaintService() {
    utils::FileManager::ensureFileExists(COMPLAINT_FILE);
}

std::vector<models::Complaint> ComplaintService::loadComplaints() {
    std::vector<models::Complaint> complaints;
    std::vector<std::string> lines = utils::FileManager::readAllLines(COMPLAINT_FILE);

    for (const std::string& line : lines) {
        models::Complaint complaint = models::Complaint::fromFileString(line);
        if (complaint.isValid()) {
            complaints.push_back(complaint);
        }
    }
    return complaints;
}

void ComplaintService::saveComplaints(const std::vector<models::Complaint>& complaints) {
    std::vector<std::string> lines;
    for (const models::Complaint& complaint : complaints) {
        lines.push_back(complaint.toFileString());
    }
    utils::FileManager::writeAllLines(COMPLAINT_FILE, lines);
}

models::Complaint ComplaintService::findComplaintById(const std::string& complaintId) {
    if (utils::InputValidator::isEmpty(complaintId)) return models::Complaint();

    std::vector<models::Complaint> complaints = loadComplaints();
    for (const models::Complaint& complaint : complaints) {
        if (complaint.getComplaintId() == complaintId) {
            return complaint;
        }
    }
    return models::Complaint();
}

std::vector<models::Complaint> ComplaintService::getComplaintsByUsername(const std::string& username) {
    std::vector<models::Complaint> result;
    if (utils::InputValidator::isEmpty(username)) return result;

    std::vector<models::Complaint> complaints = loadComplaints();
    for (const models::Complaint& complaint : complaints) {
        if (complaint.getUsername() == username) {
            result.push_back(complaint);
        }
    }
    return result;
}

std::vector<models::Complaint> ComplaintService::getComplaintsByStatus(const std::string& status) {
    std::vector<models::Complaint> result;
    if (utils::InputValidator::isEmpty(status)) return result;

    std::vector<models::Complaint> complaints = loadComplaints();
    for (const models::Complaint& complaint : complaints) {
        if (complaint.getStatus() == status) {
            result.push_back(complaint);
        }
    }
    return result;
}

models::Complaint ComplaintService::submitComplaint(const std::string& username) {
    if (utils::InputValidator::isEmpty(username)) {
        std::cout << "Invalid user.\n";
        return models::Complaint();
    }

    std::string topic, priorityChoice, description, location, departmentChoice;

    std::cout << "Enter Topic: ";
    std::getline(std::cin, topic);

    std::cout << "Choose Priority:\n1. Low\n2. Medium\n3. High\nEnter choice: ";
    std::getline(std::cin, priorityChoice);

    std::cout << "Enter Description: ";
    std::getline(std::cin, description);

    std::cout << "Enter Location: ";
    std::getline(std::cin, location);

    std::cout << "Choose Department:\n";
    for (size_t i = 0; i < DEPARTMENTS.size(); i++) {
        std::cout << (i + 1) << ". " << DEPARTMENTS[i] << "\n";
    }
    std::cout << "Enter choice: ";
    std::getline(std::cin, departmentChoice);

    if (utils::InputValidator::isEmpty(topic) || utils::InputValidator::isEmpty(description) || utils::InputValidator::isEmpty(location)) {
        std::cout << "All fields are required.\n";
        return models::Complaint();
    }

    std::string priority = getPriorityFromChoice(priorityChoice);
    if (priority.empty()) {
        std::cout << "Invalid priority choice.\n";
        return models::Complaint();
    }

    std::string department = getDepartmentFromChoice(departmentChoice);
    if (department.empty()) {
        std::cout << "Invalid department choice.\n";
        return models::Complaint();
    }

    std::string complaintId = utils::UniqueIDGenerator::generateComplaintId();
    
    auto now = std::chrono::system_clock::now();
    std::time_t now_time = std::chrono::system_clock::to_time_t(now);
    std::tm* local_time = std::localtime(&now_time);
    
    std::stringstream date_ss, time_ss;
    date_ss << std::put_time(local_time, "%Y-%m-%d");
    time_ss << std::put_time(local_time, "%H:%M:%S");

    models::Complaint complaint(complaintId, username, topic, priority, description, location,
                                department, date_ss.str(), time_ss.str(), "Pending", "Not Reviewed");

    utils::FileManager::appendLine(COMPLAINT_FILE, complaint.toFileString());
    std::cout << "Complaint Submitted Successfully\n";
    std::cout << "Complaint ID : " << complaintId << "\n";
    return complaint;
}

bool ComplaintService::editPendingComplaint(const std::string& complaintId) {
    std::vector<models::Complaint> complaints = loadComplaints();
    bool updated = false;

    for (models::Complaint& complaint : complaints) {
        if (complaint.getComplaintId() == complaintId) {
            if (complaint.getStatus() != "Pending") {
                std::cout << "Only pending complaints can be edited.\n";
                return false;
            }

            std::string topic, priorityChoice, description, location, departmentChoice;

            std::cout << "Enter New Topic: ";
            std::getline(std::cin, topic);

            std::cout << "Choose New Priority:\n1. Low\n2. Medium\n3. High\nEnter choice: ";
            std::getline(std::cin, priorityChoice);

            std::cout << "Enter New Description: ";
            std::getline(std::cin, description);

            std::cout << "Enter New Location: ";
            std::getline(std::cin, location);

            std::cout << "Choose New Department:\n";
            for (size_t i = 0; i < DEPARTMENTS.size(); i++) {
                std::cout << (i + 1) << ". " << DEPARTMENTS[i] << "\n";
            }
            std::cout << "Enter choice: ";
            std::getline(std::cin, departmentChoice);

            std::string priority = getPriorityFromChoice(priorityChoice);
            std::string department = getDepartmentFromChoice(departmentChoice);

            if (utils::InputValidator::isEmpty(topic) || utils::InputValidator::isEmpty(description) || 
                utils::InputValidator::isEmpty(location) || priority.empty() || department.empty()) {
                std::cout << "Invalid input. Complaint not updated.\n";
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
        std::cout << "Complaint updated successfully.\n";
    } else {
        std::cout << "Complaint not found.\n";
    }
    return updated;
}

bool ComplaintService::deletePendingComplaint(const std::string& complaintId) {
    std::vector<models::Complaint> complaints = loadComplaints();
    std::vector<models::Complaint> updatedList;
    bool deleted = false;

    for (const models::Complaint& complaint : complaints) {
        if (complaint.getComplaintId() == complaintId) {
            if (complaint.getStatus() != "Pending") {
                std::cout << "Only pending complaints can be deleted.\n";
                return false;
            }
            deleted = true;
            continue;
        }
        updatedList.push_back(complaint);
    }

    if (deleted) {
        saveComplaints(updatedList);
        std::cout << "Complaint deleted successfully.\n";
    } else {
        std::cout << "Complaint not found.\n";
    }
    return deleted;
}

bool ComplaintService::updateComplaintStatus(const std::string& complaintId, const std::string& newStatus) {
    std::vector<models::Complaint> complaints = loadComplaints();
    bool updated = false;

    for (models::Complaint& complaint : complaints) {
        if (complaint.getComplaintId() == complaintId) {
            complaint.setStatus(newStatus);
            updated = true;
            break;
        }
    }

    if (updated) saveComplaints(complaints);
    return updated;
}

bool ComplaintService::updateAdminRemark(const std::string& complaintId, const std::string& remark) {
    std::vector<models::Complaint> complaints = loadComplaints();
    bool updated = false;

    for (models::Complaint& complaint : complaints) {
        if (complaint.getComplaintId() == complaintId) {
            complaint.setAdminRemark(remark);
            updated = true;
            break;
        }
    }

    if (updated) saveComplaints(complaints);
    return updated;
}

std::vector<models::Complaint> ComplaintService::sortComplaints(const std::string& sortBy) {
    std::vector<models::Complaint> complaints = loadComplaints();

    if (sortBy == "priority") {
        std::sort(complaints.begin(), complaints.end(), [this](const models::Complaint& a, const models::Complaint& b) {
            return getPriorityRank(a.getPriority()) < getPriorityRank(b.getPriority());
        });
    } else if (sortBy == "date") {
        std::sort(complaints.begin(), complaints.end(), [](const models::Complaint& a, const models::Complaint& b) {
            if (a.getDate() == b.getDate()) return a.getTime() < b.getTime();
            return a.getDate() < b.getDate();
        });
    } else if (sortBy == "department") {
        std::sort(complaints.begin(), complaints.end(), [](const models::Complaint& a, const models::Complaint& b) {
            return a.getDepartment() < b.getDepartment();
        });
    } else if (sortBy == "status") {
        std::sort(complaints.begin(), complaints.end(), [](const models::Complaint& a, const models::Complaint& b) {
            return a.getStatus() < b.getStatus();
        });
    } else {
        std::sort(complaints.begin(), complaints.end(), [](const models::Complaint& a, const models::Complaint& b) {
            return a.getComplaintId() < b.getComplaintId();
        });
    }
    
    return complaints;
}

std::queue<models::Complaint> ComplaintService::getPendingQueue() {
    std::queue<models::Complaint> q;
    std::vector<models::Complaint> complaints = loadComplaints();
    for (const models::Complaint& c : complaints) {
        if (c.getStatus() == "Pending") {
            q.push(c);
        }
    }
    return q;
}

std::map<std::string, int> ComplaintService::getDashboardStatistics() {
    std::vector<models::Complaint> complaints = loadComplaints();
    std::map<std::string, int> stats = {
        {"Total", (int)complaints.size()},
        {"Pending", 0}, {"Accepted", 0}, {"Resolved", 0}, {"Rejected", 0},
        {"High", 0}, {"Medium", 0}, {"Low", 0}
    };

    for (const models::Complaint& c : complaints) {
        incrementCount(stats, c.getStatus());
        incrementCount(stats, c.getPriority());
    }

    return stats;
}

void ComplaintService::printComplaintDetails(const models::Complaint& complaint) {
    if (!complaint.isValid()) {
        std::cout << "Complaint Not Found\n";
        return;
    }

    std::cout << "--------------------------------------------------\n";
    std::cout << "Complaint ID   : " << complaint.getComplaintId() << "\n";
    std::cout << "Username       : " << complaint.getUsername() << "\n";
    std::cout << "Topic          : " << complaint.getTopic() << "\n";
    std::cout << "Priority       : " << complaint.getPriority() << "\n";
    std::cout << "Description    : " << complaint.getDescription() << "\n";
    std::cout << "Location       : " << complaint.getLocation() << "\n";
    std::cout << "Department     : " << complaint.getDepartment() << "\n";
    std::cout << "Date           : " << complaint.getDate() << "\n";
    std::cout << "Time           : " << complaint.getTime() << "\n";
    std::cout << "Status         : " << complaint.getStatus() << "\n";
    std::cout << "Admin Remark   : " << complaint.getAdminRemark() << "\n";
    std::cout << "--------------------------------------------------\n";
}

std::string ComplaintService::getPriorityFromChoice(const std::string& choice) {
    if (choice == "1") return "Low";
    if (choice == "2") return "Medium";
    if (choice == "3") return "High";
    return "";
}

std::string ComplaintService::getDepartmentFromChoice(const std::string& choice) {
    try {
        int idx = std::stoi(choice);
        if (idx >= 1 && idx <= (int)DEPARTMENTS.size()) {
            return DEPARTMENTS[idx - 1];
        }
    } catch (...) {}
    return "";
}

int ComplaintService::getPriorityRank(const std::string& priority) const {
    if (priority == "High") return 1;
    if (priority == "Medium") return 2;
    return 3;
}

void ComplaintService::incrementCount(std::map<std::string, int>& stats, const std::string& key) {
    if (stats.find(key) != stats.end()) {
        stats[key]++;
    }
}

}
}
