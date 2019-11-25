package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

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

    private static String readFile(String file) {
        var str = "";
        try {
            str = new BufferedReader(new InputStreamReader(
                    TestUtils.class.getResource(file).openStream())).lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return str;
    }

}
