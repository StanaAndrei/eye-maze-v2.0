package eyemazev20.Services;

import eyemazev20.Utils.IdGetter;
import eyemazev20.Utils.PairCmpPq;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MatchMakingService {
    private static IdGetter idGetter = new IdGetter(Long.MIN_VALUE);
    private static AtomicReference<HashMap<String, Long>> posOf = new AtomicReference<>(new HashMap<>());
    private static AtomicReference<PriorityQueue<Pair<Long, String>>> searchers = new AtomicReference<>(
            new PriorityQueue<>(new PairCmpPq())
    );

    public static void addToOrRemoveFromSearchers(final String s) {
        if (posOf.get().get(s) == null) {
            final var pos = idGetter.get();
            posOf.get().put(s, pos);
            searchers.get().add(new Pair<>(pos, s));
        } else {
            searchers.get().remove(new Pair<>(posOf.get().get(s), s));
            posOf.get().remove(s);
        }
    }

    public static boolean canExtract() {
        return searchers.get().size() > 1;
    }

    public static Pair<String, String> extract() {
        final var fi = searchers.get().poll().getValue();
        final var se = searchers.get().poll().getValue();
        posOf.get().remove(fi);
        posOf.get().remove(se);
        return new Pair<>(fi, se);
    }
}
