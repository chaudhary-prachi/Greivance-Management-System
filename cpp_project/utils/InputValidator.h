#ifndef INPUTVALIDATOR_H
#define INPUTVALIDATOR_H

#include <string>

namespace project {
namespace utils {

class InputValidator {
public:
    static bool isEmpty(const std::string& value);
    static bool isValidUsername(const std::string& username);
    static bool isValidPassword(const std::string& password);
    static bool isValidEmail(const std::string& email);
    static bool isValidPhone(const std::string& phone);
    static bool isNonEmptyText(const std::string& value);
};

}
}

#endif // INPUTVALIDATOR_H
