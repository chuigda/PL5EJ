package tech.icey.pl5.syntax;

import java.util.List;

public final class BeginNode extends ControlBlockNode {
    public BeginNode(String file, int line, List<Node> content) {
        super(file, line, content);
    }
}
