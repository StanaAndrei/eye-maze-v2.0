package eyemazev20.utils;

import javafx.util.Pair;

import java.util.Comparator;

public class PairCmpPq implements Comparator<Pair<Long, String>> {

    @Override
    public int compare(Pair<Long, String> s1, Pair<Long, String> s2) {
        if (s1.getKey() < s2.getKey()) {
            return 1;
        }
        if (s1.getKey() > s2.getKey()) {
            return -1;
        }
        return 0;
    }
}
