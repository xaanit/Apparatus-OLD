package me.xaanit.apparatus.objects.listeners;


import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.enums.Level;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;

import java.util.ArrayList;
import java.util.List;

import static me.xaanit.apparatus.GlobalVars.guilds;
import static me.xaanit.apparatus.GlobalVars.logger;

public class DisconnectListener {

    @EventSubscriber
    public void onDisconnect(DisconnectedEvent event) {
        Database.saveConfig();
        int s = 0;
        int f = 0;
        List<Long> failedGuilds = new ArrayList<>();
        for (long key : GlobalVars.guilds.keySet()) {
            if (!Database.saveGuild(GlobalVars.guilds.get(key))) {
                f++;
                failedGuilds.add(key);
            } else {
                s++;
            }
        }
        int max = guilds.size();
        int perc = f * 100 / max;
        logger.log("Saved [ " + s + " ] guilds. Failed to save the following [ " + f + " ] guilds:\n" + failedGuilds, (perc >= 75 ? Level.CRITICAL : perc >= 50 ? Level.HIGH : perc >= 25 ? Level.HIGH : perc >= 10 ? Level.MEDIUM : perc >= 5 ? Level.LOW : Level.INFO));
    }
}
