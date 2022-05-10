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

        final var beforeMethods = getMethodsFromClassWithAnnotation(underTestClass, Before.class);
        final var afterMethods = getMethodsFromClassWithAnnotation(underTestClass, After.class);
        final var underTestMethods = getMethodsFromClassWithAnnotation(underTestClass, Test.class);

        int failed = 0;
        for (var method : underTestMethods) {
            Object underTestObject = instantiate(underTestClass);
            try {
                runBeforeMethods(underTestObject, beforeMethods);
                callMethod(underTestObject, method);
            } catch (Exception e) {
                System.out.println("\nTest failed with message: " + e.getMessage());
                failed++;
            } finally {
                runAfterMethods(underTestObject, afterMethods);
            }
        }
        System.out.println(getStatisticString(underTestMethods.size(), failed));
    }

    private static void runMethods(final Object classInstance, final List<Method> methods) {
        methods.forEach(method -> callMethod(classInstance, method));
    }

    private static void runBeforeMethods(final Object underTestObject, final List<Method> beforeMethods) {
        runMethods(underTestObject, beforeMethods);
    }

    private static void runAfterMethods(final Object underTestObject, final List<Method> afterMethods) {
        runMethods(underTestObject, afterMethods);
    }

    private static String getStatisticString(int total, int failed) {
        return String.format("\n-------------------\nTotal: %d\nPassed: %d\nFailed: %d", total, total - failed, failed);
    }
}