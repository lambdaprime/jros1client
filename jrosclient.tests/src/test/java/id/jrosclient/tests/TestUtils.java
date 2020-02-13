package id.jrosclient.tests;

import static id.xfunction.XUtils.readResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import id.xfunction.TemplateMatcher;

public class TestUtils {

    public static void compare(String out, String file) {
        var str = readResource(file);
        assertEquals(str, out);
    }

    public static void compareWithTemplate(String out, String templateFile) {
        var template = readResource(templateFile);
        assertTrue(new TemplateMatcher(template).matches(out));
    }

}
