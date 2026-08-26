#ifndef COMPLAINT_H
#define COMPLAINT_H

#include <string>

namespace project {
namespace models {

class Complaint {
private:
    std::string complaintId;
    std::string username;
    std::string topic;
    std::string priority;
    std::string description;
    std::string location;
    std::string department;
    std::string date;
    std::string time;
    std::string status;
    std::string adminRemark;

public:
    Complaint();
    Complaint(std::string complaintId, std::string username, std::string topic, std::string priority, 
              std::string description, std::string location, std::string department, 
              std::string date, std::string time, std::string status, std::string adminRemark);

    std::string getComplaintId() const;
    void setComplaintId(const std::string& id);

    std::string getUsername() const;
    void setUsername(const std::string& username);

    std::string getTopic() const;
    void setTopic(const std::string& topic);

    std::string getPriority() const;
    void setPriority(const std::string& priority);

    std::string getDescription() const;
    void setDescription(const std::string& description);

    std::string getLocation() const;
    void setLocation(const std::string& location);

    std::string getDepartment() const;
    void setDepartment(const std::string& department);

    std::string getDate() const;
    void setDate(const std::string& date);

    std::string getTime() const;
    void setTime(const std::string& time);

    std::string getStatus() const;
    void setStatus(const std::string& status);

    std::string getAdminRemark() const;
    void setAdminRemark(const std::string& adminRemark);

    std::string toFileString() const;
    static Complaint fromFileString(const std::string& line);
    bool isValid() const;
};

}
}

#endif // COMPLAINT_H
