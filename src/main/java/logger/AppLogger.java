package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Represents central logger for the entire application.
 * All commands, exceptions, and parser logs will be written here.
 */
public class AppLogger {
    private static final Logger logger = Logger.getLogger("FinanceAppLogger");
    private static final String LOG_FILE = "TrackStars.log";

    static {
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE, false);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);

        } catch (IOException e) {
            Logger fallbackLogger = Logger.getLogger("FallbackLogger");
            fallbackLogger.log(Level.SEVERE, "Failed to initialize file logging", e);
        }
    }

    private AppLogger() {
    }

    public static Logger getLogger() {
        return logger;
    }
}
