package me.xaanit.apparatus;

import me.xaanit.apparatus.objects.enums.Level;

import java.io.IOException;

// Code adapted from Kotlin to Java
// Taken from https://github.com/austinv11/D4JBot
// All rights go to him for the idea, and most of the execution
// Classes include, Update.kt, Master.kt, and Slave.kt
public class Master extends GlobalVars {

    private static final String JAR_PATH = Master.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    private static Process slave;

    public static void main(String[] args) {
        try {
            do {
                logger.log("Creating slave... ", Level.INFO);
                slave = createSlave();
            } while (slave.waitFor() != EXIT_CODE);
        } catch (InterruptedException ex) {
            logger.log("Slave inturrupted! Shutting down...", Level.CRITICAL);
            System.exit(0);
        }
    }

    public static Process getSlave() {
        return slave;
    }

    public static String getJar() {
        return JAR_PATH;
    }

    public static Process createSlave() {
        try {
            return new ProcessBuilder("java", "-cp", JAR_PATH, "me.xaanit.apparatus.Slave").inheritIO().start();
        } catch (IOException ex) {
            logger.log("Slave creation failed....", Level.CRITICAL);
            System.exit(0);
            return null;
        }
    }
}
