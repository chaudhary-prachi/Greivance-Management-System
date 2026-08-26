#include <iostream>
#include <string>
#include <vector>
#include "models/User.h"
#include "models/Complaint.h"
#include "services/UserService.h"
#include "services/ComplaintService.h"
#include "services/AdminService.h"
#include "utils/MenuPrinter.h"

using namespace project::services;
using namespace project::models;
using namespace project::utils;

std::string readText(const std::string& prompt) {
    std::cout << prompt;
    std::string input;
    std::getline(std::cin, input);
    // basic trim
    input.erase(0, input.find_first_not_of(" \t\r\n"));
    if (!input.empty()) input.erase(input.find_last_not_of(" \t\r\n") + 1);
    return input;
}

std::string shortenText(const std::string& text, int maxLength) {
    if (text.length() <= (size_t)maxLength) {
        return text;
    }
    return text.substr(0, std::max(0, maxLength - 3)) + "...";
}

void showUserComplaints(const User& user, ComplaintService& complaintService) {
    std::vector<Complaint> complaints = complaintService.getComplaintsByUsername(user.getUsername());
    if (complaints.empty()) {
        std::cout << "No complaints found.\n";
        return;
    }

    std::cout << "------------------------------------------------------------\n";
    printf("%-12s %-18s %-10s %-10s %-14s %-12s\n",
            "ComplaintID", "Topic", "Priority", "Status", "Department", "Date");
    std::cout << "------------------------------------------------------------\n";

    for (const Complaint& complaint : complaints) {
        printf("%-12s %-18s %-10s %-10s %-14s %-12s\n",
                complaint.getComplaintId().c_str(),
                shortenText(complaint.getTopic(), 17).c_str(),
                complaint.getPriority().c_str(),
                complaint.getStatus().c_str(),
                shortenText(complaint.getDepartment(), 13).c_str(),
                complaint.getDate().c_str());
    }
    std::cout << "------------------------------------------------------------\n";
}

void searchComplaint(ComplaintService& complaintService) {
    std::string complaintId = readText("Enter Complaint ID: ");
    Complaint complaint = complaintService.findComplaintById(complaintId);
    complaintService.printComplaintDetails(complaint);
}

void editPendingComplaint(ComplaintService& complaintService, const User& user) {
    std::string complaintId = readText("Enter Complaint ID to edit: ");
    Complaint complaint = complaintService.findComplaintById(complaintId);

    if (!complaint.isValid()) {
        std::cout << "Complaint not found.\n";
        return;
    }

    if (complaint.getUsername() != user.getUsername()) {
        std::cout << "You can only edit your own complaints.\n";
        return;
    }

    complaintService.editPendingComplaint(complaintId);
}

void deletePendingComplaint(ComplaintService& complaintService, const User& user) {
    std::string complaintId = readText("Enter Complaint ID to delete: ");
    Complaint complaint = complaintService.findComplaintById(complaintId);

    if (!complaint.isValid()) {
        std::cout << "Complaint not found.\n";
        return;
    }

    if (complaint.getUsername() != user.getUsername()) {
        std::cout << "You can only delete your own complaints.\n";
        return;
    }

    if (complaint.getStatus() != "Pending") {
        std::cout << "Only pending complaints can be deleted.\n";
        return;
    }

    std::string confirm = readText("Are you sure you want to delete this complaint? (Y/N): ");
    if (confirm == "Y" || confirm == "y") {
        complaintService.deletePendingComplaint(complaintId);
    } else {
        std::cout << "Delete cancelled.\n";
    }
}

void showProfile(const User& user, UserService& userService) {
    std::cout << "------------------------------------------\n";
    std::cout << "Name                  : " << user.getFullName() << "\n";
    std::cout << "Username              : " << user.getUsername() << "\n";
    std::cout << "Email                 : " << user.getEmail() << "\n";
    std::cout << "Phone                 : " << user.getPhone() << "\n";
    std::cout << "Complaints Submitted  : " << userService.countComplaintsForUser(user.getUsername()) << "\n";
    std::cout << "------------------------------------------\n";
}

void userDashboard(const User& user, UserService& userService, ComplaintService& complaintService) {
    bool loggedIn = true;

    while (loggedIn) {
        MenuPrinter::printUserDashboard();
        std::string choice = readText("Enter your choice: ");

        if (choice == "1") complaintService.submitComplaint(user.getUsername());
        else if (choice == "2") showUserComplaints(user, complaintService);
        else if (choice == "3") searchComplaint(complaintService);
        else if (choice == "4") editPendingComplaint(complaintService, user);
        else if (choice == "5") deletePendingComplaint(complaintService, user);
        else if (choice == "6") showProfile(user, userService);
        else if (choice == "7") {
            std::cout << "Logging out...\n";
            loggedIn = false;
        }
        else std::cout << "Invalid choice. Please try again.\n";
    }
}

void userLogin(UserService& userService, ComplaintService& complaintService) {
    std::string username = readText("Enter Username: ");
    std::string password = readText("Enter Password: ");

    User user = userService.loginUser(username, password);
    if (!user.isValid()) {
        std::cout << "Invalid username or password.\n";
        return;
    }

    MenuPrinter::printWelcomeBack(user.getFullName());
    userDashboard(user, userService, complaintService);
}

