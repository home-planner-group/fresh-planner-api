package com.freshplanner.api.utility;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ApiLogger {

    private static final Logger LOGGER = Logger.getLogger(ApiLogger.class.getName());

    public static void error(String message) {
        LOGGER.log(Level.SEVERE, message + getExecutingMethod());
    }

    public static void warning(String message) {
        LOGGER.log(Level.WARNING, message + getExecutingMethod());
    }

    public static void info(String message) {
        LOGGER.log(Level.INFO, message + getExecutingMethod());
    }

    public static Level getLevel() {
        return LOGGER.getLevel();
    }

    private static String getExecutingMethod() {
        StackTraceElement stacktraceElement = Thread.currentThread().getStackTrace()[3];
        return " (" + stacktraceElement.getFileName() + "::" + stacktraceElement.getMethodName() + ")";
    }
}
