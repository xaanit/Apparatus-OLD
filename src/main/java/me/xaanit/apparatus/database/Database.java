package me.xaanit.apparatus.database;

import com.google.gson.Gson;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.objects.json.Guild;
import sx.blah.discord.handle.obj.IGuild;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import static me.xaanit.apparatus.GlobalVars.logger;

/**
 * Created by Jacob on 5/13/2017.
 */
public class Database {

    static {
        Gson gson = new Gson();
    }

    public static String getConfig() {
        return "";
    }

    public static boolean saveGuild(IGuild guild) {
        return true;
    }

    public static boolean saveGuild(Guild guild) {
        try {
            String path = System.getProperty("user.dir") + "\\guilds";
            new File(path).mkdirs();
            File file = new File(path + "\\" + guild.getId() + ".json");
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file);
            String json = new Gson().toJson(guild);
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
            String path = System.getProperty("user.dir") + "\\guilds";
            File file = new File(path + "\\" + guild.getStringID() + ".json");
            if (!file.exists()) {
                logger.log("Creating new Guild object for guild [ " + guild.getName() + " ] with ID [ " + guild.getStringID() + " ]", Level.INFO);
                return new Guild(guild);
            }
            logger.log("Loading guild object for guild [ " + guild.getName() + " ] with ID [ " + guild.getStringID() + " ]", Level.INFO);

            return new Gson().fromJson(new FileReader(file), Guild.class);
        } catch (Exception ex) {
            return null;
        }
    }
}
