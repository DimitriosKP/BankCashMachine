package api;

public class User {
    private String firstname;
    private String lastname;
    private int password;

    public User(String firstname, String lastname, int password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setPassword(int password) {
        this.password = password;
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
}
