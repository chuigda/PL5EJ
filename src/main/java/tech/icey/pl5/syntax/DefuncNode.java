package tech.icey.pl5.syntax;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class DefuncNode extends Node {
    private final @Nullable String funcName;
    private final List<String> paramNames;
    private final Node body;

    public DefuncNode(
            String file,
            int line,
            @Nullable String funcName,
            List<String> paramNames,
            Node body
    ) {
        super(file, line);
        this.funcName = funcName;
        this.paramNames = paramNames;
        this.body = body;
    }

    public @Nullable String funcName() {
        return funcName;
    }

    public List<String> paramNames() {
        return paramNames;
    }

    public Node body() {
        return body;
    }
}
