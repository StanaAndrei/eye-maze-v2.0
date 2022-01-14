package eyemazev20.Dtos.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserInfoUpDto {
    private String username, userpassword, email, profilePicB64;
}
