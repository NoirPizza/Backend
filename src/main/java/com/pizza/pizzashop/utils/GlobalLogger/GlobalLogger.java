package com.pizza.pizzashop.utils.GlobalLogger;

/**
 * This class is a wrapper for static usage of non-static method logMessage of class LoggerHelper.
 */
public class GlobalLogger {
    private static final LoggerHelper loggerHelper = new LoggerHelperImpl();

    private GlobalLogger() {
    }

    /**
     * Log message using slf4j logger to console and .log file.
     *
     * @param level   Logging level ("DEBUG", "INFO", "WARN", "ERROR").
     * @param message Some text message to log.
     * @throws IllegalArgumentException If provided illegal logging level.
     */
    public static void log(String level, String message) throws IllegalArgumentException {
        loggerHelper.logMessage(level, message);
    }
}
