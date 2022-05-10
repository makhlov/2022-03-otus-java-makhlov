package ru.otus;

import ru.otus.framework.annotation.After;
import ru.otus.framework.annotation.Before;
import ru.otus.framework.annotation.Test;

public class DummyClassTest {
    @Before
    void before() {
        System.out.println("\n<Before method is running>");
    }

    @After
    void after() {
        System.out.println("\n<After method is running>");
    }

    @Test
    void someFailedTest() {
        DummyClass.runFailed();
    }

    @Test
    void successTest() {
        DummyClass.runSuccessful();
    }
}