void sortComplaints(AdminService& adminService) {
    std::cout << "Sort by:\n";
    std::cout << "1. Priority\n";
    std::cout << "2. Date\n";
    std::cout << "3. Department\n";
    std::cout << "4. Status\n";
    std::cout << "5. Complaint ID\n";
    std::string choice = readText("Enter choice: ");

    std::string sortBy = "complaintId";
    if (choice == "1") sortBy = "priority";
    else if (choice == "2") sortBy = "date";
    else if (choice == "3") sortBy = "department";
    else if (choice == "4") sortBy = "status";

    adminService.printComplaintTable(adminService.sortComplaints(sortBy));
}

void changeComplaintStatus(AdminService& adminService) {
    std::string complaintId = readText("Enter Complaint ID: ");
    std::cout << "Choose Status:\n";
    std::cout << "1. Pending\n";
    std::cout << "2. Accepted\n";
    std::cout << "3. Resolved\n";
    std::cout << "4. Rejected\n";
    std::string choice = readText("Enter choice: ");

    std::string status = "";
    if (choice == "1") status = "Pending";
    else if (choice == "2") status = "Accepted";
    else if (choice == "3") status = "Resolved";
    else if (choice == "4") status = "Rejected";

    if (status.empty()) {
        std::cout << "Invalid status choice.\n";
        return;
    }

    if (adminService.changeComplaintStatus(complaintId, status)) {
        std::cout << "Complaint status updated successfully.\n";
    } else {
        std::cout << "Complaint not found.\n";
    }
}

void giveAdminRemark(AdminService& adminService) {
    std::string complaintId = readText("Enter Complaint ID: ");
    std::string remark = readText("Enter Admin Remark: ");

    if (adminService.giveAdminRemark(complaintId, remark)) {
        std::cout << "Admin remark saved successfully.\n";
    } else {
        std::cout << "Complaint not found.\n";
    }
}

void showStatistics(AdminService& adminService) {
    auto stats = adminService.getStatistics();

    std::cout << "==========================================\n";
    std::cout << "           DASHBOARD STATISTICS\n";
    std::cout << "==========================================\n";
    std::cout << "Total Complaints : " << stats["Total"] << "\n";
    std::cout << "Pending          : " << stats["Pending"] << "\n";
    std::cout << "Accepted         : " << stats["Accepted"] << "\n";
    std::cout << "Resolved         : " << stats["Resolved"] << "\n";
    std::cout << "Rejected         : " << stats["Rejected"] << "\n";
    std::cout << "High Priority    : " << stats["High"] << "\n";
    std::cout << "Medium Priority  : " << stats["Medium"] << "\n";
    std::cout << "Low Priority     : " << stats["Low"] << "\n";
    std::cout << "==========================================\n";
}

void adminDashboard(AdminService& adminService) {
    bool loggedIn = true;

    while (loggedIn) {
        MenuPrinter::printAdminDashboard();
        std::string choice = readText("Enter your choice: ");

        if (choice == "1") adminService.printComplaintTable(adminService.viewAllComplaints());
        else if (choice == "2") {
            Complaint complaint = adminService.searchComplaintById(readText("Enter Complaint ID: "));
            adminService.printComplaintDetails(complaint);
        }
        else if (choice == "3") adminService.printComplaintTable(adminService.viewByStatus("Pending"));
        else if (choice == "4") adminService.printComplaintTable(adminService.viewByStatus("Accepted"));
        else if (choice == "5") adminService.printComplaintTable(adminService.viewByStatus("Resolved"));
        else if (choice == "6") adminService.printComplaintTable(adminService.viewByStatus("Rejected"));
        else if (choice == "7") sortComplaints(adminService);
        else if (choice == "8") changeComplaintStatus(adminService);
        else if (choice == "9") giveAdminRemark(adminService);
        else if (choice == "10") showStatistics(adminService);
        else if (choice == "11") {
            std::cout << "Admin logging out...\n";
            loggedIn = false;
        }
        else std::cout << "Invalid choice. Please try again.\n";
    }
}

void adminLogin(AdminService& adminService) {
    std::string username = readText("Enter Admin Username: ");
    std::string password = readText("Enter Admin Password: ");

    if (adminService.loginAdmin(username, password)) {
        std::cout << "------------------------------------\n";
        std::cout << "Admin Login Successful\n";
        std::cout << "------------------------------------\n";
        adminDashboard(adminService);
    } else {
        std::cout << "Invalid admin credentials.\n";
    }
}

int main() {
    UserService userService;
    ComplaintService complaintService;
    AdminService adminService;

    std::cout << "\n";
    std::cout << "*******************************************\n";
    std::cout << "      PUBLIC GRIEVANCE SORTING SYSTEM\n";
    std::cout << "*******************************************\n";

    bool running = true;
    while (running) {
        MenuPrinter::printWelcomeScreen();
        std::string choice = readText("Enter your choice: ");

        if (choice == "1") userLogin(userService, complaintService);
        else if (choice == "2") userService.registerUser();
        else if (choice == "3") adminLogin(adminService);
        else if (choice == "4") {
            MenuPrinter::printGoodbye();
            running = false;
        }
        else std::cout << "Invalid choice. Please try again.\n";
    }

    return 0;
}
