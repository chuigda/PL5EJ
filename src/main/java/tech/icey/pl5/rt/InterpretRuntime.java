package tech.icey.pl5.rt;

import org.jetbrains.annotations.Nullable;
import tech.icey.pl5.value.FunctionValue;

import java.util.HashMap;
import java.util.function.Consumer;

public final class InterpretRuntime {
    private final HashMap<String, Scope> packageScopes;
    private final HashMap<String, FunctionValue> javaFunctions;
    private @Nullable Scope currentScope = null;

    public InterpretRuntime() {
        this.packageScopes = new HashMap<>();
        this.javaFunctions = new HashMap<>();
    }

    public void registerJavaFunction(String name, FunctionValue function, @Nullable Consumer<String> onDuplicate) {
        if (onDuplicate != null && javaFunctions.containsKey(name)) {
            onDuplicate.accept(name);
        }
        javaFunctions.put(name, function);
    }
}
