package me.xaanit.apparatus;

import com.google.gson.GsonBuilder;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.enums.Level;

import java.io.IOException;

// Code adapted from Kotlin to Java
// Taken from https://github.com/austinv11/D4JBot
// All rights go to him for the idea, and most of the execution
// Classes include, Update.kt, Master.kt, and Slave.kt
public class Master extends GlobalVars {

    private static final String JAR_PATH = Master.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    public static void main(String[] args) {
        gson = new GsonBuilder().create();
        config = Database.loadConfig();
        Database.saveConfig();
        do {
            Slave slave = create
        } while (slave.waitFor() != EXIT_CODE)
    }

    public static Slave createSlave(String token) {
        try {
            return new ProcessBuilder("java", "-cp", JAR_PATH, "com.austinv11.d4j.bot.SlaveKt").inheritIO().start();
        } catch (IOException ex) {
            logger.log("Slave creation failed....", Level.CRITICAL);
            System.exit(0);
            return null;
        }
    }
}
