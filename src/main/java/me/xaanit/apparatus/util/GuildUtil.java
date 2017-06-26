package me.xaanit.apparatus.util;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.internal.json.JsonGuild;
import sx.blah.discord.handle.obj.IGuild;

/**
 * Created by Jacob on 5/14/2017.
 */
public class GuildUtil extends EmbedUtil {

    public static JsonGuild getGuild(IGuild guild) {
        if (!GlobalVars.guilds.containsKey(guild.getLongID())) {
            JsonGuild g = Database.loadGuild(guild);
            g.updateCommands();
            GlobalVars.guilds.put(guild.getLongID(), g);
        }
        return GlobalVars.guilds.get(guild.getLongID());
    }
}
