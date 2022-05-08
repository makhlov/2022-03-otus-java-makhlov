package ru.otus.framework;

import ru.otus.framework.annotation.After;
import ru.otus.framework.annotation.Before;
import ru.otus.framework.annotation.Test;

import java.lang.reflect.Method;
import java.util.List;

import static ru.otus.framework.util.ReflectionHelper.callMethod;
import static ru.otus.framework.util.ReflectionHelper.getMethodsFromClassWithAnnotation;
import static ru.otus.framework.util.ReflectionHelper.instantiate;


public class TestPerformer {
    private TestPerformer() { }

    public static void performTests(String className) throws ClassNotFoundException {
        final Class<?> underTestClass = Class.forName(className);
        int failed = 0;
        var underTestMethods = getMethodsFromClassWithAnnotation(underTestClass, Test.class);
        for (var method : underTestMethods) {
            Object underTestObject = instantiate(underTestClass);
            try {
                runBeforeMethods(underTestObject, underTestClass);
                callMethod(underTestObject, method);
            } catch (Exception e) {
                System.out.println("\nTest failed with message: " + e.getMessage());
                failed++;
            } finally {
                runAfterMethods(underTestObject, underTestClass);
            }
        }
        System.out.println(getStatisticString(underTestMethods.size(), failed));
    }

    private static void runMethods(Object classInstance, List<Method> methods) {
        methods.forEach(method -> callMethod(classInstance, method));
    }

    private static void runBeforeMethods(Object underTestObject, Class<?> underTestClass) {
        runMethods(underTestObject, getMethodsFromClassWithAnnotation(underTestClass, Before.class));
    }

    private static void runAfterMethods(Object underTestObject, Class<?> underTestClass) {
        runMethods(underTestObject, getMethodsFromClassWithAnnotation(underTestClass, After.class));
    }

    private static String getStatisticString(int total, int failed) {
        return String.format("\n-------------------\nTotal: %d\nPassed: %d\nFailed: %d", total, total - failed, failed);
    }
}