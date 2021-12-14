package eyemazev20.models;

import java.util.Arrays;

public class Room {
    final public String []players;

    public Room(final String first) {
        players = new String[]{first, null};
    }

    @Override
    public String toString() {
        return "Room{" +
                "players=" + Arrays.toString(players) +
                '}';
    }

    public void addSecond(final String second) {
        players[1] = second;
    }

    public String[] getPlayers() {
        return players;
    }
}
