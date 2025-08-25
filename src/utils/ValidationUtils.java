package utils;

public class ValidationUtils {
    public static boolean isValidAmount(double amount) { return amount > 0; }
    public static boolean isValidType(String type) {
        return type != null && (type.equalsIgnoreCase("Income") || type.equalsIgnoreCase("Expense"));
    }
}
