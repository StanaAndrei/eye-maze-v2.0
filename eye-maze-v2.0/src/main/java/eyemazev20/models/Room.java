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
        if (players[0] == null) {
            players[0] = second;
        } else if (players[1] == null) {
            players[1] = second;
        }
    }

    public String[] getPlayers() {
        return players;
    }
}
