package eyemazev20.models.orm;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "Users")
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Override
    public String toString() {
        return '{' +
                "\"id\":" + id +
                ", \"loginUUID\":\"" + loginUUID + '\"' +
                ", \"username\":\"" + username + '\"' +
                ", \"password\":\"" + password + '\"' +
                ", \"email\":\"" + email + '\"' +
                '}';
    }

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
    }//*/

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
