package eyemazev20.Dtos.http;

import eyemazev20.models.entities.MazeCell;
import eyemazev20.utils.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MazeFormDto {
    private int nrLines, nrCols;
    private Point start, finish;
    private MazeCell []mazeCells;
    private String name;

    @Override
    public String toString() {
        final var formAsJson = new JSONObject();
        formAsJson.put("start", start.toJson());
        formAsJson.put("finish", finish.toJson());
        formAsJson.put("nrLines", nrLines);
        formAsJson.put("nrCols", nrCols);
        formAsJson.put("name", name);

        final var array1d = new JSONArray();
        for (int j = 0; j < mazeCells.length; j++) {
            array1d.put(mazeCells[j]);
        }
        formAsJson.put("cells", array1d);

        return formAsJson.toString();
    }
}