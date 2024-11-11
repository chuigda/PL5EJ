package tech.icey.pl5.util;

@FunctionalInterface
public interface Action<T> {
    void apply(T t);
}
