package models;

public class Transaction {
    private String type;       // "Income" or "Expense"
    private String category;
    private double amount;
    private String date;       // yyyy-MM-dd
    private String description;
    private String user;       // username owner

    public Transaction(String type, String category, double amount, String date, String description, String user) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.user = user;
    }

    public String getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getUser() { return user; }

    public void setType(String type) { this.type = type; }
    public void setCategory(String category) { this.category = category; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDate(String date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
    public void setUser(String user) { this.user = user; }

    @Override
    public String toString() {
        return date + " | " + type + " | " + category + " | " + amount + " | " + description;
    }
}
