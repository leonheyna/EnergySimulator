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

    LoggerCreator() {}

    public static Logger createYMLLogger() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();

        ple.setPattern("%msg%n");
        ple.setContext(lc);
        ple.start();
        fileAppender.setFile(USER_HOME + FS + "Desktop" + FS + "logs" + FS + "ymlLogger.yml");
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger("ymlLogger");
        logger.addAppender(fileAppender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false);

        return logger;
    }

    public static Logger createMainLogger() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        PatternLayoutEncoder ple1 = new PatternLayoutEncoder();
        PatternLayoutEncoder ple2 = new PatternLayoutEncoder();

        ple1.setPattern("[%level] %msg%n");
        ple1.setContext(lc);
        ple1.start();
        fileAppender.setFile(USER_HOME + FS + "Desktop" + FS + "logs" + FS + "main.log");
        fileAppender.setEncoder(ple1);
        fileAppender.setContext(lc);
        fileAppender.start();

        ple2.setPattern("[%level] %msg%n");
        ple2.setContext(lc);
        ple2.start();
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setEncoder(ple2);
        consoleAppender.setContext(lc);
        consoleAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger("main");
        logger.addAppender(fileAppender);
        logger.addAppender(consoleAppender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false);

        return logger;
    }

    public static Logger createBeaconLogger() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        PatternLayoutEncoder ple1 = new PatternLayoutEncoder();
        PatternLayoutEncoder ple2 = new PatternLayoutEncoder();

        ple1.setPattern("[%level] %msg%n");
        ple1.setContext(lc);
        ple1.start();
        fileAppender.setFile(USER_HOME + FS + "Desktop" + FS + "logs" + FS + "beacon.log");
        fileAppender.setEncoder(ple1);
        fileAppender.setContext(lc);
        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger("beacon");
        logger.addAppender(fileAppender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false);

        return logger;
    }

    public static Logger createSmartControllerLogger() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        PatternLayoutEncoder ple1 = new PatternLayoutEncoder();
        PatternLayoutEncoder ple2 = new PatternLayoutEncoder();

        ple1.setPattern("[%level] %msg%n");
        ple1.setContext(lc);
        ple1.start();
        fileAppender.setFile(USER_HOME + FS + "Desktop" + FS + "logs" + FS + "smaCo.log");
        fileAppender.setEncoder(ple1);
        fileAppender.setContext(lc);
        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger("smaCo");
        logger.addAppender(fileAppender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false);

        return logger;
    }

    public static Logger createConsumerLogger() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        PatternLayoutEncoder ple1 = new PatternLayoutEncoder();
        PatternLayoutEncoder ple2 = new PatternLayoutEncoder();

        ple1.setPattern("[%level] %msg%n");
        ple1.setContext(lc);
        ple1.start();
        fileAppender.setFile(USER_HOME + FS + "Desktop" + FS + "logs" + FS + "consumer.log");
        fileAppender.setEncoder(ple1);
        fileAppender.setContext(lc);
        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger("consumer");
        logger.addAppender(fileAppender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false);

        return logger;
    }
}
