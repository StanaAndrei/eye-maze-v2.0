package eyemazev20.Dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@NoArgsConstructor
@AllArgsConstructor
public class PastGameDto {
    private String []playersName;
    private int []scores;

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    public String[] getPlayersName() {
        return playersName;
    }

    public void setPlayersName(String[] playersName) {
        this.playersName = playersName;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scores", scores);
        jsonObject.put("playersName", playersName);
        return jsonObject;
    }
}
