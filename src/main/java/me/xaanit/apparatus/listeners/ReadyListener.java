package me.xaanit.apparatus.listeners;

import me.xaanit.apparatus.Apparatus;
import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.commands.music.Music;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.interfaces.IListener;
import me.xaanit.apparatus.internal.json.JsonGuild;
import me.xaanit.apparatus.internal.json.JsonStats;
import me.xaanit.apparatus.music.GuildMusicManager;
import me.xaanit.apparatus.util.Util;
import me.xaanit.simplelogger.SimpleLogger;
import sx.blah.discord.api.IShard;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static me.xaanit.apparatus.GlobalVars.*;

/**
 * Created by Jacob on 4/21/2017.
 */
public class ReadyListener implements IListener {

    public static boolean ready = false;
    static SimpleLogger logger = SimpleLogger.getLoggerByClass(Apparatus.class);

    @Override
    public boolean isTemp() {
        return true;
    }

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        logger.info("Ready event start...");
        for (long l : config.getBlacklistedServers()) {
            IGuild guild = client.getGuildByID(l);
            if (guild != null) {
                Util.sendMessage(guild.getOwner().getOrCreatePMChannel(), "Your server [" + guild.getName() + " ] has been blacklisted by the developer for one reason or another. I shall be leaving it.");
                RequestBuffer.request(() -> guild.leave());
            }
        }
        for(IShard shard : event.getClient().getShards()) {
            RequestBuffer.request(() -> shard.online("@Apparatus prefix | " + shard.getGuilds().size() + " guild(s)"));
        }
        if (!ready) {
            save();
            initShardStats();
        }
        initGuilds();
        initMusicManagers();
        logger.info("Bot ready!");
        ready = true;

    }

    private void initGuilds() {
            for (IGuild guild : client.getGuilds().toArray(new IGuild[client.getGuilds().size()])) {
                if (!GlobalVars.guilds.containsKey(guild.getLongID())) {
                    JsonGuild g = Database.loadGuild(guild);
                    g.updateCommands();
                    GuildCreateListener.MOD_LOGS.forEach(m -> g.addModlog(m));
                    GlobalVars.guilds.putIfAbsent(guild.getLongID(), g);
            }
        }
    }

    public static void initMusicManagers() {
        SimpleLogger logger = SimpleLogger.getLoggerByClass(Music.class);
        for (JsonGuild g : guilds.values()) {
            if (g.whitelistedGuild) {
                if (!Music.managers.containsKey(g.getId())) {
                    Music.managers.putIfAbsent(g.getId(), new GuildMusicManager(Music.manager));
                    logger.info("Making music manager for guild [ " + client.getGuildByID(g.getId()).getName() + " ]");
                }
            }
        }
    }

    public void initShardStats() {
        for (IShard shard : client.getShards()) {
            config.shardStats.putIfAbsent(shard.getInfo()[0], new JsonStats());
        }
    }

    public void save() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        final Runnable actualTask = new Runnable() {
            @Override
            public void run() {
                Database.saveConfig();
                int s = 0;
                int f = 0;
                List<Long> failedGuilds = new ArrayList<>();
                for (long key : guilds.keySet()) {
                    if (!Database.saveGuild(guilds.get(key))) {
                        f++;
                        failedGuilds.add(key);
                    } else {
                        s++;
                    }
                }
                int max = guilds.size();
                int perc = f * 100 / max;
                SimpleLogger.getLoggerByClass(Database.class).debug("Saved [ " + s + " ] guilds. Failed to save the following [ " + f + " ] guilds:\n" + failedGuilds);
            }
        };

        executorService.scheduleAtFixedRate(new Runnable() {
            private final ExecutorService executor = Executors.newSingleThreadExecutor();
            private Future<?> lastExecution;

            @Override
            public void run() {
                if (lastExecution != null && !lastExecution.isDone()) {
                    return;
                }
                SimpleLogger.getLoggerByClass(Database.class).debug("Saving config..");
                lastExecution = executor.submit(actualTask);
            }
        }, 10, 10, TimeUnit.MINUTES);
    }


}
