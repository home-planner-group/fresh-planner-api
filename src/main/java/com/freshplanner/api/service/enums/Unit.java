package com.freshplanner.api.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Unit {
    PIECE, ML, GRAM;

    private static Stream<Unit> streamAll() {
        return Arrays.stream(Unit.class.getEnumConstants());
    }

    public static List<Unit> getAll() throws RuntimeException {
        return streamAll().collect(Collectors.toList());
    }

    @JsonCreator // decodes @RequestBody
    public static Unit decode(String value) throws RuntimeException {
        return EnumExtension.decode(streamAll(), value);
    }
}
