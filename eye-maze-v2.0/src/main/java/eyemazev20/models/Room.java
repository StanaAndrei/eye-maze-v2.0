package eyemazev20.models;

import java.util.*;

public class Room {
    private String []players;
    private Set<String> ready;

    public boolean canLaunch() {
        return ready.size() == players.length;
    }

    public void resetNrOfReady() {
        ready.clear();
    }

    public boolean addToReady(final String player) {
        assert (players[0].equals(player) || players[1].equals(player));
        if (ready.contains(player)) {
            return false;
        }
        ready.add(player);
        return true;
    }

    public Room(final String first) {
        ready = new HashSet<>();
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

    public void removePlayer(int nr) {
        assert nr < players.length;
        players[nr] = null;

    }
}
