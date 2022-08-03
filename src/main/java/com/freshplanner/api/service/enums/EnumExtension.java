package com.freshplanner.api.service.enums;

import java.util.Optional;
import java.util.stream.Stream;

public class EnumExtension {

    private EnumExtension() {
    }

    public static <T> T decode(Stream<T> stream, String value) {
        Optional<T> result = stream
                .filter(parameter ->
                        parameter.toString().equalsIgnoreCase(value)
                                || parameter.toString().replace('_', ' ').equalsIgnoreCase(value)
                                || parameter.toString().replaceAll("\\s", "").equalsIgnoreCase(value))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("No match in in EnumExtension decode() for value: " + value);
        }
    }
}
