package tech.icey.pl5.syntax;

public abstract sealed class Node permits
        ValueNode,
        IdentNode,
        ControlBlockNode,
        ApplyNode,
        CondNode,
        IfNode,
        DefVarNode,
        DefuncNode
{
    private final String file;
    private final int line;

    public Node(String file, int line) {
        this.file = file;
        this.line = line;
    }

    public final String file() {
        return file;
    }

    public final int line() {
        return line;
    }
}
