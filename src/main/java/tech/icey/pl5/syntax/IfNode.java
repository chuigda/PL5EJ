package tech.icey.pl5.syntax;

import org.jetbrains.annotations.Nullable;

public final class IfNode extends Node {
    private final Node cond;
    private final Node then;
    private final @Nullable Node otherwise;

    public IfNode(String file, int line, Node cond, Node then, @Nullable Node otherwise) {
        super(file, line);
        this.cond = cond;
        this.then = then;
        this.otherwise = otherwise;
    }

    public Node cond() {
        return cond;
    }

    public Node then() {
        return then;
    }

    public @Nullable Node otherwise() {
        return otherwise;
    }
}
