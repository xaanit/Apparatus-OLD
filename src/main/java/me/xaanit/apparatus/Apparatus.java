package me.xaanit.apparatus;

import com.google.gson.GsonBuilder;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.objects.interfaces.IListener;
import org.reflections.Reflections;
import sx.blah.discord.api.ClientBuilder;

public class Apparatus extends GlobalVars {
    public static void main(String[] args) {
        gson = new GsonBuilder().create();
        config = Database.loadConfig();
        Database.saveConfig();
        client = new ClientBuilder().withRecommendedShardCount().withToken(config.getToken()).build();
        initListeners();
        logger.log("Logging in...", Level.INFO);
        client.login();
        logger.log("Logged in!", Level.INFO);
    }

    private static void initListeners() {
        Reflections reflections = new Reflections("me.xaanit.apparatus.objects.listeners");
        reflections.getSubTypesOf(IListener.class).forEach(subclass -> {
            try {
                client.getDispatcher().registerListener(subclass.newInstance());
                logger.log("Registered listener " + subclass.getSimpleName(), Level.INFO);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.log("Could not register listener " + subclass.getSimpleName(), Level.CRITICAL);
            }
        });
    }
}