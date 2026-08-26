#ifndef FILEMANAGER_H
#define FILEMANAGER_H

#include <string>
#include <vector>

namespace project {
namespace utils {

class FileManager {
public:
    static void ensureFileExists(const std::string& filePath);
    static std::vector<std::string> readAllLines(const std::string& filePath);
    static void writeAllLines(const std::string& filePath, const std::vector<std::string>& lines);
    static void appendLine(const std::string& filePath, const std::string& line);
    static void rewriteFile(const std::string& filePath, const std::vector<std::string>& updatedLines);
};

}
}

#endif // FILEMANAGER_H
