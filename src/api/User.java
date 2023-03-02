package api;

public class User {
    private String firstname;
    private String lastname;
    private int id;
    private int password;
    private int amount;

    public User(int id, String firstname, String lastname, int password, int amount) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.amount = amount;
    }

    public int getId(){
        return id;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return  lastname;
    }
    public int getPassword(){
        return password;
    }
    public int getAmount() { return amount; }
}
