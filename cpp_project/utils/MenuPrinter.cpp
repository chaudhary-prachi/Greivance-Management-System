#include "MenuPrinter.h"
#include <iostream>

namespace project {
namespace utils {

void MenuPrinter::printWelcomeScreen() {
    std::cout << "==========================================\n";
    std::cout << "      PUBLIC GRIEVANCE SORTING SYSTEM\n";
    std::cout << "==========================================\n\n";
    std::cout << "1. User Login\n";
    std::cout << "2. User Signup\n";
    std::cout << "3. Admin Login\n";
    std::cout << "4. Exit\n\n";
}

void MenuPrinter::printUserDashboard() {
    std::cout << "------------------------------------------\n";
    std::cout << "               USER DASHBOARD\n";
    std::cout << "------------------------------------------\n";
    std::cout << "1. Submit Complaint\n";
    std::cout << "2. View My Complaints\n";
    std::cout << "3. Search Complaint by ID\n";
    std::cout << "4. Edit Pending Complaint\n";
    std::cout << "5. Delete Pending Complaint\n";
    std::cout << "6. Profile\n";
    std::cout << "7. Logout\n";
    std::cout << "------------------------------------------\n";
}

void MenuPrinter::printAdminDashboard() {
    std::cout << "------------------------------------------\n";
    std::cout << "              ADMIN DASHBOARD\n";
    std::cout << "------------------------------------------\n";
    std::cout << "1. View All Complaints\n";
    std::cout << "2. Search Complaint\n";
    std::cout << "3. View Pending\n";
    std::cout << "4. View Accepted\n";
    std::cout << "5. View Resolved\n";
    std::cout << "6. View Rejected\n";
    std::cout << "7. Sort Complaints\n";
    std::cout << "8. Change Complaint Status\n";
    std::cout << "9. Give Admin Remark\n";
    std::cout << "10. Dashboard Statistics\n";
    std::cout << "11. Logout\n";
    std::cout << "------------------------------------------\n";
}

void MenuPrinter::printWelcomeBack(const std::string& name) {
    std::cout << "------------------------------------\n";
    std::cout << "Hello, " << name << "\n";
    std::cout << "Welcome Back\n";
    std::cout << "------------------------------------\n";
}

void MenuPrinter::printGoodbye() {
    std::cout << "\n==========================================\n";
    std::cout << "   Thank you for using the system.\n";
    std::cout << "             Goodbye!\n";
    std::cout << "==========================================\n";
}

void MenuPrinter::printLoading(const std::string& message) {
    std::cout << message << " ...\n";
}

void MenuPrinter::printSection(const std::string& title) {
    std::cout << "\n==========================================\n";
    std::cout << "  " << title << "\n";
    std::cout << "==========================================\n";
}

}
}
