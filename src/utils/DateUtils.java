package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DF);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
