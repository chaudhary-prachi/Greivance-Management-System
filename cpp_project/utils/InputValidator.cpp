#include "InputValidator.h"
#include <regex>

namespace project {
namespace utils {

bool InputValidator::isEmpty(const std::string& value) {
    return value.empty() || value.find_first_not_of(" \t\n\r") == std::string::npos;
}

bool InputValidator::isValidUsername(const std::string& username) {
    if (isEmpty(username)) return false;
    std::string trimmed = username; 
    return trimmed.length() >= 4;
}

bool InputValidator::isValidPassword(const std::string& password) {
    return !isEmpty(password) && password.length() >= 6;
}

bool InputValidator::isValidEmail(const std::string& email) {
    if (isEmpty(email)) return false;
    const std::regex pattern("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    return std::regex_match(email, pattern);
}

bool InputValidator::isValidPhone(const std::string& phone) {
    if (isEmpty(phone)) return false;
    const std::regex pattern("[0-9]{10}");
    return std::regex_match(phone, pattern);
}

bool InputValidator::isNonEmptyText(const std::string& value) {
    return !isEmpty(value);
}

}
}
