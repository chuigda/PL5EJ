package tech.icey.pl5.rt;

import org.jetbrains.annotations.Nullable;
import tech.icey.pl5.util.Action;
import tech.icey.pl5.value.FunctionValue;

import java.util.HashMap;

public final class InterpretRuntime {
    private final HashMap<String, Scope> packageScopes;
    private final HashMap<String, FunctionValue> javaFunctions;
    private @Nullable Scope currentScope = null;

    public InterpretRuntime() {
        this.packageScopes = new HashMap<>();
        this.javaFunctions = new HashMap<>();
    }

    public void registerJavaFunction(String name, FunctionValue function, @Nullable Action<String> onDuplicate) {
        if (onDuplicate != null && javaFunctions.containsKey(name)) {
            onDuplicate.apply(name);
        }
        javaFunctions.put(name, function);
    }
}
