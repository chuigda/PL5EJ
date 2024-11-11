package tech.icey.pl5.syntax;

import java.util.List;

public final class BreakNode extends ControlBlockNode {
    public BreakNode(String file, int line, List<Node> content) {
        super(file, line, content);
    }
}
