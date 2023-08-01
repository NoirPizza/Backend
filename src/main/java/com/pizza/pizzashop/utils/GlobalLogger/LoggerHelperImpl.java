package com.pizza.pizzashop.utils.GlobalLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class implements LoggerHelper interface that provides entrypoint for logging messages using slf4j logger.
 */
public class LoggerHelperImpl implements LoggerHelper {
    private Logger logger = LoggerFactory.getLogger(LoggerHelperImpl.class);
    private static final String LOG_DIRECTORY = "logs/";

    public Logger getLogger() {
        return this.logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Logs the provided message at the specified log level and writes it to a log file and to the console.
     *
     * @param level   The log level (e.g., "DEBUG", "INFO", "WARN", "ERROR").
     * @param message The message to be logged.
     * @throws IllegalArgumentException If an invalid log level is provided.
     */
    public void logMessage(String level, String message) throws IllegalArgumentException {
        String logEntry = String.format("[%s] %s", level, message);
        switch (level) {
            case "DEBUG" -> logger.debug(message);
            case "INFO" -> logger.info(message);
            case "WARN" -> logger.warn(message);
            case "ERROR" -> logger.error(message);
            default -> throw new IllegalArgumentException("Invalid log level: " + level);
        }
        writeToFile(logEntry);
    }

    /**
     * Writes the log entry to the log file for the current date.
     *
     * @param logEntry The log entry to be written to the log file.
     */
    private void writeToFile(String logEntry) {
        String logFileName = getLogFileName();
        String timestamp = getCurrentTimestamp();

        String logEntryWithTimestamp = String.format("[%s] %s", timestamp, logEntry);

        try (FileWriter fileWriter = new FileWriter(logFileName, true)) {
            fileWriter.write(logEntryWithTimestamp + System.lineSeparator());
        } catch (IOException e) {
            logger.error(String.format("Failed to write log entry to file: %s", e.getMessage()));
        }
    }

    /**
     * Generates the log file name based on the current date.
     *
     * @return The log file name with the format "logs/yyyy-MM-dd.log".
     */
    private String getLogFileName() {
        LocalDate currentDate = LocalDate.now();
        String formattedDateTime = currentDate.format(DateTimeFormatter.ISO_DATE);
        return LOG_DIRECTORY + formattedDateTime + ".log";
    }

    /**
     * Gets the current timestamp in the format "yyyy-MM-dd HH:mm:ss.SSS".
     *
     * @return The current timestamp as a formatted string.
     */
    private String getCurrentTimestamp() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
