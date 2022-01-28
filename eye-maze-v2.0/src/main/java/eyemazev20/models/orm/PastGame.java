package eyemazev20.models.orm;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import eyemazev20.Dtos.http.PastGameDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Arrays;

@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = StringArrayType.class
        ),
        @TypeDef(
                name = "int-array",
                typeClass = IntArrayType.class
        )
})

@Entity
@Table(name = "PastGames")
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class PastGame {
    @Id
    @Column(name = "roomUUID")
    private String roomUUID;

    @Type(type = "int-array")
    @Column(name = "scores",
            updatable = false,
            columnDefinition = "INT[]"
    )
    private int[] scores;

    @Type(type = "string-array")
    @Column(
            name = "plUUIDs",
            updatable = false,
            columnDefinition = "text[]"
    )
    private String[] plUUIDs;

    public Timestamp getTimestp() {
        return timestp;
    }

    public void setTimestp(Timestamp timestp) {
        this.timestp = timestp;
    }

    @Column(name = "timestp")
    @ColumnDefault(value="CURRENT_TIMESTAMP")
    @Generated(GenerationTime.INSERT)
    private Timestamp timestp;//*/

    @Column(name = "maze_id")
    private int mazeId;

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    public String getRoomUUID() {
        return roomUUID;
    }

    public void setRoomUUID(String roomUUID) {
        this.roomUUID = roomUUID;
    }

    public PastGame(String roomUUID, String[] plUUIDs, int[] scores, int mazeId) {
        this.roomUUID = roomUUID;
        this.plUUIDs = plUUIDs;
        this.scores = scores;
        this.mazeId = mazeId;
    }

    public String[] getPlUUIDs() {
        return plUUIDs;
    }

    public void setPlUUIDs(String[] plUUIDs) {
        this.plUUIDs = plUUIDs;
    }

    @Override
    public String toString() {
        return "PastGame{" +
                "roomUUID='" + roomUUID + '\'' +
                ", scores=" + Arrays.toString(scores) +
                ", plUUIDs=" + Arrays.toString(plUUIDs) +
                ", timestp=" + timestp +
                ", mazeId=" + mazeId +
                '}';
    }
}
