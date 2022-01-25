package eyemazev20.Dtos.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JoinLeaveDto {

    public enum STATE {
        JOINED, LEFT
    }

    String who;
    STATE state;
}
