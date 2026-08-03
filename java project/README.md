# Public Grievance Sorting System

A beginner-friendly **Java console application** for registering, logging in, submitting complaints, tracking complaint status, and managing grievances through an admin dashboard.

This project uses **basic Java**, **OOP**, **file handling**, and common **DSA concepts**. All data is stored in **text files**, so no database is required.

---

## Project Structure

```text
java project/
├── README.md
└── project/
    ├── Main.java
    ├── models/
    │   ├── User.java
    │   └── Complaint.java
    ├── services/
    │   ├── UserService.java
    │   ├── ComplaintService.java
    │   └── AdminService.java
    ├── utils/
    │   ├── FileManager.java
    │   ├── InputValidator.java
    │   ├── MenuPrinter.java
    │   └── UniqueIDGenerator.java
    └── data/
        ├── users.txt
        ├── complaints.txt
        └── admin.txt
```

---

## Features

### User Features

- User signup
- User login
- Submit complaint
- View my complaints
- Search complaint by ID
- Edit pending complaint only
- Delete pending complaint only
- View profile

### Admin Features

- Admin login
- View all complaints
- Search complaint by ID
- View complaints by status
- Sort complaints
- Change complaint status
- Add admin remark
- Dashboard statistics
- View recent complaints
- Process complaints using priority order

### Extra Features

- Unique complaint IDs like `CMP1001`
- Auto date and time
- Complaint counter
- ASCII menu design
- Simple loading style output
- File-based data storage

---

## Data Storage Format

### `users.txt`

```text
FullName,Username,Password,Email,Phone
```

Example:

```text
John Doe,john,123456,john@gmail.com,9876543210
```

### `complaints.txt`

```text
ComplaintID|Username|Topic|Priority|Description|Location|Department|Date|Time|Status|AdminRemark
```

Example:

```text
CMP1001|john|Street light not working|High|Street lights are off for 3 days|Sector 5|Electricity|2026-07-20|12:15:30|Pending|Not Reviewed
```

### `admin.txt`

```text
admin,admin123
```

---

## DSA Concepts Used

This project uses the following DSA concepts naturally:

- **ArrayList** – to store users and complaints in memory
- **LinkedList** – used as a queue for pending complaints
- **HashMap** – used for dashboard statistics
- **Queue** – used to process pending complaints
- **PriorityQueue** – used to process high-priority complaints first
- **Linear Search** – used for complaint ID search
- **Sorting** – used for sorting complaints
- **Comparator** – used for custom sorting logic
- **Collections.sort()** – used for sorting complaint lists
- **File Handling** – used for saving and reading data from text files

---

## How to Run

### In VS Code / IntelliJ IDEA / Eclipse

1. Open the `java project` folder.
2. Make sure the package structure is preserved.
3. Run `project.Main`.
4. Use the terminal menu to interact with the system.

### From Command Line

1. Open terminal in the `java project` folder.
2. Compile all `.java` files.
3. Run the main class `project.Main`.

> The project uses standard Java packages, so it can run in any Java IDE.

---

## Default Admin Login

- **Username:** `admin`
- **Password:** `admin123`

---

## Validation Rules

- No empty fields
- Username must be unique
- Username length must be valid
- Password must be at least 6 characters
- Email format must be valid
- Phone number must contain 10 digits
- Complaint ID must be unique
- Pending complaints only can be edited or deleted

---

## Future Scope

- Add graphical user interface using Swing or JavaFX
- Add password encryption for better security
- Add report export to PDF or CSV
- Add email notifications
- Add complaint escalation system
- Add multi-admin support
- Add complaint attachments
- Add better search and filtering options

---

## Viva Questions and Answers

### 1. What is the purpose of this project?

**Answer:** The project helps citizens submit complaints and allows administrators to manage and resolve those complaints in an organized way.

### 2. Why did you use text files instead of a database?

**Answer:** The project is beginner-friendly and the requirement says not to use MySQL, SQLite, MongoDB, or any database.

### 3. Which OOP concepts are used?

**Answer:** Encapsulation, classes, objects, and separation of responsibilities through packages.

### 4. How is complaint data stored?

**Answer:** Complaint data is stored in `complaints.txt` using pipe-separated values.

### 5. How do you generate unique complaint IDs?

**Answer:** The `UniqueIDGenerator` scans the complaint file and creates the next ID like `CMP1001`, `CMP1002`, and so on.

### 6. Which search technique is used?

**Answer:** Linear search is used to find complaints by ID.

### 7. Which sorting method is used?

**Answer:** `Collections.sort()` with `Comparator` is used for sorting complaints.

### 8. Why did you use `PriorityQueue`?

**Answer:** It helps process high-priority complaints before medium and low-priority complaints.

### 9. Why is `Queue` used in this project?

**Answer:** Pending complaints are stored in a queue so they can be handled in order.

### 10. Can a user edit or delete any complaint?

**Answer:** No. A user can only edit or delete complaints that are still in `Pending` status.

### 11. What is the default admin login?

**Answer:** Username is `admin` and password is `admin123`.

### 12. What file handling methods are used?

**Answer:** The project uses `BufferedReader`, `BufferedWriter`, `FileReader`, and `FileWriter` for reading and writing files.

### 13. What happens if a complaint is not found?

**Answer:** The system shows `Complaint Not Found`.

### 14. Why is validation important?

**Answer:** Validation prevents invalid input such as empty fields, duplicate usernames, and incorrect phone numbers.

### 15. What is the main advantage of this system?

**Answer:** It provides a simple and organized way to register, track, and manage public complaints.

---

## Notes

- All files are designed to work with basic Java.
- The code is intentionally simple for beginners.
- The terminal interface uses ASCII borders for a neat look.

---

## Goodbye

Thank you for using the **Public Grievance Sorting System**!
