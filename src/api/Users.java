package api;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Users {
    private static List<User> _users;
    public static User LoggedUser = null;    // TXT file delimiter

    boolean LoggedIn;
    //public List<String> loadUsers() {}

    public static void load() throws ClassNotFoundException, SQLException {

        _users = new ArrayList<>();

        // Load the database driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Connect to the database
        String url = "jdbc:mysql://localhost/cashmachine";
        Connection conn = DriverManager.getConnection(url, "root", "password");

        // Create a statement object
        Statement stmt = conn.createStatement();

        // Execute the SQL query and get the result set
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");

        // Loop through the result set and print out the data
        while (rs.next()) {
            int id = rs.getInt("id");
            String firstname = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            int password = rs.getInt("password");

            User u = new User(firstname, lastname, password);
            _users.add(u);

        }

        // Close the result set, statement, and connection
        rs.close();
        stmt.close();
        conn.close();
    }

    public static boolean loginUser(String fname, String lname, int password, int tries) throws SQLException, ClassNotFoundException {
        LoggedUser = null;
        // we search all users to find a match
        if (_users == null) Users.load();

        for (User u : _users) {
            if (u.getFirstname().equals(fname.trim()) && u.getLastname().equals(lname.trim()) && u.getPassword() == password) {
                // keep the logged user for later use
                LoggedUser = u;
                System.out.println("Welcome, " + u.getFirstname() + " " + u.getLastname() + "!");
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean login_panel() throws SQLException, ClassNotFoundException {
        int tries = 0;
        boolean login = false;
        Scanner in = new Scanner(System.in);

        while (tries < 3 && (login == false)) {
            System.out.print("Enter your first name: ");
            String fname = in.next();
            System.out.print("Enter your last name: ");
            String lname = in.next();
            System.out.print("Enter your password: ");
            int psw = in.nextInt();

            if (Users.loginUser(fname, lname, psw, tries) == true) {
                Users.load();
                login = true;
            } else {
                tries++;
                System.out.println("Wrong password!\nPlease enter again your password.\nRemaining tries: " + (3 - tries));
                if (tries == 3)
                    System.exit(0);
            }
        }
        return false;
    }
}
