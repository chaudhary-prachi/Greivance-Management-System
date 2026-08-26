#ifndef MENUPRINTER_H
#define MENUPRINTER_H

#include <string>

namespace project {
namespace utils {

class MenuPrinter {
public:
    static void printWelcomeScreen();
    static void printUserDashboard();
    static void printAdminDashboard();
    static void printWelcomeBack(const std::string& name);
    static void printGoodbye();
    static void printLoading(const std::string& message);
    static void printSection(const std::string& title);
};

}
}

#endif // MENUPRINTER_H
