package com.freshplanner.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RoleName {
    ROLE_USER,
    ROLE_EDITOR,
    ROLE_ADMIN;

    private static Stream<RoleName> streamAll() {
        return Arrays.stream(RoleName.class.getEnumConstants());
    }

    public static List<RoleName> getAll() throws RuntimeException {
        return streamAll().collect(Collectors.toList());
    }

    @JsonCreator // decodes @RequestBody
    public static RoleName decode(String value) throws RuntimeException {
        return EnumExtension.decode(streamAll(), value);
    }
}
