package me.xaanit.apparatus;

import me.xaanit.apparatus.commands.music.Music;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.interfaces.ICommand;
import me.xaanit.apparatus.interfaces.IListener;
import me.xaanit.apparatus.listeners.ReadyListener;
import com.google.gson.GsonBuilder;
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
        ((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Discord4J.Discord4JLogger.Level.ERROR);
        gson = new GsonBuilder().create();

        config = Database.loadConfig();
        Database.saveConfig();
        client = new ClientBuilder().withRecommendedShardCount().withToken(config.getToken()).build();
        initListeners();
        initCommands();
        logger.info("Logging in...");
        client.login();
        logger.info("Logged in!");
    }

    private static void initCommands() {
        SimpleLogger logger = SimpleLogger.getLoggerByClass(ICommand.class);
        logger.info("Initialising commands....");
        Reflections reflections = new Reflections("me.xaanit.apparatus.commands");
        reflections.getSubTypesOf(ICommand.class).forEach(subclass -> {
            try {
                ICommand command = subclass.newInstance();
                if (command.getAliases().length != 0) {
                    for (String str : command.getAliases()) {
                        commands.putIfAbsent(str.toLowerCase(), command);
                        String lol = str + ":" + command.getName();
                    }
                }
            } catch (InstantiationException | IllegalAccessException e) {
                logger.critical(subclass.getName() + " failed to load!");
                logger.critical("[" + e.getMessage() + "]");

            }
        });
        logger.info("Commands initialised!");
    }

    private static void initListeners() {
        SimpleLogger logger = SimpleLogger.getLoggerByClass(IListener.class);
        logger.info("Initialising listeners...");
        Reflections reflections = new Reflections(ReadyListener.class.getPackage().getName());
        reflections.getSubTypesOf(IListener.class).forEach(subclass -> {
            try {
                IListener instance = subclass.newInstance();
                if (!instance.isTemp())
                    client.getDispatcher().registerListener(instance);
                else
                    client.getDispatcher().registerTemporaryListener(instance);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.critical("Could not register listener " + subclass.getSimpleName());
            }
        });
        logger.info("Listeners initialised!");
    }
}