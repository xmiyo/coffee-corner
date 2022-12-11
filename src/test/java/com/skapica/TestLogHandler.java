package com.skapica;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

class TestLogHandler extends Handler {
    private Level lastLevel = Level.FINEST;
    private String lastMessage;

    public Level getLevel() {
        return lastLevel;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void publish(LogRecord record) {
        lastLevel = record.getLevel();
        lastMessage = record.getMessage();
    }

    public void close(){}
    public void flush(){}
}