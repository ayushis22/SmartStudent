# 🧑‍🎓 Student Management System – Java + MySQL + Swing

## 📌 Description
A desktop GUI-based Java application to manage student records. It allows administrators to:
- Add, update, delete, and search student records.
- View department-wise statistics.
- Export student data to a CSV file.
- Authenticate via admin login.
- Validate user inputs (email, phone, etc).

---

## 💻 Tech Stack
- **Java** (Swing for GUI)
- **MySQL** (Database)
- **JDBC** (Java Database Connectivity)

---

## 📂 Features
- Admin login authentication
- Add/update/delete student data
- Search by roll number, department, or marks
- View stats: total, highest, lowest, dept-wise count
- Export data to `students.csv`
- Input validation and confirmation dialogs

---

## 🏁 How to Run

### 1. Setup MySQL Database
- Create a database using the provided `database_setup.sql` file.

### 2. Configure DB Connection
In your `DatabaseConnection.java` file, add your details:
```java
String url = "jdbc:mysql://localhost:3306/database_name";
String user = "your_username";
String password = "your_mysql_password";
```

### 3. Compile and Run
Use any IDE (like IntelliJ or Eclipse) or command line:
```bash
javac *.java
java LoginForm
```

### 4. Login Credentials
```txt
Username: admin
Password: admin123
```

---

## 📁 Output
- `students.csv` will be created when you click “Export CSV”
- Search filters update table view in real-time
- Confirmation popups appear for delete and logout actions

---

## 🙋‍♂️ Developed By
- Ayushi Srivastava
