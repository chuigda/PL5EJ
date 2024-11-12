package tech.icey.pl5.value;

import org.jetbrains.annotations.Nullable;
import tech.icey.pl5.PL5Exception;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.WrongMethodTypeException;

@FunctionalInterface
public interface FunctionValue {
    @Nullable Object apply(@Nullable Object ...args) throws PL5Exception;

    static FunctionValue make(MethodHandle methodHandle, String name, String qualName) {
        return args -> {
            try {
                return methodHandle.invokeWithArguments(args);
            } catch (ClassCastException | WrongMethodTypeException e) {
                throw new PL5Exception(name + ": invalid argument types: " + e.getMessage(), qualName);
            } catch (Throwable throwable) {
                throw new PL5Exception(throwable.getMessage(), qualName);
            }
        };
    }
}
