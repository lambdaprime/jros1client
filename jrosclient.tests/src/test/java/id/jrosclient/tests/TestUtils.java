package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import id.xfunction.TemplateMatcher;

public class TestUtils {

    public static void compare(String out, String file) {
        var str = readFile(file);
        assertEquals(str, out);
    }

    public static void compareWithTemplate(String out, String templateFile) {
        var template = readFile(templateFile);
        assertTrue(new TemplateMatcher(template).matches(out));
    }

    public static Stream<String> readFileAsStream(String file) {
        try {
            return new BufferedReader(new InputStreamReader(
                    TestUtils.class.getResource(file).openStream())).lines();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFile(String file) {
        return readFileAsStream(file)
                .collect(Collectors.joining("\n"));
    }

}
