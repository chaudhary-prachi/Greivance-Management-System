#ifndef UNIQUEIDGENERATOR_H
#define UNIQUEIDGENERATOR_H

#include <string>

namespace project {
namespace utils {

class UniqueIDGenerator {
private:
    static const std::string COMPLAINT_FILE;
    static const std::string PREFIX;
    static const int START_NUMBER = 1001;

public:
    static std::string generateComplaintId();
    static bool complaintIdExists(const std::string& complaintId);
};

}
}

#endif // UNIQUEIDGENERATOR_H
