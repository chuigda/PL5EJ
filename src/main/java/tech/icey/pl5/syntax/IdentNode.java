package tech.icey.pl5.syntax;

public final class IdentNode extends Node {
    private final String ident;

    public IdentNode(String file, int line, String ident) {
        super(file, line);
        this.ident = ident;
    }

    public String ident() {
        return ident;
    }
}
