package bachelor.leonheyna;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;

public abstract class LoggerCreator {

    private final static String FS = System.getProperty("file.separator");
    private final static String USER_HOME = System.getProperty("user.home");

    LoggerCreator() {
    }

    public static Logger defaultLogger(String logName) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        PatternLayoutEncoder ple0 = new PatternLayoutEncoder();

        ple0.setPattern("%msg%n");
        ple0.setContext(lc);
        ple0.start();
        fileAppender.setFile(USER_HOME + FS + "Desktop" + FS + "logs" + FS + "component-" + logName + ".csv");
        fileAppender.setEncoder(ple0);
        fileAppender.setContext(lc);
        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(logName);
        logger.addAppender(fileAppender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false);

        return logger;
    }
}
