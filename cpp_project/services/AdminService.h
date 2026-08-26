#ifndef ADMINSERVICE_H
#define ADMINSERVICE_H

#include <string>
#include <vector>
#include <queue>
#include <map>
#include "ComplaintService.h"

namespace project {
namespace services {

class AdminService {
private:
    std::string ADMIN_FILE = "data/admin.txt";
    std::string DEFAULT_USERNAME = "admin";
    std::string DEFAULT_PASSWORD = "admin123";
    
    ComplaintService complaintService;

    void initializeDefaultAdmin();
    std::string shorten(const std::string& text, int maxLength) const;

public:
    AdminService();
    
    bool loginAdmin(const std::string& username, const std::string& password);
    
    std::vector<models::Complaint> viewAllComplaints();
    models::Complaint searchComplaintById(const std::string& complaintId);
    std::vector<models::Complaint> viewByStatus(const std::string& status);
    std::vector<models::Complaint> sortComplaints(const std::string& sortBy);
    
    bool changeComplaintStatus(const std::string& complaintId, const std::string& newStatus);
    bool giveAdminRemark(const std::string& complaintId, const std::string& remark);
    
    std::map<std::string, int> getStatistics();
    std::queue<models::Complaint> getPendingQueue();
    std::vector<models::Complaint> getRecentComplaints(int limit);
    models::Complaint getHighestPriorityComplaint();
    
    void printComplaintTable(const std::vector<models::Complaint>& complaints) const;
    void printComplaintDetails(const models::Complaint& complaint);
};

}
}

#endif // ADMINSERVICE_H
