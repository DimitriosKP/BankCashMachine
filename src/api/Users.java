package api;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Users {
    private static List<User> _users;
    public static User LoggedUser = null;
    public static Scanner in = new Scanner(System.in);
    boolean LoggedIn;
    //public List<String> loadUsers() {}

    public static void save(int id, int new_amount) throws ClassNotFoundException, SQLException {
        // Load the database driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Connect to the database
        String url = "jdbc:mysql://localhost/cashmachine";
        String query = "UPDATE accounts SET amount = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, "root", "password");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, new_amount);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
        } catch (SQLException e) {
            // handle exception
        }
    }
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
        ResultSet rs = stmt.executeQuery("SELECT u.id, a.id, u.firstname, u.lastname, u.password, a.amount FROM users u JOIN accounts a ON u.id = a.id");

        // Loop through the result set and create the User List _users
        while (rs.next()) {
            int id = rs.getInt("id");
            String firstname = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            int password = rs.getInt("password");
            int amount = rs.getInt("amount");

            User u = new User(id, firstname, lastname, password, amount);
            _users.add(u);
        }
        // Close the result set, statement, and connection
        rs.close();
        stmt.close();
        conn.close();
    }

    public static boolean loginUser(String fname, String lname, int password) throws SQLException, ClassNotFoundException {
        LoggedUser = null;
        // we search all users to find a match
        if (_users == null) Users.load();

        for (User u : _users) {
            if (u.getFirstname().equals(fname.trim()) && u.getLastname().equals(lname.trim()) && u.getPassword() == password) {
                // keep the logged user for later use
                LoggedUser = u;
                System.out.println("Welcome, " + u.getFirstname() + " " + u.getLastname() + "!");
                return true;
            }
        }
        return false;
    }
    public static boolean login_panel() throws SQLException, ClassNotFoundException {
        int tries = 0;
        boolean login = false;
        if (_users == null) Users.load();

        while ( tries < 3 && (!login) ) {
            System.out.print("Enter your first name: ");
            String fname = in.next();
            System.out.print("Enter your last name: ");
            String lname = in.next();
            System.out.print("Enter your password: ");
            int psw = in.nextInt();

            if (Users.loginUser(fname, lname, psw)) {
                login = true;
                showMenu();
            } else {
                tries++;
                System.out.println("Wrong credentials!\nRemaining tries: " + (3 - tries));
                if (tries == 3)
                    System.exit(0);
            }
        }
        return false;
    }
    public static void showMenu() throws SQLException, ClassNotFoundException {
        System.out.print("MENU\nDeposit: 1\nWithdrawal: 2\nView amount: 3\nExit: 0\nOPTION: ");
        int option = in.nextInt();
        switch (option) {
            case 0 -> System.exit(0);
            case 1 -> Users.deposit();
            case 2 -> withdrawal();
            case 3 -> view_amount();
        }
    }
    public static void deposit() throws SQLException, ClassNotFoundException {
        if (_users == null) Users.load();
        int new_amount = Users.LoggedUser.getAmount();
        System.out.print("Please enter the deposit amount: ");

        int deposit = in.nextInt();
        new_amount += deposit;
        save(Users.LoggedUser.getId(),new_amount);
        showMenu();
    }
    public static void withdrawal() throws SQLException, ClassNotFoundException {
        if (_users == null) Users.load();
        int new_amount = Users.LoggedUser.getAmount();
        System.out.print("Please enter the amount: ");
        int withdrawal = in.nextInt();
        if(withdrawal > new_amount) {
            System.out.print("The balance is less than the amount!\n");
            showMenu();
        } else {
            new_amount -= withdrawal;
        }
        save(Users.LoggedUser.getId(), new_amount);
        showMenu();
    }
    public static void view_amount() throws SQLException, ClassNotFoundException {
        if (_users == null) Users.load();{
            System.out.print("Your amount is: " + Users.LoggedUser.getAmount());
        }
    }
}