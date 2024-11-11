package tech.icey.pl5.value;

import org.jetbrains.annotations.Nullable;
import tech.icey.pl5.PL5Exception;

@FunctionalInterface
public interface FunctionValue {
    @Nullable Object apply(@Nullable Object ...args) throws PL5Exception;
}
