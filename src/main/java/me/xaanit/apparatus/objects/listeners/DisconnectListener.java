package me.xaanit.apparatus.objects.listeners;


import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.simplelogger.SimpleLogger;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;

import java.util.ArrayList;
import java.util.List;

import static me.xaanit.apparatus.GlobalVars.guilds;

public class DisconnectListener {

    private SimpleLogger logger = SimpleLogger.getLoggerByClass(IListener.class);

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
        logger.info("Saved [ " + s + " ] guilds. Failed to save the following [ " + f + " ] guilds:\n" + failedGuilds);
    }
}
