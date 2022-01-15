package eyemazev20.models.orm;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@Entity
@Table(name = "Users")
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private int id;

    @Column(name = "loginUUID", updatable = false, unique = true)
    private String loginUUID;

    @Column(name = "username")
    private String username;

    @Column(name = "userpassword")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @GeneratedValue
    @Generated(GenerationTime.INSERT)/*def is blank profile pic code*/
    @Column(name = "profilePicB64")
    private String profilePicB64;

    public String getProfilePicB64() {
        return profilePicB64;
    }

    public void setProfilePicB64(String profilePicB64) {
        this.profilePicB64 = profilePicB64;
    }//*/

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
