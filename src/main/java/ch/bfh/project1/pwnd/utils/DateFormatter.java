package ch.bfh.project1.pwnd.utils;

import ch.bfh.project1.pwnd.MonitoringDemon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatter {

    private static final DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter inputDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter outputDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final Logger logger = LogManager.getLogger(DateFormatter.class);
    public static String inputToOutputData(String input) {
        try {
            // Try parsing as a date-time string
            LocalDateTime dateTime = LocalDateTime.parse(input, inputDateTimeFormatter);
            return dateTime.format(outputDateTimeFormatter);
        } catch (DateTimeParseException e) {
            // If parsing as a date-time string fails, try parsing as a date string
            try {
                LocalDate date = LocalDate.parse(input, inputDateFormatter);
                return date.format(outputFormatter);
            } catch (DateTimeParseException ex) {
                logger.error("Invalid date format: {}", input, e);
                throw new IllegalArgumentException("Invalid date format: " + input);
            }
        }
    }

    public static String getCurrentTimestamp(){
        return LocalDateTime.now().format(outputDateTimeFormatter);
    }
}
