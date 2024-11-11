package tech.icey.pl5.syntax;

import java.util.List;

public abstract sealed class ControlBlockNode extends Node permits
        BeginNode,
        LoopNode,
        BreakNode
{
    private final List<Node> content;

    public ControlBlockNode(String file, int line, List<Node> content) {
        super(file, line);
        this.content = content;
    }

    public final List<Node> content() {
        return content;
    }
}
