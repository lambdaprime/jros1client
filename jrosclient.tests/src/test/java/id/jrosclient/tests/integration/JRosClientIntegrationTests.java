/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jrosclient.tests.integration;

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
        test_echo_missing_args();
        test_debug();
        test_list();
    }

    private void test_echo() {
        var out = runOk("--masterUrl http://ubuntu:11311/ --nodePort 1234 rostopic echo -n 5 testTopic std_msgs/String");
        Assertions.assertTrue(new TemplateMatcher(readResource("echo")).matches(out));
    }

    private void test_echo_missing_args() {
        var out = runFail("--masterUrl http://ubuntu:11311/ --nodePort 1234 rostopic echo testTopic");
        Assertions.assertEquals(readResource("README.md") + "\n\n", out);
    }
    
    private void test_no_args() throws Exception {
        var out = runFail("");
        Assertions.assertEquals(readResource("README.md") + "\n\n", out);
    }

    private void test_debug() {
        var out = runOk("--masterUrl http://ubuntu:11311/ --nodePort 1234 --debug rostopic echo -n 1 testTopic std_msgs/String");
        Assertions.assertTrue(new TemplateMatcher(readResource("debug")).matches(out));
    }

    private void test_list() {
        var out = runOk("--masterUrl http://ubuntu:11311/ --nodePort 1234 rostopic list");
        Assertions.assertTrue(new TemplateMatcher(readResource("list")).matches(out));
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
        proc.flush(false);
        var code = proc.await();
        var out = proc.stdoutAsString() + "\n" + proc.stderrAsString() + "\n";
        System.out.print(out);
        Assertions.assertEquals(expectedCode, code);
        return out;
    }
    
}
