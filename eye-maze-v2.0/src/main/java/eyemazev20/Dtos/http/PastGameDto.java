package eyemazev20.Dtos.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PastGameDto {
    private String []playersName;
    private int []scores;
    private Timestamp timestp;
    private String mzName;
    private String []profilePics;

    public JSONObject toJson() {
        final var jsonObject = new JSONObject();
        jsonObject.put("scores", scores);
        jsonObject.put("playersName", playersName);
        jsonObject.put("timestp", timestp);
        jsonObject.put("mzName", mzName);
        jsonObject.put("profilePics", profilePics);
        return jsonObject;
    }
}
