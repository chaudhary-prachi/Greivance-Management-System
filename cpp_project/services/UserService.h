#ifndef USERSERVICE_H
#define USERSERVICE_H

#include <vector>
#include <string>
#include "../models/User.h"

namespace project {
namespace services {

class UserService {
private:
    std::string USER_FILE = "data/users.txt";

public:
    UserService();
    std::vector<models::User> loadUsers();
    models::User findUserByUsername(const std::string& username);
    bool usernameExists(const std::string& username);
    models::User registerUser();
    models::User loginUser(const std::string& username, const std::string& password);
    int countComplaintsForUser(const std::string& username);
};

}
}

#endif // USERSERVICE_H
