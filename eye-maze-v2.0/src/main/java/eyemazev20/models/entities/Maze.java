package eyemazev20.models.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eyemazev20.Services.MazeGenService;
import eyemazev20.utils.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javafx.util.Pair;

public class Maze {
    private MazeCell[][]mazeCells;
    private Point start, finish;

    public Point getFinish() {
        return finish;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public MazeCell[][] getMazeCells() {
        return mazeCells;
    }

    public Maze(int n, int m, ArrayList<MazeGenService.Cell> way, Point start, Point end) {
        assert (start.getLine() >= 0 && start.getCol() >= 0);
        assert (end.getLine() < n && end.getCol() < m);
        this.start = start;
        this.finish = end;
        mazeCells = new MazeCell[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mazeCells[i][j] = new MazeCell(i, j);
            }
        }

        for (int i = 1; i < way.size(); i++) {
            final int lastLine = way.get(i - 1).line;
            final int lastCol = way.get(i - 1).col;

            final int currLine = way.get(i).line;
            final int currCol = way.get(i).col;

            MazeCell.removeWalls(mazeCells[lastLine][lastCol], mazeCells[currLine][currCol]);
        }
    }

    public Maze(final String mazeForm) throws JsonProcessingException {
        //System.out.println(mazeForm);
        final var objMapper = new ObjectMapper();
        final var jsonMz = new JSONObject(mazeForm);

        final int nrLines = jsonMz.getInt("nrLines");
        final int nrCols = jsonMz.getInt("nrCols");
        this.start = objMapper.readValue(jsonMz.get("start").toString(), Point.class);
        this.finish = objMapper.readValue(jsonMz.get("finish").toString(), Point.class);

        final List<MazeCell> cells1d = objMapper.readerForListOf(MazeCell.class)
                                                    .readValue(jsonMz.get("cells").toString());
        mazeCells = new MazeCell[nrLines][nrCols];
        for (int i = 0, nr = 0; i < nrLines; i++) {
            for (int j = 0; j < nrCols; j++) {
                mazeCells[i][j] = cells1d.get(nr++);
            }
        }
    }

    public Pair<UUID, String> toDbObj() {
        JSONObject mazeOrm = new JSONObject();

        mazeOrm.put("nrLines", mazeCells.length);
        mazeOrm.put("nrCols", mazeCells[0].length);
        mazeOrm.put("start", start.toJson());
        mazeOrm.put("finish", finish.toJson());

        JSONArray cellsAsJson = new JSONArray();
        for (final var line : mazeCells) {
            for (final var cell : line) {
                final var cellAsJson = new JSONObject();
                cellAsJson.put("line", cell.getLine());
                cellAsJson.put("col", cell.getCol());
                cellAsJson.put("hasCoin", cell.getHasCoin());
                cellAsJson.put("walls", cell.getWalls());

                cellsAsJson.put(cellAsJson.toString());
            }
        }
        mazeOrm.put("cells", cellsAsJson);

        return new Pair<>(UUID.randomUUID(), mazeOrm.toString());
    }
}
