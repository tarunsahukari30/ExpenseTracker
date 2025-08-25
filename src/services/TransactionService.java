package services;

import models.Transaction;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private List<Transaction> transactions = new ArrayList<>();

    // ✅ Add a new transaction
    public void addTransaction(Transaction t) {
        transactions.add(t);
        System.out.println("✅ Transaction added!");
    }

    // ✅ View all transactions of a user (with colors + table)
    public void viewTransactions(String username) {
        System.out.println("\u001B[34m\n===== Transactions for " + username + " =====\u001B[0m");
        boolean any = false;

        // Print header
        System.out.printf("\u001B[34m%-12s %-10s %-12s %-10s %-20s\u001B[0m%n",
                "Date", "Type", "Category", "Amount", "Description");

        for (Transaction t : transactions) {
            if (t.getUser().equals(username)) {
                String color = t.getType().equalsIgnoreCase("Income")
                        ? "\u001B[32m"  // Green
                        : "\u001B[31m"; // Red

                System.out.printf(color + "%-12s %-10s %-12s %-10.2f %-20s\u001B[0m%n",
                        t.getDate(), t.getType(), t.getCategory(), t.getAmount(), t.getDescription());
                any = true;
            }
        }

        if (!any) System.out.println("(no transactions)");
    }

    // ✅ Search transactions by category (with colors + table)
    public void searchByCategory(String username, String category) {
        System.out.println("\u001B[34m\n===== " + category + " (user: " + username + ") =====\u001B[0m");
        boolean any = false;

        // Print header
        System.out.printf("\u001B[34m%-12s %-10s %-12s %-10s %-20s\u001B[0m%n",
                "Date", "Type", "Category", "Amount", "Description");

        for (Transaction t : transactions) {
            if (t.getUser().equals(username) && t.getCategory().equalsIgnoreCase(category)) {
                String color = t.getType().equalsIgnoreCase("Income")
                        ? "\u001B[32m"
                        : "\u001B[31m";

                System.out.printf(color + "%-12s %-10s %-12s %-10.2f %-20s\u001B[0m%n",
                        t.getDate(), t.getType(), t.getCategory(), t.getAmount(), t.getDescription());
                any = true;
            }
        }

        if (!any) System.out.println("(none found)");
    }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> list) { this.transactions = list; }
}
