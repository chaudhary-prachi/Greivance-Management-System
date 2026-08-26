#include "AdminService.h"
#include "../utils/FileManager.h"
#include "../utils/InputValidator.h"
#include <iostream>
#include <iomanip>
#include <algorithm>

namespace project {
namespace services {

AdminService::AdminService() {
    initializeDefaultAdmin();
}

void AdminService::initializeDefaultAdmin() {
    utils::FileManager::ensureFileExists(ADMIN_FILE);
    std::vector<std::string> lines = utils::FileManager::readAllLines(ADMIN_FILE);
    if (lines.empty()) {
        utils::FileManager::appendLine(ADMIN_FILE, DEFAULT_USERNAME + "," + DEFAULT_PASSWORD);
    }
}

bool AdminService::loginAdmin(const std::string& username, const std::string& password) {
    if (utils::InputValidator::isEmpty(username) || utils::InputValidator::isEmpty(password)) {
        return false;
    }

    std::vector<std::string> lines = utils::FileManager::readAllLines(ADMIN_FILE);
    for (const std::string& line : lines) {
        size_t commaPos = line.find(',');
        if (commaPos != std::string::npos) {
            std::string fileUsername = line.substr(0, commaPos);
            std::string filePassword = line.substr(commaPos + 1);
            
            // basic trim
            fileUsername.erase(0, fileUsername.find_first_not_of(" \t\r\n"));
            if (!fileUsername.empty()) fileUsername.erase(fileUsername.find_last_not_of(" \t\r\n") + 1);
            
            filePassword.erase(0, filePassword.find_first_not_of(" \t\r\n"));
            if (!filePassword.empty()) filePassword.erase(filePassword.find_last_not_of(" \t\r\n") + 1);

            if (fileUsername == username && filePassword == password) {
                return true;
            }
        }
    }
    return false;
}

std::vector<models::Complaint> AdminService::viewAllComplaints() {
    return complaintService.loadComplaints();
}

models::Complaint AdminService::searchComplaintById(const std::string& complaintId) {
    return complaintService.findComplaintById(complaintId);
}

std::vector<models::Complaint> AdminService::viewByStatus(const std::string& status) {
    return complaintService.getComplaintsByStatus(status);
}

std::vector<models::Complaint> AdminService::sortComplaints(const std::string& sortBy) {
    return complaintService.sortComplaints(sortBy);
}

bool AdminService::changeComplaintStatus(const std::string& complaintId, const std::string& newStatus) {
    return complaintService.updateComplaintStatus(complaintId, newStatus);
}

bool AdminService::giveAdminRemark(const std::string& complaintId, const std::string& remark) {
    return complaintService.updateAdminRemark(complaintId, remark);
}

std::map<std::string, int> AdminService::getStatistics() {
    return complaintService.getDashboardStatistics();
}

std::queue<models::Complaint> AdminService::getPendingQueue() {
    return complaintService.getPendingQueue();
}

std::vector<models::Complaint> AdminService::getRecentComplaints(int limit) {
    std::vector<models::Complaint> complaints = complaintService.loadComplaints();
    std::vector<models::Complaint> recent;
    
    for (int i = (int)complaints.size() - 1; i >= 0 && recent.size() < (size_t)limit; i--) {
        recent.push_back(complaints[i]);
    }
    
    return recent;
}

models::Complaint AdminService::getHighestPriorityComplaint() {
    std::vector<models::Complaint> complaints = complaintService.sortComplaints("priority");
    if (!complaints.empty()) {
        return complaints.front();
    }
    return models::Complaint();
}

void AdminService::printComplaintTable(const std::vector<models::Complaint>& complaints) const {
    std::cout << "--------------------------------------------------------------------------------\n";
    std::cout << std::left << std::setw(12) << "ComplaintID" 
              << std::setw(18) << "Topic" 
              << std::setw(10) << "Priority" 
              << std::setw(10) << "Status" 
              << std::setw(14) << "Department" 
              << std::setw(12) << "Date" << "\n";
    std::cout << "--------------------------------------------------------------------------------\n";

    for (const models::Complaint& complaint : complaints) {
        std::cout << std::left << std::setw(12) << complaint.getComplaintId()
                  << std::setw(18) << shorten(complaint.getTopic(), 17)
                  << std::setw(10) << complaint.getPriority()
                  << std::setw(10) << complaint.getStatus()
                  << std::setw(14) << shorten(complaint.getDepartment(), 13)
                  << std::setw(12) << complaint.getDate() << "\n";
    }
    std::cout << "--------------------------------------------------------------------------------\n";
}

void AdminService::printComplaintDetails(const models::Complaint& complaint) {
    complaintService.printComplaintDetails(complaint);
}

std::string AdminService::shorten(const std::string& text, int maxLength) const {
    if (text.length() <= (size_t)maxLength) {
        return text;
    }
    return text.substr(0, std::max(0, maxLength - 3)) + "...";
}

}
}
