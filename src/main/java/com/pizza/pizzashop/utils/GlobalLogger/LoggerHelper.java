package com.pizza.pizzashop.utils.GlobalLogger;

/**
 * This interface provides an entrypoint for logging messages using slf4j logger.
 */
public interface LoggerHelper {
    void logMessage(String level, String message);
}
