#include "Complaint.h"
#include <sstream>
#include <vector>

namespace project {
namespace models {

Complaint::Complaint() : complaintId("") {}

Complaint::Complaint(std::string complaintId, std::string username, std::string topic, std::string priority,
                     std::string description, std::string location, std::string department,
                     std::string date, std::string time, std::string status, std::string adminRemark)
    : complaintId(complaintId), username(username), topic(topic), priority(priority),
      description(description), location(location), department(department), date(date),
      time(time), status(status), adminRemark(adminRemark) {}

std::string Complaint::getComplaintId() const { return complaintId; }
void Complaint::setComplaintId(const std::string& id) { complaintId = id; }

std::string Complaint::getUsername() const { return username; }
void Complaint::setUsername(const std::string& username) { this->username = username; }

std::string Complaint::getTopic() const { return topic; }
void Complaint::setTopic(const std::string& topic) { this->topic = topic; }

std::string Complaint::getPriority() const { return priority; }
void Complaint::setPriority(const std::string& priority) { this->priority = priority; }

std::string Complaint::getDescription() const { return description; }
void Complaint::setDescription(const std::string& description) { this->description = description; }

std::string Complaint::getLocation() const { return location; }
void Complaint::setLocation(const std::string& location) { this->location = location; }

std::string Complaint::getDepartment() const { return department; }
void Complaint::setDepartment(const std::string& department) { this->department = department; }

std::string Complaint::getDate() const { return date; }
void Complaint::setDate(const std::string& date) { this->date = date; }

std::string Complaint::getTime() const { return time; }
void Complaint::setTime(const std::string& time) { this->time = time; }

std::string Complaint::getStatus() const { return status; }
void Complaint::setStatus(const std::string& status) { this->status = status; }

std::string Complaint::getAdminRemark() const { return adminRemark; }
void Complaint::setAdminRemark(const std::string& adminRemark) { this->adminRemark = adminRemark; }

std::string Complaint::toFileString() const {
    return complaintId + "|" + username + "|" + topic + "|" + priority + "|" + description + "|" +
           location + "|" + department + "|" + date + "|" + time + "|" + status + "|" + adminRemark;
}

Complaint Complaint::fromFileString(const std::string& line) {
    if (line.empty()) return Complaint();

    std::vector<std::string> parts;
    std::stringstream ss(line);
    std::string part;

    while (std::getline(ss, part, '|')) {
        parts.push_back(part);
    }
    // Handle trailing empty parts
    if (!line.empty() && line.back() == '|') {
        parts.push_back("");
    }

    if (parts.size() < 11) return Complaint();

    return Complaint(parts[0], parts[1], parts[2], parts[3], parts[4],
                     parts[5], parts[6], parts[7], parts[8], parts[9], parts[10]);
}

bool Complaint::isValid() const {
    return !complaintId.empty();
}

}
}
