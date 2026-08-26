#ifndef USER_H
#define USER_H

#include <string>

namespace project {
namespace models {

class User {
private:
    std::string fullName;
    std::string username;
    std::string password;
    std::string email;
    std::string phone;

public:
    User();
    User(std::string fullName, std::string username, std::string password, std::string email, std::string phone);

    std::string getFullName() const;
    void setFullName(const std::string& fullName);

    std::string getUsername() const;
    void setUsername(const std::string& username);

    std::string getPassword() const;
    void setPassword(const std::string& password);

    std::string getEmail() const;
    void setEmail(const std::string& email);

    std::string getPhone() const;
    void setPhone(const std::string& phone);

    std::string toFileString() const;
    static User fromFileString(const std::string& line);
    
    bool isValid() const;
};

}
}

#endif // USER_H
