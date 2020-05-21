/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jrosclient.tests;

import static id.xfunction.XUtils.readResource;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.xfunction.TemplateMatcher;
import id.xfunction.XExec;

public class JRosClientIntegrationTests {

    private static final String JROSCLIENT_PATH = Paths.get("")
            .toAbsolutePath()
            .resolve("build/jrosclient/jrosclient")
            .toString();

    @BeforeEach
    void setup() throws IOException {
    }
    
    @AfterEach
    void cleanup() throws IOException {
    }
    
    @Test
    public void test() throws Exception {
        test_no_args();
        test_echo();
    }

    private void test_echo() {
        var out = runOk("--masterUrl http://ubuntu:11311/ --nodePort 1234 rostopic echo testTopic std_msgs/String");
        Assertions.assertTrue(new TemplateMatcher(readResource("echo")).matches(out));
    }

    private void test_no_args() throws Exception {
        var out = runFail("");
        Assertions.assertEquals(readResource("README.md") + "\n\n", out);
    }

    private String runFail(String fmt, Object...args) {
        return run(1, fmt, args);
    }

    private String runOk(String fmt, Object...args) {
        return run(0, fmt, args);
    }
    
    private String run(int expectedCode, String fmt, Object...args) {
        var proc = new XExec(JROSCLIENT_PATH + " " + String.format(fmt, args))
                .run();
        var code = proc.getCode();
        proc.flushStdout();
        proc.flushStderr();
        var out = proc.stdoutAsString() + "\n" + proc.stderrAsString() + "\n";
        System.out.print(out);
        Assertions.assertEquals(expectedCode, code);
        return out;
    }
    
}
