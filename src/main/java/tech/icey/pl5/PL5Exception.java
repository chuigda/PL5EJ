package tech.icey.pl5;

import java.util.ArrayList;
import java.util.List;

public final class PL5Exception extends Exception {
    private final List<String> pl5trace;

    public PL5Exception(String message, String rootTrace) {
        super(message);
        this.pl5trace = new ArrayList<>();
        this.pl5trace.add(rootTrace);
    }

    public List<String> pl5trace() {
        return pl5trace;
    }
}
