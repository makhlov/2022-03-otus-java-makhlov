package ru.otus;

/**
 * This class does absolutely nothing.
 * Designed to test the framework.
 */
public class DummyClass {
    private DummyClass() { }

    public static void runSuccessful() {
        System.out.println("\nSuccess");
    }

    public static void runFailed() {
        throw new RuntimeException("\nFailed");
    }
}