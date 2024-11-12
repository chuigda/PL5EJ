package tech.icey.pl5;

import org.jetbrains.annotations.Nullable;
import tech.icey.pl5.rt.InterpretRuntime;
import tech.icey.pl5.value.FunctionValue;
import tech.icey.pl5.value.MutablePairValue;
import tech.icey.pl5.value.PairValue;

import java.util.HashMap;
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
        runtime.registerJavaFunction("<", lessThan, onDuplicate);
        runtime.registerJavaFunction(">", greaterThan, onDuplicate);
        runtime.registerJavaFunction("<=", lessThanOrEquals, onDuplicate);
        runtime.registerJavaFunction(">=", greaterThanOrEquals, onDuplicate);
        runtime.registerJavaFunction("and", and, onDuplicate);
        runtime.registerJavaFunction("or", or, onDuplicate);
        runtime.registerJavaFunction("struct", struct, onDuplicate);
        runtime.registerJavaFunction("struct-get", structGet, onDuplicate);
        runtime.registerJavaFunction("struct-set!", structSet, onDuplicate);
        runtime.registerJavaFunction("str->int", parseInt, onDuplicate);
        runtime.registerJavaFunction("parse-int", parseInt, onDuplicate);
        runtime.registerJavaFunction("str->float", parseFloat, onDuplicate);
        runtime.registerJavaFunction("parse-float", parseFloat, onDuplicate);
        runtime.registerJavaFunction("to-string", toString, onDuplicate);
    }

    private static final String SINKRATE = "__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED";

    private static final FunctionValue cons = args -> {
        if (args.length != 2) {
            throw new PL5Exception(String.format("cons expects 2 arguments, got %d", args.length), nameof("cons"));
        } else {
            return new MutablePairValue(args[0], args[1]);
        }
    };

    private static final FunctionValue car = args -> {
        if (args.length != 1) {
            throw new PL5Exception(String.format("car expects 1 argument, got %d", args.length), nameof("car"));
        }

        Object arg = args[0];
        return switch (arg) {
            case PairValue pairValue -> pairValue.car();
            case MutablePairValue mutablePairValue -> mutablePairValue.car();
            case null, default -> throw new PL5Exception(String.format("car expects a pair, got %s", arg), nameof("car"));
        };
    };

    private static final FunctionValue cdr = args -> {
        if (args.length != 1) {
            throw new PL5Exception(String.format("cdr expects 1 argument, got %d", args.length), nameof("cdr"));
        }

        Object arg = args[0];
        return switch (arg) {
            case PairValue pairValue -> pairValue.cdr();
            case MutablePairValue mutablePairValue -> mutablePairValue.cdr();
            case null, default -> throw new PL5Exception(String.format("cdr expects a pair, got %s", arg), nameof("cdr"));
        };
    };

    private static final FunctionValue setCar = args -> {
        if (args.length != 2) {
            throw new PL5Exception(String.format("set-car! expects 2 arguments, got %d", args.length), nameof("setCar"));
        }

        Object arg1 = args[0];
        Object arg2 = args[1];
        switch (arg1) {
            case MutablePairValue mutablePairValue -> mutablePairValue.setCar(arg2);
            case PairValue _ -> throw new PL5Exception("set-car! cannot operate on an immutable pair", nameof("setCar"));
            case null, default -> throw new PL5Exception(String.format("set-car! expects a pair, got %s", arg1), nameof("setCar"));
        }
        return null;
    };

    private static final FunctionValue setCdr = args -> {
        if (args.length != 2) {
            throw new PL5Exception(String.format("set-cdr! expects 2 arguments, got %d", args.length), nameof("setCdr"));
        }

        Object arg1 = args[0];
        Object arg2 = args[1];
        switch (arg1) {
            case MutablePairValue mutablePairValue -> mutablePairValue.setCdr(arg2);
            case PairValue _ -> throw new PL5Exception("set-cdr! cannot operate on an immutable pair", nameof("setCdr"));
            case null, default -> throw new PL5Exception(String.format("set-cdr! expects a pair, got %s", arg1), nameof("setCdr"));
        }
        return null;
    };

    private static final FunctionValue eq = args -> {
        if (args.length != 2) {
            throw new PL5Exception(String.format("eq? expects 2 arguments, got %d", args.length), nameof("eq"));
        }
        return Objects.equals(args[0], args[1]);
    };

    private static final FunctionValue ne = args -> {
        if (args.length != 2) {
            throw new PL5Exception(String.format("ne? expects 2 arguments, got %d", args.length), nameof("ne"));
        }
        return !Objects.equals(args[0], args[1]);
    };

    private static final FunctionValue lessThan = args -> {
        if (args.length != 2) {
            throw new PL5Exception(String.format("< expects 2 arguments, got %d", args.length), nameof("lessThan"));
        }

        Object arg1 = args[0];
        Object arg2 = args[1];
        if (arg1 == null || arg2 == null) {
            throw new PL5Exception(String.format("< expects two non-null arguments, got %s and %s", arg1, arg2), nameof("lessThan"));
        }
        else if (arg1 instanceof String s1 && arg2 instanceof String s2) {
            return s1.compareTo(s2) < 0;
        } else if (arg1 instanceof Long l1 && arg2 instanceof Long l2) {
            return l1 < l2;
        } else if (arg1 instanceof Number n1 && arg2 instanceof Number n2) {
            return n1.doubleValue() < n2.doubleValue();
        } else if (arg1.getClass().equals(arg2.getClass()) && arg1 instanceof Comparable<?> comparable) {
            @SuppressWarnings("unchecked")
            Comparable<Object> c1 = (Comparable<Object>) comparable;
            return c1.compareTo(arg2) < 0;
        } else {
            throw new PL5Exception(String.format("< expects two comparable arguments, got %s and %s", arg1, arg2), nameof("lessThan"));
        }
    };

    private static final FunctionValue greaterThan = args -> {
        if (args.length != 2) {
            throw new PL5Exception(String.format("> expects 2 arguments, got %d", args.length), nameof("greaterThan"));
        }

        Object arg1 = args[0];
        Object arg2 = args[1];
        if (arg1 == null || arg2 == null) {
            throw new PL5Exception(String.format("> expects two non-null arguments, got %s and %s", arg1, arg2), nameof("greaterThan"));
        }
        else if (arg1 instanceof String s1 && arg2 instanceof String s2) {
            return s1.compareTo(s2) > 0;
        } else if (arg1 instanceof Long l1 && arg2 instanceof Long l2) {
            return l1 > l2;
        } else if (arg1 instanceof Number n1 && arg2 instanceof Number n2) {
            return n1.doubleValue() > n2.doubleValue();
        } else if (arg1.getClass().equals(arg2.getClass()) && arg1 instanceof Comparable<?> comparable) {
            @SuppressWarnings("unchecked")
            Comparable<Object> c1 = (Comparable<Object>) comparable;
            return c1.compareTo(arg2) > 0;
        } else {
            throw new PL5Exception(String.format("> expects two comparable arguments, got %s and %s", arg1, arg2), nameof("greaterThan"));
        }
    };

    private static final FunctionValue lessThanOrEquals = args -> {
        if (args.length != 2) {
            throw new PL5Exception(String.format("<= expects 2 arguments, got %d", args.length), nameof("lessThanOrEquals"));
        }

        Object arg1 = args[0];
        Object arg2 = args[1];
        if (arg1 == null || arg2 == null) {
            throw new PL5Exception(String.format("<= expects two non-null arguments, got %s and %s", arg1, arg2), nameof("lessThanOrEquals"));
        }
        else if (arg1 instanceof String s1 && arg2 instanceof String s2) {
            return s1.compareTo(s2) <= 0;
        } else if (arg1 instanceof Long l1 && arg2 instanceof Long l2) {
            return l1 <= l2;
        } else if (arg1 instanceof Number n1 && arg2 instanceof Number n2) {
            return n1.doubleValue() <= n2.doubleValue();
        } else if (arg1.getClass().equals(arg2.getClass()) && arg1 instanceof Comparable<?> comparable) {
            @SuppressWarnings("unchecked")
            Comparable<Object> c1 = (Comparable<Object>) comparable;
            return c1.compareTo(arg2) <= 0;
        } else {
            throw new PL5Exception(String.format("<= expects two comparable arguments, got %s and %s", arg1, arg2), nameof("lessThanOrEquals"));
        }
    };

    private static final FunctionValue greaterThanOrEquals = args -> {
        if (args.length != 2) {
            throw new PL5Exception(String.format(">= expects 2 arguments, got %d", args.length), nameof("greaterThanOrEquals"));
        }

        Object arg1 = args[0];
        Object arg2 = args[1];
        if (arg1 == null || arg2 == null) {
            throw new PL5Exception(String.format(">= expects two non-null arguments, got %s and %s", arg1, arg2), nameof("greaterThanOrEquals"));
        }
        else if (arg1 instanceof String s1 && arg2 instanceof String s2) {
            return s1.compareTo(s2) >= 0;
        } else if (arg1 instanceof Long l1 && arg2 instanceof Long l2) {
            return l1 >= l2;
        } else if (arg1 instanceof Number n1 && arg2 instanceof Number n2) {
            return n1.doubleValue() >= n2.doubleValue();
        } else if (arg1.getClass().equals(arg2.getClass()) && arg1 instanceof Comparable<?> comparable) {
            @SuppressWarnings("unchecked")
            Comparable<Object> c1 = (Comparable<Object>) comparable;
            return c1.compareTo(arg2) >= 0;
        } else {
            throw new PL5Exception(String.format(">= expects two comparable arguments, got %s and %s", arg1, arg2), nameof("greaterThanOrEquals"));
        }
    };

    private static final FunctionValue and = _ -> {
        throw new IllegalCallerException(SINKRATE);
    };

    private static final FunctionValue or = _ -> {
        throw new IllegalCallerException(SINKRATE);
    };

    private static final FunctionValue struct = args -> {
        if (args.length % 2 != 0) {
            throw new PL5Exception(String.format("struct expects an even number of arguments, got %d", args.length), nameof("struct"));
        }

        HashMap<String, Object> map = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            if (!(args[i] instanceof String s)) {
                throw new PL5Exception(String.format("struct expects a string as argument %d, got %s", i + 1, args[i]), nameof("struct"));
            }
            map.put(s, args[i + 1]);
        }
        return map;
    };

    public static final FunctionValue structGet = args -> {
        if (args.length != 2) {
            throw new PL5Exception(String.format("struct-get expects 2 arguments, got %d", args.length), nameof("structGet"));
        }

        Object arg1 = args[0];
        Object arg2 = args[1];
        if (!(arg1 instanceof HashMap<?, ?> map)) {
            throw new PL5Exception(String.format("struct-get expects a struct (java.util.HashMap) as argument 1, got %s", arg1), nameof("structGet"));
        }
        if (!(arg2 instanceof String s)) {
            throw new PL5Exception(String.format("struct-get expects a string as argument 2, got %s", arg2), nameof("structGet"));
        }
        return map.get(s);
    };

    public static final FunctionValue structSet = args -> {
        if (args.length != 3) {
            throw new PL5Exception(String.format("struct-set expects 3 arguments, got %d", args.length), nameof("structSet"));
        }

        Object arg1 = args[0];
        Object arg2 = args[1];
        Object arg3 = args[2];
        if (!(arg1 instanceof HashMap<?, ?> map)) {
            throw new PL5Exception(String.format("struct-set expects a struct (java.util.HashMap) as argument 1, got %s", arg1), nameof("structSet"));
        }
        if (!(arg2 instanceof String s)) {
            throw new PL5Exception(String.format("struct-set expects a string as argument 2, got %s", arg2), nameof("structSet"));
        }

        @SuppressWarnings("unchecked")
        HashMap<String, Object> newMap = (HashMap<String, Object>) map;
        return newMap.put(s, arg3);
    };

    public static final FunctionValue parseInt = args -> {
        if (args.length != 1) {
            throw new PL5Exception(String.format("parseInt expects 1 argument, got %d", args.length), nameof("parseInt"));
        }

        Object arg = args[0];
        if (arg instanceof String s) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (arg instanceof Number number) {
            return number.longValue();
        } else {
            throw new PL5Exception(String.format("parseInt expects a string, got %s", arg), nameof("parseInt"));
        }
    };

    public static final FunctionValue parseFloat = args -> {
        if (args.length != 1) {
            throw new PL5Exception(String.format("parseFloat expects 1 argument, got %d", args.length), nameof("parseFloat"));
        }

        Object arg = args[0];
        if (arg instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (arg instanceof Number number) {
            return number.doubleValue();
        } else {
            throw new PL5Exception(String.format("parseFloat expects a string, got %s", arg), nameof("parseFloat"));
        }
    };

    public static final FunctionValue toString = args -> {
        if (args.length != 1) {
            throw new PL5Exception(String.format("toString expects 1 argument, got %d", args.length), nameof("toString"));
        }
        return String.valueOf(args[0]);
    };

    private static String nameof(String function) {
        return "(Java) " + LibCore.class.getName() + "." + function;
    }
}
