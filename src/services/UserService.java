package services;

import models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String DATA_FOLDER = "data/";
    private static final String USERS_FILE = DATA_FOLDER + "users.csv";
    private final List<User> users = new ArrayList<>();

    public UserService() {
        ensureFolder();
        loadUsers();
    }

    private void ensureFolder() {
        File f = new File(DATA_FOLDER);
        if (!f.exists()) f.mkdirs();
    }

    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 2) {
                    users.add(new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                bw.write(u.getUsername() + "," + u.getPassword());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public boolean signup(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.length() < 4) {
            System.out.println("Username cannot be blank and password must be >= 4 chars.");
            return false;
        }
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        users.add(new User(username, password));
        saveUsers();
        return true;
    }

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
