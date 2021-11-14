package eyemazev20.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "loginUUID", updatable = false)
    private String loginUUID;

    @Column(name = "username")
    private String username;

    @Column(name = "userpassword")
    private String password;

    @Column(name = "email")
    private String email;

    public User(String username, String password, String email, String loginUUID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.loginUUID = loginUUID;
    }

    public String getLoginUUID() {
        return loginUUID;
    }

    public void setLoginUUID(String loginUUID) {
        this.loginUUID = loginUUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
