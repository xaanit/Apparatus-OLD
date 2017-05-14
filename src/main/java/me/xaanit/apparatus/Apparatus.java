package me.xaanit.apparatus;

import com.google.gson.GsonBuilder;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.apparatus.objects.json.Config;
import org.reflections.Reflections;
import sx.blah.discord.api.ClientBuilder;

import static me.xaanit.apparatus.GlobalVars.logger;

/**
 * Created by Jacob on 4/21/2017.
 */
public class Apparatus {

    public static void main(String[] args) {
        GlobalVars.gson = new GsonBuilder().create();
        GlobalVars.client = new ClientBuilder().withRecommendedShardCount()
                .withToken(loadConfig() == null ? "MzA1NDA3MjY0MDk5OTI2MDE2.C90wMA.6Wh-YYyiKrPHY2oBAYVQm9yu4nw" : GlobalVars.config.getToken()).build();
        initListeners();
        logger.log("Logging in...", Level.INFO);
        GlobalVars.client.login();
        logger.log("Logged in!", Level.INFO);

    }

    private static void initListeners() {
        Reflections reflections = new Reflections("me.xaanit.apparatus.objects.listeners");
        reflections.getSubTypesOf(IListener.class).forEach(subclass -> {
            try {
                GlobalVars.client.getDispatcher().registerListener(subclass.newInstance());
                // Some logging here that it was registered
            } catch (InstantiationException | IllegalAccessException e) {
                // Some logging here that it failed
            }
        });
    }

    public static Config loadConfig() {
        return GlobalVars.gson.fromJson(Database.getConfig().isEmpty() ? null : Database.getConfig(), Config.class);
    }

}
