package tech.icey.pl5.syntax;

import java.util.List;

public final class ApplyNode extends Node {
    private final Node callee;
    private final List<Node> args;

    public ApplyNode(String file, int line, Node callee, List<Node> args) {
        super(file, line);
        this.callee = callee;
        this.args = args;
    }

    public Node callee() {
        return callee;
    }

    public List<Node> args() {
        return args;
    }
}
