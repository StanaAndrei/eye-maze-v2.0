package eyemazev20.Dtos.ws;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class LaunchResMess {
    public enum State {
        READY,
        NOT_READY,
        LAUNCH
    }

    private String who;
    private State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
