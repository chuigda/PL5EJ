package tech.icey.pl5.rt;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public record Scope(@Nullable Scope parent, HashMap<String, Object> vars) {}
