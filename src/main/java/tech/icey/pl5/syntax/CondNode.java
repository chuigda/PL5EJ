package tech.icey.pl5.syntax;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class CondNode extends Node {
    private final List<Node> condList;
    private final List<Node> thenList;
    private final @Nullable Node otherwise;

    public CondNode(String file, int line, List<Node> condList, List<Node> thenList, @Nullable Node otherwise) {
        super(file, line);
        this.condList = condList;
        this.thenList = thenList;
        this.otherwise = otherwise;
    }

    public List<Node> condList() {
        return condList;
    }

    public List<Node> thenList() {
        return thenList;
    }

    public @Nullable Node otherwise() {
        return otherwise;
    }
}
