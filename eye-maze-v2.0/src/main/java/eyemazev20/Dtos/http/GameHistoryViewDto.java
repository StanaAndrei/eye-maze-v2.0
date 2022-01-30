package eyemazev20.Dtos.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameHistoryViewDto {
    public enum STATE {
        WON,
        LOST,
        DRAW
    }

    private String code;
    private STATE state;

    @Override
    public String toString() {
        final var obj = new JSONObject();
        obj.put("code", code);
        obj.put("state", state);
        return obj.toString();
    }
}
