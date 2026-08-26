#include "User.h"
#include <sstream>
#include <vector>

namespace project {
namespace models {

User::User() {}

User::User(std::string fullName, std::string username, std::string password, std::string email, std::string phone)
    : fullName(fullName), username(username), password(password), email(email), phone(phone) {}

std::string User::getFullName() const { return fullName; }
void User::setFullName(const std::string& fullName) { this->fullName = fullName; }

std::string User::getUsername() const { return username; }
void User::setUsername(const std::string& username) { this->username = username; }

std::string User::getPassword() const { return password; }
void User::setPassword(const std::string& password) { this->password = password; }

std::string User::getEmail() const { return email; }
void User::setEmail(const std::string& email) { this->email = email; }

std::string User::getPhone() const { return phone; }
void User::setPhone(const std::string& phone) { this->phone = phone; }

std::string User::toFileString() const {
    return fullName + "," + username + "," + password + "," + email + "," + phone;
}

User User::fromFileString(const std::string& line) {
    if (line.empty()) {
        return User(); // Returns empty if invalid
    }
    
    std::vector<std::string> parts;
    std::stringstream ss(line);
    std::string part;
    
    while (std::getline(ss, part, ',')) {
        parts.push_back(part);
    }
    
    if (parts.size() < 5) {
        return User();
    }
    
    return User(parts[0], parts[1], parts[2], parts[3], parts[4]);
}

bool User::isValid() const {
    return !username.empty();
}

}
}
