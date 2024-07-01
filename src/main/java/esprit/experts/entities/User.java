package esprit.experts.entities;

public class User {
    private static long ID =0 ;
     private long id ;
     private String firstname ;
     private  String lastname;
     private String password ;
     private String role ;
     private  String email  ;

    public User( String firstname,String lastname ,  String password, String role, String email) {
        this.id = ID++;
        this.firstname = firstname;
        this.password = password;
        this.role = role;
        this.email = email;
        this.lastname = lastname;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static long getID() {
        return ID;
    }

    public static void setID(long ID) {
        User.ID = ID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
