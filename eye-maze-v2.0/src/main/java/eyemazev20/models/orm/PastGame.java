package eyemazev20.models.orm;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

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

    @Type(type = "string-array")
    @Column(
            name = "plUUIDs",
            updatable = false,
            columnDefinition = "text[]"
    )
    private String[] plUUIDs;

    @Type(type = "int-array")
    @Column(name = "scores",
            updatable = false,
            columnDefinition = "INT[]"
    )
    private int[] scores;

    public PastGame(String roomUUID, String[] plUUIDs, int[] scores) {
        this.roomUUID = roomUUID;
        this.plUUIDs = plUUIDs;
        this.scores = scores;
    }

    public String[] getPlUUIDs() {
        return plUUIDs;
    }

    public void setPlUUIDs(String[] plUUIDs) {
        this.plUUIDs = plUUIDs;
    }
}
