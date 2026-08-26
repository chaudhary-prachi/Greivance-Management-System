#ifndef COMPLAINTSERVICE_H
#define COMPLAINTSERVICE_H

#include <vector>
#include <string>
#include <queue>
#include <map>
#include "../models/Complaint.h"

namespace project {
namespace services {

class ComplaintService {
private:
    std::string COMPLAINT_FILE = "data/complaints.txt";
    std::vector<std::string> DEPARTMENTS = {
        "Electricity", "Road", "Water", "Garbage", "Police",
        "Transport", "Education", "Health", "Others"
    };

    std::string getPriorityFromChoice(const std::string& choice);
    std::string getDepartmentFromChoice(const std::string& choice);
    int getPriorityRank(const std::string& priority) const;
    void incrementCount(std::map<std::string, int>& stats, const std::string& key);

public:
    ComplaintService();
    std::vector<models::Complaint> loadComplaints();
    void saveComplaints(const std::vector<models::Complaint>& complaints);
    
    models::Complaint findComplaintById(const std::string& complaintId);
    std::vector<models::Complaint> getComplaintsByUsername(const std::string& username);
    std::vector<models::Complaint> getComplaintsByStatus(const std::string& status);
    
    models::Complaint submitComplaint(const std::string& username);
    bool editPendingComplaint(const std::string& complaintId);
    bool deletePendingComplaint(const std::string& complaintId);
    
    bool updateComplaintStatus(const std::string& complaintId, const std::string& newStatus);
    bool updateAdminRemark(const std::string& complaintId, const std::string& remark);
    
    std::vector<models::Complaint> sortComplaints(const std::string& sortBy);
    std::queue<models::Complaint> getPendingQueue();
    
    std::map<std::string, int> getDashboardStatistics();
    void printComplaintDetails(const models::Complaint& complaint);
};

}
}

#endif // COMPLAINTSERVICE_H
