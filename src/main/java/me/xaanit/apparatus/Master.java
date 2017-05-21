package me.xaanit.apparatus;

import me.xaanit.apparatus.objects.enums.Level;

import java.io.IOException;

// Code adapted from Kotlin to Java
// Taken from https://github.com/austinv11/D4JBot
// All rights go to him for the idea, and most of the execution
// Classes include, Update.kt, Master.kt, and Slave.kt
public class Master extends GlobalVars {

    private static final String JAR_PATH = Master.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    public static void main(String[] args) {
        try {
            Process slave;
            do {
                slave = createSlave();
            } while (slave.waitFor() != EXIT_CODE);
        } catch (InterruptedException ex) {
            logger.log("Slave inturrupted! Shutting down...", Level.CRITICAL);
            System.exit(0);
        }
    }

    public static Process createSlave() {
        try {
            return new ProcessBuilder("java", "-cp", JAR_PATH, "com.xaanit.apparatus.SlaveJava").inheritIO().start();
        } catch (IOException ex) {
            logger.log("Slave creation failed....", Level.CRITICAL);
            System.exit(0);
            return null;
        }
    }
}
