package me.xaanit.apparatus.database;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.internal.json.Config;
import me.xaanit.apparatus.internal.json.Guild;
import me.xaanit.apparatus.internal.json.User;
import sx.blah.discord.handle.obj.IGuild;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import static me.xaanit.apparatus.GlobalVars.logger;

/**
 * Created by Jacob on 5/13/2017.
 */
public class Database {

    public static Config loadConfig() {
        try {
            File file = new File(GlobalVars.PATH + "config.json");
            System.out.println(file.getAbsolutePath());
            if (!file.exists()) {
                file.createNewFile();
                return new Config(true);
            }
            return GlobalVars.gson.fromJson(new FileReader(file), Config.class);
        } catch (Exception ex) {
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
        return true;
    }

    public static boolean saveGuild(Guild guild) {
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

    public static Guild loadGuild(IGuild guild) {
        try {
            String path = GlobalVars.PATH + "guilds";
            File file = new File(path + "\\" + guild.getStringID() + ".json");
            if (!file.exists()) {
                logger.log("Creating new Guild object for guild [ " + guild.getName() + " ] with ID [ " + guild.getStringID() + " ]", Level.INFO);
                return new Guild(guild);
            }
            logger.log("Loading guild object for guild [ " + guild.getName() + " ] with ID [ " + guild.getStringID() + " ]", Level.INFO);

            return GlobalVars.gson.fromJson(new FileReader(file), Guild.class);
        } catch (Exception ex) {
            return null;
        }
    }

    public static boolean saveUser(User user) {
        try {
            String path = GlobalVars.PATH + "users";
            new File(path).mkdirs();
            File file = new File(path + "\\" + user.getId() + ".json");
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file);
            String json = GlobalVars.gson.toJson(user);
            fw.write(json);
            fw.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static User loadUser(long id) {
        try {
            String path = GlobalVars.PATH + "users";
            File file = new File(path + "\\" + id + ".json");
            User user = GlobalVars.gson.fromJson(new FileReader(file), User.class);
            logger.log("Loading user object: " + user.toString(), Level.INFO);
            return user;
        } catch (Exception ex) {
            return null;
        }
    }
}
