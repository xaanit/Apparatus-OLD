package me.xaanit.apparatus.util;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.json.Guild;
import sx.blah.discord.handle.obj.IGuild;

/**
 * Created by Jacob on 5/14/2017.
 */
public class GuildUtil extends EmbedUtil {

    public static Guild getGuild(IGuild guild) {

        return GlobalVars.guilds.get(guild.getLongID());
    }
}
