package com.pizza.pizzashop.unit.utils;

import com.pizza.pizzashop.utils.GlobalLogger.LoggerHelperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class GlobalLoggerTests {
    @InjectMocks
    private LoggerHelperImpl loggerHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogMessageWithValidLogLevel() throws IOException {
        String level = "INFO";
        String message = "This is a test log message.";

        loggerHelper.logMessage(level, message);

        String logFileName = getLogFileName();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFileName))) {
            String logEntry = reader.readLine();
            assertTrue(logEntry.contains(level));
            assertTrue(logEntry.contains(message));
        }
    }

    @Test
    void testLogMessageWithInvalidLogLevel() {
        String invalidLevel = "INVALID_LEVEL";
        String message = "This is an invalid log level test.";

        assertThrows(IllegalArgumentException.class,
                () -> loggerHelper.logMessage(invalidLevel, message));
    }

    private String getLogFileName() {
        LocalDate currentDate = LocalDate.now();
        String formattedDateTime = currentDate.format(DateTimeFormatter.ISO_DATE);
        return "logs/" + formattedDateTime + ".log";
    }
}
