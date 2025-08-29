package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Main class.
 */
public class MainTest {
    
    @Test
    public void testMainClassExists() {
        // This test verifies that the Main class can be instantiated
        assertNotNull(new Main());
    }
    
    @Test
    public void testMainMethodExists() {
        // This test verifies that the main method exists and is accessible
        try {
            Main.class.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            fail("Main method should exist in Main class");
        }
    }
} 