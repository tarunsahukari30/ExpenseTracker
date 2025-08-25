package services;

import models.Transaction;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private static final String DATA_FOLDER = "data/";

    public FileService() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) folder.mkdirs();
    }

    public void saveTransactions(String username, List<Transaction> transactions) {
        String fileName = DATA_FOLDER + "transactions_" + username + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Transaction t : transactions) {
                if (t.getUser().equals(username)) {
                    writer.write(t.getType() + "," +
                                 t.getCategory() + "," +
                                 t.getAmount() + "," +
                                 t.getDate() + "," +
                                 t.getDescription() + "," +
                                 t.getUser());
                    writer.newLine();
                }
            }
            System.out.println("✅ Saved: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    public List<Transaction> loadTransactions(String username) {
        List<Transaction> list = new ArrayList<>();
        String fileName = DATA_FOLDER + "transactions_" + username + ".csv";
        File file = new File(fileName);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length == 6) {
                    list.add(new Transaction(
                        p[0],                 // type
                        p[1],                 // category
                        Double.parseDouble(p[2]),
                        p[3],                 // date
                        p[4],                 // description
                        p[5]                  // user
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
        return list;
    }
}
