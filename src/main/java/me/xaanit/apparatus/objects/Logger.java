package me.xaanit.apparatus.objects;

import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.util.Util;

public class Logger {


    public Logger() {
    }

    public void log(CharSequence str, Level level) {
        System.err.println("[" + Util.getCurrentTime() + "] " + level.toString() + ": " + str);
    }
}