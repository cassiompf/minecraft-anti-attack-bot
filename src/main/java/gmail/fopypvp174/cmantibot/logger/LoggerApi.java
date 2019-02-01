package gmail.fopypvp174.cmantibot.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public final class LoggerApi extends AbstractFilter {

    public void registerFilter() {
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(this);
    }

    @Override
    public Result filter(LogEvent event) {
        return event == null ? Result.NEUTRAL : isLoggable(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return isLoggable(msg.getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return isLoggable(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return msg == null ? Result.NEUTRAL : isLoggable(msg.toString());
    }

    private Result isLoggable(String msg) {
        if (msg != null) {
            if (msg.contains("com.mojang.authlib.GameProfile@")) {
                return Result.DENY;
            } else if (msg.contains("lost connection: Disconnected")) {
                return Result.DENY;
            } else if (msg.contains("left the game.")) {
                return Result.DENY;
            } else if (msg.contains("logged in with entity id")) {
                return Result.DENY;
            } else if (msg.contains("lost connection: Timed out")) {
                return Result.DENY;
            } else if (msg.contains("UUID of player")) {
                return Result.DENY;
            }
        }
        return Result.NEUTRAL;
    }

}