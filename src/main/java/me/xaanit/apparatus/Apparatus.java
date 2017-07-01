package me.xaanit.apparatus;

import com.google.gson.GsonBuilder;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.commands.music.Music;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.simplelogger.SimpleLogger;
import org.reflections.Reflections;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;

public class Apparatus extends GlobalVars {
    public static void main(String[] args) {
        new SimpleLogger(IListener.class);
        new SimpleLogger(ICommand.class);
        new SimpleLogger(Apparatus.class);
        new SimpleLogger(Music.class);
        ((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Discord4J.Discord4JLogger.Level.NONE);
        gson = new GsonBuilder().create();
        config = Database.loadConfig();
        Database.saveConfig();
        client = new ClientBuilder().withRecommendedShardCount().withToken(config.getToken()).build();
        initListeners();
        logger.info("Logging in...");
        client.login();
        logger.info("Logged in!");
    }

    private static void initListeners() {
        SimpleLogger logger = SimpleLogger.getLoggerByClass(IListener.class);
        logger.info("Initialising listeners...");
        Reflections reflections = new Reflections("me.xaanit.apparatus.objects.listeners");
        reflections.getSubTypesOf(IListener.class).forEach(subclass -> {
            try {
                client.getDispatcher().registerListener(subclass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                logger.critical("Could not register listener " + subclass.getSimpleName());
            }
        });
        logger.info("Listeners initialised!");
    }
}