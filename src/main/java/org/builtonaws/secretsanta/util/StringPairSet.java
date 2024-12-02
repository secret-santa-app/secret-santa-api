package org.builtonaws.secretsanta.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

public final class StringPairSet {
    private final Map<String, Set<String>> inner = new HashMap<>();

    public boolean contains(String first, String second) {
        return get(first).contains(second);
    }

    public Set<String> get(String first) {
        return Collections.unmodifiableSet(inner.getOrDefault(first, Collections.emptySet()));
    }

    public Stream<Map.Entry<String, String>> entryStream() {
        return inner.entrySet().stream().flatMap(entry -> entry.getValue().stream()
                .map(second -> Map.entry(entry.getKey(), second))
                .filter(pair -> pair.getKey().compareTo(pair.getValue()) <= 0));
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return entryStream().collect(Collectors.toUnmodifiableSet());
    }

    public void put(String first, String second) {
        var firstMapping = inner.computeIfAbsent(first, k -> new HashSet<>());
        firstMapping.add(second);
        var secondMapping = inner.computeIfAbsent(second, k -> new HashSet<>());
        secondMapping.add(first);
    }

    @JsonValue
    public List<List<String>> serialized() {
        return entryStream()
                .map(entry -> List.of(entry.getKey(), entry.getValue()))
                .toList();
    }

    @JsonCreator
    public static StringPairSet ofSerialized(@Nullable List<@Nullable List<@Nullable String>> serialized) {
        var result = new StringPairSet();
        Objects.requireNonNull(serialized);
        for (var i = 0; i < serialized.size(); i++) {
            var pair = Objects.requireNonNull(serialized.get(i));
            if (pair.size() != 2) {
                throw new IllegalArgumentException(
                        "Expected pair at index " + i + " but got " + pair.size() + "-tuple.");
            }
            result.put(Objects.requireNonNull(pair.get(0)), Objects.requireNonNull(pair.get(1)));
        }
        return result;
    }

    @Override
    public String toString() {
        return "StringPairSet" + serialized();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StringPairSet that)) return false;
        return Objects.equals(inner, that.inner);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(inner);
    }
}
