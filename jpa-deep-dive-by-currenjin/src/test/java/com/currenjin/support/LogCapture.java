package com.currenjin.support;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LogCapture {
    public static List<String> execute(Runnable runnable) {
        List<String> sqlLogs = new ArrayList<>();

        Logger logger = (Logger) LoggerFactory.getLogger("org.hibernate.SQL");
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        try {
            runnable.run();
            sqlLogs.addAll(listAppender.list.stream()
                    .map(ILoggingEvent::getMessage)
                    .toList());
        } finally {
            logger.detachAppender(listAppender);
        }

        return sqlLogs;
    }
}
