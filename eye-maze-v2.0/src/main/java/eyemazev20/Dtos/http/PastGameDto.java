package eyemazev20.Dtos.http;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class PastGameDto {
    private String []playersName;
    private int []scores;
    private Timestamp timestp;

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scores", scores);
        jsonObject.put("playersName", playersName);
        jsonObject.put("timestp", timestp);
        return jsonObject;
    }
}
