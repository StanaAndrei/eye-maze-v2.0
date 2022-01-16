package eyemazev20.models.orm;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Users")
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
@Data
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

    public User(String username, String password, String email, String loginUUID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.loginUUID = loginUUID;
    }//*/
}
