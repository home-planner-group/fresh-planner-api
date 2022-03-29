package utility;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger to getting a clean test log.
 */
public class TestLogger {

    private static final Logger LOGGER = Logger.getLogger(TestLogger.class.getName());

    public static void info(String message) {
        LOGGER.log(Level.INFO, message + getExecutingMethod());
    }

    private static String getExecutingMethod() {
        StackTraceElement stacktraceElement = Thread.currentThread().getStackTrace()[3];
        return " [Unit Test: " + stacktraceElement.getClassName() + "::" + stacktraceElement.getMethodName() + "]";
    }
}
