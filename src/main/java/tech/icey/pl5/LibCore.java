package tech.icey.pl5;

import org.jetbrains.annotations.Nullable;
import tech.icey.pl5.rt.InterpretRuntime;
import tech.icey.pl5.value.FunctionValue;
import tech.icey.pl5.value.MutablePairValue;
import tech.icey.pl5.value.PairValue;

import java.util.Objects;
import java.util.function.Consumer;

public final class LibCore {
    public static void register(InterpretRuntime runtime, @Nullable Consumer<String> onDuplicate) {
        runtime.registerJavaFunction("cons", cons, onDuplicate);
        runtime.registerJavaFunction("car", car, onDuplicate);
        runtime.registerJavaFunction("cdr", cdr, onDuplicate);
        runtime.registerJavaFunction("set-car!", setCar, onDuplicate);
        runtime.registerJavaFunction("set-cdr!", setCdr, onDuplicate);
        runtime.registerJavaFunction("eq?", eq, onDuplicate);
        runtime.registerJavaFunction("equals?", eq, onDuplicate);
        runtime.registerJavaFunction("=", eq, onDuplicate);
        runtime.registerJavaFunction("ne?", ne, onDuplicate);
        runtime.registerJavaFunction("not-equals?", ne, onDuplicate);
        runtime.registerJavaFunction("!=", ne, onDuplicate);
        runtime.registerJavaFunction("<>", ne, onDuplicate);
        runtime.registerJavaFunction("and", and, onDuplicate);
        runtime.registerJavaFunction("or", or, onDuplicate);
    }

    private static final String SINKRATE = "__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED";

    private static final FunctionValue cons = args -> {
        if (args.length != 2) {
            throw new PL5Exception("cons expects 2 arguments", nameof("cons"));
        } else {
            return new MutablePairValue(args[0], args[1]);
        }
    };

    private static final FunctionValue car = args -> {
        if (args.length != 1) {
            throw new PL5Exception("car expects 1 argument", nameof("car"));
        }

        Object arg = args[0];
        return switch (arg) {
            case PairValue pairValue -> pairValue.car();
            case MutablePairValue mutablePairValue -> mutablePairValue.car();
            case null, default -> throw new PL5Exception("car expects a pair", nameof("car"));
        };
    };

    private static final FunctionValue cdr = args -> {
        if (args.length != 1) {
            throw new PL5Exception("cdr expects 1 argument", nameof("cdr"));
        }

        Object arg = args[0];
        return switch (arg) {
            case PairValue pairValue -> pairValue.cdr();
            case MutablePairValue mutablePairValue -> mutablePairValue.cdr();
            case null, default -> throw new PL5Exception("cdr expects a pair", nameof("cdr"));
        };
    };

    private static final FunctionValue setCar = args -> {
        if (args.length != 2) {
            throw new PL5Exception("set-car! expects 2 arguments", nameof("setCar"));
        }

        Object arg1 = args[0];
        Object arg2 = args[1];
        switch (arg1) {
            case MutablePairValue mutablePairValue -> mutablePairValue.setCar(arg2);
            case PairValue _ -> throw new PL5Exception("set-car! cannot operate on an immutable pair", nameof("setCar"));
            case null, default -> throw new PL5Exception("set-car! expects a pair", nameof("setCar"));
        }
        return null;
    };

    private static final FunctionValue setCdr = args -> {
        if (args.length != 2) {
            throw new PL5Exception("set-cdr! expects 2 arguments", nameof("setCdr"));
        }

        Object arg1 = args[0];
        Object arg2 = args[1];
        switch (arg1) {
            case MutablePairValue mutablePairValue -> mutablePairValue.setCdr(arg2);
            case PairValue _ -> throw new PL5Exception("set-cdr! cannot operate on an immutable pair", nameof("setCdr"));
            case null, default -> throw new PL5Exception("set-cdr! expects a pair", nameof("setCdr"));
        }
        return null;
    };

    private static final FunctionValue eq = args -> {
        if (args.length != 2) {
            throw new PL5Exception("eq? expects 2 arguments", nameof("eq"));
        }
        return Objects.equals(args[0], args[1]);
    };

    private static final FunctionValue ne = args -> {
        if (args.length != 2) {
            throw new PL5Exception("ne? expects 2 arguments", nameof("ne"));
        }
        return !Objects.equals(args[0], args[1]);
    };

    private static final FunctionValue and = _ -> {
        throw new IllegalCallerException(SINKRATE);
    };

    private static final FunctionValue or = _ -> {
        throw new IllegalCallerException(SINKRATE);
    };

    private static String nameof(String function) {
        return "(Java) " + LibCore.class.getName() + "." + function;
    }
}
