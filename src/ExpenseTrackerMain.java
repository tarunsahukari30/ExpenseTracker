import models.Transaction;
import models.User;
import services.TransactionService;
import services.ReportService;
import services.FileService;
import services.UserService;
import utils.DateUtils;
import utils.ValidationUtils;

import java.util.List;
import java.util.Scanner;

public class ExpenseTrackerMain {
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        FileService fileService = new FileService();
        TransactionService txService = new TransactionService();
        ReportService reportService = new ReportService();

        System.out.println(BLUE + "===== Welcome to Expense Tracker =====" + RESET);

        // ----- LOGIN / SIGNUP -----
        User loggedIn = null;
        while (loggedIn == null) {
            System.out.println("\n" + BLUE + "1. Login" + RESET);
            System.out.println(BLUE + "2. Signup" + RESET);
            System.out.println(BLUE + "3. Exit" + RESET);
            System.out.print(YELLOW + "Enter choice: " + RESET);
            int authChoice = readInt(sc);

            if (authChoice == 3) {
                System.out.println(GREEN + "[OK] Goodbye!" + RESET);
                sc.close();
                return;
            }

            System.out.print("Enter Username: ");
            String username = sc.nextLine().trim();
            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            if (authChoice == 1) {
                loggedIn = userService.login(username, password);
                if (loggedIn == null)
                    System.out.println(RED + "[X] Invalid credentials." + RESET);
                else
                    System.out.println(GREEN + "[OK] Logged in as " + loggedIn.getUsername() + RESET);
            } else if (authChoice == 2) {
                boolean ok = userService.signup(username, password);
                System.out.println(ok
                        ? GREEN + "[OK] Signup successful. Please login." + RESET
                        : RED + "[X] Username already exists or invalid." + RESET);
            } else {
                System.out.println(RED + "[X] Invalid choice." + RESET);
            }
        }

        // Load this user's transactions
        List<Transaction> loaded = fileService.loadTransactions(loggedIn.getUsername());
        txService.setTransactions(loaded);

        // ----- DASHBOARD -----
        int choice;
        do {
            System.out.println("\n" + BLUE + "--- Dashboard ---" + RESET);
            System.out.println("1. Add Transaction");
            System.out.println("2. View Transactions");
            System.out.println("3. View Summary");
            System.out.println("4. Search by Category");
            System.out.println("5. Generate Monthly Report");
            System.out.println("6. Save Summary Report");
            System.out.println("7. Exit");
            System.out.print(YELLOW + "Enter choice: " + RESET);
            choice = readInt(sc);

            switch (choice) {
                case 1:
                    // ----- ADD TRANSACTION -----
                    System.out.println("\nChoose type:");
                    System.out.println("1. Income");
                    System.out.println("2. Expense");
                    System.out.print(YELLOW + "Enter choice: " + RESET);
                    int typeChoice = readInt(sc);

                    String type = (typeChoice == 1) ? "Income" : "Expense";

                    String category;
                    if (type.equals("Income")) {
                        System.out.println("\nChoose category:");
                        System.out.println("1. Salary");
                        System.out.println("2. Business");
                        System.out.println("3. Other");
                        System.out.print(YELLOW + "Enter choice: " + RESET);
                        int catChoice = readInt(sc);
                        category = switch (catChoice) {
                            case 1 -> "Salary";
                            case 2 -> "Business";
                            default -> "Other";
                        };
                    } else {
                        System.out.println("\nChoose category:");
                        System.out.println("1. Food");
                        System.out.println("2. Travel");
                        System.out.println("3. Shopping");
                        System.out.println("4. Bills");
                        System.out.println("5. Other");
                        System.out.print(YELLOW + "Enter choice: " + RESET);
                        int catChoice = readInt(sc);
                        category = switch (catChoice) {
                            case 1 -> "Food";
                            case 2 -> "Travel";
                            case 3 -> "Shopping";
                            case 4 -> "Bills";
                            default -> "Other";
                        };
                    }

                    System.out.print("Enter Amount: ");
                    Double amount = readDouble(sc);
                    if (amount == null || !ValidationUtils.isValidAmount(amount)) {
                        System.out.println(RED + "[X] Invalid amount." + RESET);
                        break;
                    }

                    System.out.print("Enter Date (yyyy-MM-dd): ");
                    String date = sc.nextLine().trim();
                    if (!DateUtils.isValidDate(date)) {
                        System.out.println(RED + "[X] Invalid date format." + RESET);
                        break;
                    }

                    System.out.print("Enter Description: ");
                    String desc = sc.nextLine();

                    txService.addTransaction(new Transaction(type, category, amount, date, desc, loggedIn.getUsername()));

                    // ✅ AUTO-SAVE after adding transaction
                    fileService.saveTransactions(loggedIn.getUsername(), txService.getTransactions());

                    System.out.println(GREEN + "[OK] Transaction added & saved!" + RESET);
                    break;

                case 2:
                    txService.viewTransactions(loggedIn.getUsername());
                    break;

                case 3:
                    reportService.generateSummary(loggedIn.getUsername(), txService.getTransactions());
                    break;

                case 4:
                    System.out.println("\nSearch Category:");
                    System.out.println("1. Food");
                    System.out.println("2. Travel");
                    System.out.println("3. Shopping");
                    System.out.println("4. Bills");
                    System.out.println("5. Salary");
                    System.out.println("6. Business");
                    System.out.println("7. Other");
                    System.out.print(YELLOW + "Enter choice: " + RESET);
                    int searchChoice = readInt(sc);

                    String searchCat = switch (searchChoice) {
                        case 1 -> "Food";
                        case 2 -> "Travel";
                        case 3 -> "Shopping";
                        case 4 -> "Bills";
                        case 5 -> "Salary";
                        case 6 -> "Business";
                        default -> "Other";
                    };

                    txService.searchByCategory(loggedIn.getUsername(), searchCat);
                    break;

                case 5:
                    System.out.print("Enter month (yyyy-MM): ");
                    String month = sc.nextLine().trim();
                    if (month.matches("\\d{4}-\\d{2}")) {
                        reportService.generateMonthlyReport(loggedIn.getUsername(),
                                txService.getTransactions(), month);
                    } else {
                        System.out.println(RED + "[X] Invalid month format. Example: 2025-08" + RESET);
                    }
                    break;

                case 6:
                    reportService.saveSummaryReport(loggedIn.getUsername(), txService.getTransactions());
                    break;

                case 7:
                    fileService.saveTransactions(loggedIn.getUsername(), txService.getTransactions());
                    reportService.saveSummaryReport(loggedIn.getUsername(), txService.getTransactions());
                    System.out.println(GREEN + "[OK] Data saved. Goodbye!" + RESET);
                    break;

                default:
                    System.out.println(RED + "[X] Invalid choice." + RESET);
            }
        } while (choice != 7);

        sc.close();
    }

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print(YELLOW + "Enter a number: " + RESET);
        }
        int val = sc.nextInt();
        sc.nextLine(); // consume newline
        return val;
    }

    private static Double readDouble(Scanner sc) {
        if (!sc.hasNextDouble()) {
            sc.nextLine();
            return null;
        }
        double val = sc.nextDouble();
        sc.nextLine(); // consume newline
        return val;
    }
}
