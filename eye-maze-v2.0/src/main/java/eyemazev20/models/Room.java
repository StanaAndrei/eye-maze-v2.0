package eyemazev20.models;

import java.util.*;

public class Room {
    private String [] plUUIDs;
    private Set<String> ready;
    public Game game = null;
    private boolean isPublic = false;

    public boolean canLaunch() {
        return ready.size() == plUUIDs.length;
    }

    public void resetNrOfReady() {
        ready.clear();
    }

    public boolean addToReady(final String player) {
        assert (plUUIDs[0].equals(player) || plUUIDs[1].equals(player));
        if (ready.contains(player)) {
            return false;
        }
        ready.add(player);
        return true;
    }

    public Room(final String first) {
        ready = new HashSet<>();
        plUUIDs = new String[]{first, null};
    }

    @Override
    public String toString() {
        return "Room{" +
                "name=" + Arrays.toString(plUUIDs) +
                '}';
    }

    public void addSecond(final String second) {
        if (plUUIDs[0] == null) {
            plUUIDs[0] = second;
        } else if (plUUIDs[1] == null) {
            plUUIDs[1] = second;
        }
    }

    public String[] getPlUUIDs() {
        return plUUIDs;
    }

    public void removePlayer(int nr) {
        assert nr < plUUIDs.length;
        plUUIDs[nr] = null;

    }
}
