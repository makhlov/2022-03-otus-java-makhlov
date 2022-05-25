package proxy;

import annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

class Arbiter {

    private Arbiter() {}

    static Calculator create() {
        InvocationHandler handler = new LoggedMethodInvocationHandler(new CalculatorImpl());
        return (Calculator) Proxy.newProxyInstance(
                CalculatorImpl.class.getClassLoader(),
                CalculatorImpl.class.getInterfaces(),
                handler
        );
    }

    static class LoggedMethodInvocationHandler implements InvocationHandler {
        private final Calculator clazz;

        LoggedMethodInvocationHandler(final Calculator clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
            if (isMarkedByAnnotation(this.clazz, method)) {
                System.out.printf("\nexecuted method: %s, param:%s".formatted(method.getName(), Arrays.toString(args)));
            }
            return method.invoke(clazz, args);
        }

        private boolean isMarkedByAnnotation(Calculator clazz, Method method) throws NoSuchMethodException {
            return clazz.getClass()
                    .getMethod(method.getName(), method.getParameterTypes())
                    .isAnnotationPresent(Log.class);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + clazz +
                    '}';
        }
    }
}