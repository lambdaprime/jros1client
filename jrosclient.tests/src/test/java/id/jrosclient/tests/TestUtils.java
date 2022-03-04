/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jrosclient
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import id.xfunction.ResourceUtils;
import id.xfunction.text.WildcardMatcher;

/** @author lambdaprime intid@protonmail.com */
public class TestUtils {

    private static final ResourceUtils resourceUtils = new ResourceUtils();

    public static void compare(String out, String file) {
        var str = resourceUtils.readResource(file);
        assertEquals(str, out);
    }

    public static void compareWithTemplate(String out, String templateFile) {
        var template = resourceUtils.readResource(templateFile);
        assertTrue(new WildcardMatcher(template).matches(out));
    }
}
