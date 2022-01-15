package eyemazev20.models.orm;

import eyemazev20.Services.GameService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Entity
@Table(name = "GlobalMessages")
@Data
public class GlobalMessage {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "cont", updatable = false)
    private String content;

    @Column(name = "timestp")
    private Timestamp timestp;

    @JoinColumn(name = "senderId", referencedColumnName = "id", nullable = false)//ref to user
    private int senderId;

    public GlobalMessage(String content, Timestamp timestp, int senderId) {
        this.content = content;
        this.timestp = timestp;
        this.senderId = senderId;
    }
}