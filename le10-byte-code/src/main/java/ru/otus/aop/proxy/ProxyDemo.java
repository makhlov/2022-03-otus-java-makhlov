package ru.otus.aop.proxy;

public class ProxyDemo {
    public static void main(String[] args) {
        // (1) Нам нужен оригинальный класс (экземпляр)
        // (2) Нам необходимо перехватить все вызовы к классу, для этого создается Proxy, который стоит в середине и
        // перехватывает все вызовы к оригинальному классу.


        //Мы работаем не с оригинальным классом, а с его оберткой
        MyClassInterface myClass = Ioc.createMyClass();
        myClass.secureAccess("Security Param"); //Когда мы обращаемся к instance этой штуки мы сперва вызываем
        //метод прокси, а затем принимаем решение, вызываем мы оригинальный метод или нет
    }
}



