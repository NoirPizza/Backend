package com.pizza.pizzashop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GlobalLogger {
    private static final Logger logger = LoggerFactory.getLogger(GlobalLogger.class);
    private static final String LOG_DIRECTORY = "logs/";

    public static void log(String level, String message) {
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

    private static void writeToFile(String logEntry) {
        String logFileName = getLogFileName();
        String timestamp = getCurrentTimestamp();

        String logEntryWithTimestamp = String.format("[%s] %s", timestamp, logEntry);

        try (FileWriter fileWriter = new FileWriter(logFileName, true)) {
            fileWriter.write(logEntryWithTimestamp + System.lineSeparator());
        } catch (IOException e) {
            logger.error("Failed to write log entry to file: " + e.getMessage());
        }
    }

    private static String getLogFileName() {
        LocalDate currentDate = LocalDate.now();
        String formattedDateTime = currentDate.format(DateTimeFormatter.ISO_DATE);
        return LOG_DIRECTORY + formattedDateTime + ".log";
    }

    private static String getCurrentTimestamp() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
