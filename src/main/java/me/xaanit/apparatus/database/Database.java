package me.xaanit.apparatus.database;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.internal.json.JsonConfig;
import me.xaanit.apparatus.internal.json.JsonGuild;
import me.xaanit.simplelogger.SimpleLogger;
import sx.blah.discord.handle.obj.IGuild;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import static me.xaanit.apparatus.GlobalVars.guilds;

/**
 * Created by Jacob on 5/13/2017.
 */
public class Database {

    private static SimpleLogger logger = new SimpleLogger(Database.class);

    public static JsonConfig loadConfig() {
        try {
            File file = new File(GlobalVars.PATH + "config.json");
            if (!file.exists()) {
                file.createNewFile();
                return new JsonConfig(true);
            }
            return GlobalVars.gson.fromJson(new FileReader(file), JsonConfig.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
            return null;
        }
    }


    public static boolean saveConfig() {
        try {
            String path = GlobalVars.PATH + "config.json";
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file);
            String json = GlobalVars.gson.toJson(GlobalVars.config);
            fw.write(json);
            fw.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean saveGuild(IGuild guild) {
        return saveGuild(guilds.get(guild.getLongID()));
    }

    public static boolean saveGuild(JsonGuild guild) {
        try {
            String path = GlobalVars.PATH + "guilds";
            new File(path).mkdirs();
            File file = new File(path + "\\" + guild.getId() + ".json");
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file);
            String json = GlobalVars.gson.toJson(guild);
            fw.write(json);
            fw.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static JsonGuild loadGuild(IGuild guild) {
        try {
            String path = GlobalVars.PATH + "guilds";
            File file = new File(path + "\\" + guild.getStringID() + ".json");
            if (!file.exists()) {
                logger.info("Creating a new guild for " + guild.getName());
                return new JsonGuild(guild);
            }
            logger.info("Loading guild file for " + guild.getName());
            return GlobalVars.gson.fromJson(new FileReader(file), JsonGuild.class);
        } catch (Exception ex) {
            return null;
        }
    }

}
