package id.jrosclient.ros;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Allows to call methods of an object by name dynamically at a
 * runtime.</p>
 * <p>Calls are done thru MethodHandle and not reflection which supposed
 * to be faster.</p>
 * <p>Overloaded methods are not supported.</p>
 */
public class MethodCaller {

    private Object object;
    private Map<String, MethodHandle> methods = new HashMap<>();

    /**
     * @param object object which methods will be called
     * @throws Exception
     */
    public MethodCaller(Object object) throws Exception {
        this.object = object;
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (var m: methods) {
            if (!Modifier.isPublic(m.getModifiers())) continue;
            if (Modifier.isNative(m.getModifiers())) continue;
            if (Modifier.isStatic(m.getModifiers())) continue;
            var mt = MethodType.methodType(m.getReturnType(), m.getParameterTypes());
            var mh = MethodHandles.lookup().findVirtual(clazz, m.getName(), mt);
            this.methods.put(m.getName(), mh);
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object call(String methodName, List args) throws Throwable, NoSuchMethodException {
        var mh = methods.get(methodName);
        if (mh == null) throw new NoSuchMethodException(methodName);
        List list = new ArrayList<>();
        list.add(object);
        list.addAll(args);
        return mh.invokeWithArguments(list);
    }
    
    public Object call(String methodName) throws Throwable, NoSuchMethodException {
        return call(methodName, Collections.emptyList());
    }
}
