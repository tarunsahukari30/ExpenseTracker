## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

# 💰 Expense Tracker (Java Console Application)

A simple **Java-based Expense Tracker** that helps users manage income and expenses, view summaries, and generate monthly reports.

---

## 🚀 Features
- 🔑 User **Login & Signup**
- 💵 Add **Income / Expense** transactions  
- 📂 Categories:
  - Income → Salary, Business, Other
  - Expense → Food, Travel, Shopping, Bills, Other
- 📊 View **transaction history**
- 📈 View **summary report** (Income, Expense, Savings, Category breakdown)
- 📅 Generate **monthly reports** (saved in `data/reports/`)
- 💾 Auto-save transactions for each user
- 🎨 Colorful console output (ANSI colors)

---

## 🛠️ Tech Stack
- **Java 17+** (works with 8+)
- Console-based UI
- File-based storage (`data/` folder)

---

## 📂 Project Structure
ExpenseTracker/
├── src/
│ ├── models/ # User, Transaction classes
│ ├── services/ # TransactionService, ReportService, FileService, UserService
│ ├── utils/ # ValidationUtils, DateUtils
│ └── ExpenseTrackerMain.java
├── data/ # User and transaction data storage
├── out/ # Compiled .class files
├── .gitignore
└── README.md


---

## ▶️ How to Run

### 1. Compile
```sh
javac -d out src\models\*.java src\services\*.java src\utils\*.java src\ExpenseTrackerMain.java
java -cp out ExpenseTrackerMain



===== Welcome to Expense Tracker =====

1. Login
2. Signup
3. Exit
Enter choice: 2
Enter Username: tarun
Enter Password: ****
[OK] Signup successful. Please login.


📝 Future Improvements
Export reports to PDF/Excel
Add budget limits & alerts
GUI version using JavaFX or Swing
Cloud sync (MySQL / Firebase)

👨‍💻 Author
Tarun Sahukari🚀
GitHub: https://github.com/tarunsahukari30
