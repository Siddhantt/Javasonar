package com.example.sonar.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorServiceTest {

    private final CalculatorService calculatorService = new CalculatorService();

    @Test
    void testAdd() {
        assertEquals(5, calculatorService.add(2, 3));
    }

    @Test
    void testDivide() {
        assertEquals(2, calculatorService.divide(10, 5));
    }

    @Test
    void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> calculatorService.divide(10, 0));
    }
}
