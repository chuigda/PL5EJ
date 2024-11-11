package tech.icey.pl5.syntax;

import org.jetbrains.annotations.Nullable;

public final class ValueNode extends Node {
    @Nullable
    private final Object value;

    public ValueNode(String file, int line, @Nullable Object value) {
        super(file, line);
        this.value = value;
    }

    public @Nullable Object value() {
        return value;
    }
}
