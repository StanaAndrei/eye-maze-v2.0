package eyemazev20.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    private int line = 0, col = 0;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        final var point = (Point) object;
        return line == point.line && col == point.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, col);
    }

    public JSONObject toJson() {
        var p = new JSONObject();
        p.put("line", line);
        p.put("col", col);
        return p;
    }
}
