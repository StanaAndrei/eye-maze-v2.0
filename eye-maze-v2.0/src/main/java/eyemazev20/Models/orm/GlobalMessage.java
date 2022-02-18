package eyemazev20.Models.orm;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Entity(name = "GlobalMessage")
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

    @JoinColumn(name = "user_id", referencedColumnName = "senderId")
    private int senderId;

    public GlobalMessage(String content, Timestamp timestp, int senderId) {
        this.content = content;
        this.timestp = timestp;
        this.senderId = senderId;
    }
}