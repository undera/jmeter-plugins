package kg.apc.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 *  Configure log4j logging for JMeter since 3.2 in PluginsManagerCMD
 */
public class LoggingConfigurator {



    public LoggingConfigurator() {
        configure();
    }

    public void configure() {
        PatternLayout.Builder patternBuilder = PatternLayout.newBuilder();
        patternBuilder.withPattern("%d %p %c{1.}: %m%n");
        PatternLayout layout = patternBuilder.build();

        ConsoleAppender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout(layout);
        consoleAppender.start();

        Configuration configuration = ((LoggerContext) LogManager.getContext(false)).getConfiguration();

        LoggerConfig rootLogger = configuration.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(consoleAppender, Level.INFO, null);
    }
}
