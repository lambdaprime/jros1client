package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import id.jrosclient.ros.MethodCaller;

public class MethodCallerTest {

    @Test
    public void test_no_method() throws Exception {
        var caller = new MethodCaller("hello world");
        Assertions.assertThrows(NoSuchMethodException.class, () -> 
            caller.call("sadf"));
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void test_happy() throws Throwable {
        var list = new ArrayList();
        list.add("hello");
        var caller = new MethodCaller(list);
        assertEquals("[hello]", caller.call("toString"));
    }
    
    @Test
    public void test_void() throws Throwable {
        var caller = new MethodCaller("hello  ");
        assertEquals("hello", caller.call("trim"));
    }
}
