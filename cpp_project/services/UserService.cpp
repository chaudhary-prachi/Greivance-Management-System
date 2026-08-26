#include "UserService.h"
#include "ComplaintService.h"
#include "../utils/FileManager.h"
#include "../utils/InputValidator.h"
#include <iostream>
#include <string>

namespace project {
namespace services {

UserService::UserService() {
    utils::FileManager::ensureFileExists(USER_FILE);
}

std::vector<models::User> UserService::loadUsers() {
    std::vector<models::User> users;
    std::vector<std::string> lines = utils::FileManager::readAllLines(USER_FILE);

    for (const std::string& line : lines) {
        models::User user = models::User::fromFileString(line);
        if (user.isValid()) {
            users.push_back(user);
        }
    }
    return users;
}

models::User UserService::findUserByUsername(const std::string& username) {
    if (utils::InputValidator::isEmpty(username)) {
        return models::User();
    }

    std::vector<models::User> users = loadUsers();
    for (const models::User& user : users) {
        if (user.getUsername() == username) {
            return user;
        }
    }
    return models::User();
}

bool UserService::usernameExists(const std::string& username) {
    return findUserByUsername(username).isValid();
}

models::User UserService::registerUser() {
    std::string fullName, username, password, confirmPassword, email, phone;
    
    std::cout << "Enter Full Name: ";
    std::getline(std::cin, fullName);

    std::cout << "Enter Username: ";
    std::getline(std::cin, username);

    std::cout << "Enter Password: ";
    std::getline(std::cin, password);

    std::cout << "Confirm Password: ";
    std::getline(std::cin, confirmPassword);

    std::cout << "Enter Email: ";
    std::getline(std::cin, email);

    std::cout << "Enter Phone Number: ";
    std::getline(std::cin, phone);

    if (utils::InputValidator::isEmpty(fullName) || utils::InputValidator::isEmpty(username) || 
        utils::InputValidator::isEmpty(password) || utils::InputValidator::isEmpty(confirmPassword) || 
        utils::InputValidator::isEmpty(email) || utils::InputValidator::isEmpty(phone)) {
        std::cout << "All fields are required.\n";
        return models::User();
    }

    if (!utils::InputValidator::isValidUsername(username)) {
        std::cout << "Username must be at least 4 characters long.\n";
        return models::User();
    }

    if (usernameExists(username)) {
        std::cout << "Username already exists.\n";
        return models::User();
    }

    if (!utils::InputValidator::isValidPassword(password)) {
        std::cout << "Password length must be at least 6 characters.\n";
        return models::User();
    }

    if (password != confirmPassword) {
        std::cout << "Passwords do not match.\n";
        return models::User();
    }

    if (!utils::InputValidator::isValidEmail(email)) {
        std::cout << "Invalid email format.\n";
        return models::User();
    }

    if (!utils::InputValidator::isValidPhone(phone)) {
        std::cout << "Phone number must contain exactly 10 digits.\n";
        return models::User();
    }

    models::User user(fullName, username, password, email, phone);
    utils::FileManager::appendLine(USER_FILE, user.toFileString());
    std::cout << "User registered successfully.\n";
    return user;
}

models::User UserService::loginUser(const std::string& username, const std::string& password) {
    if (utils::InputValidator::isEmpty(username) || utils::InputValidator::isEmpty(password)) {
        return models::User();
    }

    std::vector<models::User> users = loadUsers();
    for (const models::User& user : users) {
        if (user.getUsername() == username && user.getPassword() == password) {
            return user;
        }
    }
    return models::User();
}

int UserService::countComplaintsForUser(const std::string& username) {
    if (utils::InputValidator::isEmpty(username)) {
        return 0;
    }

    ComplaintService complaintService;
    return complaintService.getComplaintsByUsername(username).size();
}

}
}
