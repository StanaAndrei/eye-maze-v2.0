package eyemazev20.Dtos.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.sql.Timestamp;

@AllArgsConstructor
public class GlobalMessageOutDto {
    private String content;
    private Timestamp timestp;
    private String profilePicB64;
    private String username;
    private String senderId;

    @Override
    public String toString() {
        final var gmodJson = new JSONObject();
        gmodJson.put("content", content);
        if (timestp != null) {
            gmodJson.put("timestp", timestp.toLocalDateTime().toString());
        }
        gmodJson.put("profilePicB64", profilePicB64);
        gmodJson.put("username", username);
        gmodJson.put("senderId", senderId);
        return gmodJson.toString();
    }
}
