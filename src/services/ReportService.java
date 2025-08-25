package services;

import models.Transaction;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private static final String REPORTS_FOLDER = "data/reports/";

    // ANSI Colors for console
    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";

    public ReportService() {
        File folder = new File(REPORTS_FOLDER);
        if (!folder.exists()) folder.mkdirs();
    }

    // ✅ Console Summary with Colors + Bar Chart
    public void generateSummary(String username, List<Transaction> transactions) {
        String summary = buildSummary(username, transactions, true);
        System.out.print(summary); // print with colors
    }

    // ✅ Save Summary (without ANSI colors, plain text + ASCII chart)
    public void saveSummaryReport(String username, List<Transaction> transactions) {
        String summary = buildSummary(username, transactions, false);

        String fileName = REPORTS_FOLDER + username + "_summary.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(summary);
            System.out.println("✅ Summary report saved: " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing summary: " + e.getMessage());
        }
    }

    // 🔹 Shared logic for summary (console + file)
    private String buildSummary(String username, List<Transaction> transactions, boolean withColors) {
        double income = 0, expense = 0;
        Map<String, Double> byCategory = new HashMap<>();

        for (Transaction t : transactions) {
            if (!t.getUser().equals(username)) continue;

            if (t.getType().equalsIgnoreCase("Income")) {
                income += t.getAmount();
            } else if (t.getType().equalsIgnoreCase("Expense")) {
                expense += t.getAmount();
                byCategory.put(t.getCategory(),
                        byCategory.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
        }
        double balance = income - expense;

        StringBuilder sb = new StringBuilder();
        if (withColors) sb.append(BLUE);
        sb.append("\n===== Summary for ").append(username).append(" =====\n");
        if (withColors) sb.append(RESET);

        if (withColors) sb.append(GREEN);
        sb.append(String.format("Total Income : %.2f%n", income));
        if (withColors) sb.append(RESET);

        if (withColors) sb.append(RED);
        sb.append(String.format("Total Expense: %.2f%n", expense));
        if (withColors) sb.append(RESET);

        if (withColors) sb.append(YELLOW);
        sb.append(String.format("Net Savings  : %.2f%n", balance));
        if (withColors) sb.append(RESET);

        if (withColors) sb.append(BLUE);
        sb.append("\n--- Expense by Category ---\n");
        if (withColors) sb.append(RESET);

        if (byCategory.isEmpty()) {
            sb.append("(no expenses)\n");
        } else {
            for (Map.Entry<String, Double> e : byCategory.entrySet()) {
                sb.append(String.format("%-12s : %.2f%n", e.getKey(), e.getValue()));
            }

            if (withColors) sb.append(BLUE);
            sb.append("\n--- Expense Chart ---\n");
            if (withColors) sb.append(RESET);

            double max = byCategory.values().stream().mapToDouble(v -> v).max().orElse(1);
            for (Map.Entry<String, Double> e : byCategory.entrySet()) {
                int barLength = (int) ((e.getValue() / max) * 30);
                String bar = (withColors ? "█" : "#").repeat(Math.max(1, barLength));
                sb.append(String.format("%-12s | %s %.2f%n", e.getKey(), bar, e.getValue()));
            }
        }

        return sb.toString();
    }

    // ✅ Existing monthly report (unchanged)
    public void generateMonthlyReport(String username, List<Transaction> transactions, String month) {
        double income = 0, expense = 0;
        Map<String, Double> byCategory = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        sb.append("===== Monthly Report for ").append(username)
          .append(" (").append(month).append(") =====\n\n");

        for (Transaction t : transactions) {
            if (t.getUser().equals(username) && t.getDate().startsWith(month)) {
                sb.append(String.format("%-12s %-8s %-12s %-10.2f %s%n",
                        t.getDate(), t.getType(), t.getCategory(),
                        t.getAmount(), t.getDescription()));

                if (t.getType().equalsIgnoreCase("Income")) income += t.getAmount();
                else {
                    expense += t.getAmount();
                    byCategory.put(t.getCategory(),
                            byCategory.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
                }
            }
        }
        double balance = income - expense;

        sb.append("\n--- Summary ---\n");
        sb.append(String.format("Total Income : %.2f%n", income));
        sb.append(String.format("Total Expense: %.2f%n", expense));
        sb.append(String.format("Net Savings  : %.2f%n", balance));

        sb.append("\n--- Expense by Category ---\n");
        if (byCategory.isEmpty()) {
            sb.append("(no expenses)\n");
        } else {
            for (Map.Entry<String, Double> e : byCategory.entrySet()) {
                sb.append(String.format("%-12s : %.2f%n", e.getKey(), e.getValue()));
            }

            sb.append("\n--- Expense Chart ---\n");
            double max = byCategory.values().stream().mapToDouble(v -> v).max().orElse(1);
            for (Map.Entry<String, Double> e : byCategory.entrySet()) {
                int barLength = (int) ((e.getValue() / max) * 30);
                String bar = "#".repeat(Math.max(1, barLength));
                sb.append(String.format("%-12s | %s %.2f%n", e.getKey(), bar, e.getValue()));
            }
        }

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(REPORTS_FOLDER + username + "_" + month + ".txt"))) {
            bw.write(sb.toString());
            System.out.println("✅ Report generated: " + REPORTS_FOLDER + username + "_" + month + ".txt");
        } catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }
}
