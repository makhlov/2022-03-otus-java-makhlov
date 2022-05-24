package ru.otus.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class Ioc {

    private Ioc() {
    }

    //Логирование добавляется внешним арбитром через Dynamic Proxy (штука довольно старая)
    static MyClassInterface createMyClass() {
        // 1. Создается экземпляр оригинального класса и помещается в специальный handler
        InvocationHandler handler = new DemoInvocationHandler(new MyClassImpl());
        return (MyClassInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{MyClassInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final MyClassInterface myClass;

        DemoInvocationHandler(MyClassInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //После того как handler заряжается нашим классом, сперва выполняется метод логирования
            //А затем мы выполняем метод оригинального класса
            System.out.println("invoking method:" + method);
            return method.invoke(myClass, args);
            //Этот метод передается в Proxy и мы с этой Proxy уже работаем

        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
