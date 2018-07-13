package net.sarangnamu.libcore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TimeLoger {
    private static final Logger mLog = LoggerFactory.getLogger(TimeLoger.class);

    public static TimeLog start(final String message) {
        return new TimeLog(message);
    }

    public static final class TimeLog {
        private long startTime;
        private final String message;

        TimeLog(final String name) {
            message = name;
            start();
        }

        public void start() {
            startTime = System.currentTimeMillis();

            if (mLog.isDebugEnabled()) {
                mLog.info("[TIMELOG] " + message + " START");
            }
        }

        public void end() {
            if (mLog.isDebugEnabled()) {
                mLog.info("[TIMELOG] " + message + " END (" + (System.currentTimeMillis() - startTime) + " ms)");
            }
        }
    }
}
