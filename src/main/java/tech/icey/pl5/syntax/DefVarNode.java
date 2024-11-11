package tech.icey.pl5.syntax;

public final class DefVarNode extends Node {
    private final String varName;
    private final Node init;

    public DefVarNode(String file, int line, String varName, Node init) {
        super(file, line);
        this.varName = varName;
        this.init = init;
    }

    public String varName() {
        return varName;
    }

    public Node init() {
        return init;
    }
}
