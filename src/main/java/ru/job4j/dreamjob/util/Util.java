package ru.job4j.dreamjob.util;

import java.time.format.DateTimeFormatter;

public class Util {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static DateTimeFormatter getFormatter() {
        return FORMATTER;
    }
}