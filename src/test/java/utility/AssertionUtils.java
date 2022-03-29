package utility;

import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Set;

/**
 * Custom assertions with custom methods.
 *
 * <p>Usage: import static utility.AssertionUtils.*;</p>
 */
public class AssertionUtils extends Assertions {

    public static <T> void assertContains(List<T> list, T expected, String message) {
        assertTrue(list.contains(expected), message);
    }

    public static <T> void assertContains(List<T> list, T expected) {
        assertContains(list, expected, "Expected list (" + list + ") to contain " + expected);
    }

    public static <T> void assertContains(Set<T> set, T expected, String message) {
        assertTrue(set.contains(expected), message);
    }

    public static <T> void assertContains(Set<T> set, T expected) {
        assertContains(set, expected, "Expected set (" + set + ") to contain " + expected);
    }

    public static <T> void assertNotContains(Set<T> set, T expected, String message) {
        assertFalse(set.contains(expected), message);
    }

    public static <T> void assertNotContains(Set<T> set, T expected) {
        assertNotContains(set, expected, "Expected set (" + set + ") to not contain " + expected);
    }
}
