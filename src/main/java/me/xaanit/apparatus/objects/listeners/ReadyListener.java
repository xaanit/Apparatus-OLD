package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.internal.json.JsonGuild;
import me.xaanit.apparatus.internal.json.JsonStats;
import me.xaanit.apparatus.objects.commands.music.MusicVariables;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.apparatus.objects.music.GuildMusicManager;
import me.xaanit.apparatus.util.Util;
import org.reflections.Reflections;
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

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        logger.log("Ready event start...", Level.INFO);
        for (long l : config.getBlacklistedServers()) {
            IGuild guild = client.getGuildByID(l);
            if (guild != null) {
                Util.sendMessage(guild.getOwner().getOrCreatePMChannel(), "Your server [" + guild.getName() + " ] has been blacklisted by the developer for one reason or another. I shall be leaving it.");
                RequestBuffer.request(() -> guild.leave());
            }
        }
        RequestBuffer.request(() -> client.streaming("@Apparatus prefix | " + client.getGuilds().size() + " guild(s)", "https://www.twitch.tv/p/about"));

        initCommands();
        if (!ready) {
            save();
            initShardStats();
        }
        initMusicManagers();
        logger.log("Bot ready!", Level.INFO);
        ready = true;
    }

    public static void initMusicManagers() {

        for (JsonGuild g : guilds.values()) {
            if (g.whitelistedGuild) {
                if (!MusicVariables.managers.containsKey(g.getId())) {
                    MusicVariables.managers.putIfAbsent(g.getId(), new GuildMusicManager(MusicVariables.manager));
                    logger.log("Making music manager for guild [ " + client.getGuildByID(g.getId()).getName() + " ]", Level.INFO);
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
                logger.log("Saved [ " + s + " ] guilds. Failed to save the following [ " + f + " ] guilds:\n" + failedGuilds, (perc >= 75 ? Level.CRITICAL : perc >= 50 ? Level.HIGH : perc >= 25 ? Level.HIGH : perc >= 10 ? Level.MEDIUM : perc >= 5 ? Level.LOW : Level.INFO));
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
                logger.log("Saving config..", Level.INFO);
                lastExecution = executor.submit(actualTask);
            }
        }, 10, 10, TimeUnit.MINUTES);
    }

    private void initCommands() {
        logger.log("Initialising commands....", Level.INFO);
        Reflections reflections = new Reflections("me.xaanit.apparatus.objects.commands");
        reflections.getSubTypesOf(ICommand.class).forEach(subclass -> {
            try {
                ICommand command = subclass.newInstance();
                if (command.getAliases().length != 0) {
                    for (String str : command.getAliases()) {
                        commands.putIfAbsent(str.toLowerCase(), command);
                        String lol = str + ":" + command.getName();
                        if (!commandNames.contains(lol))
                            commandNames.add(lol);
                        logger.log("Loaded command \"" + command.getName() + "\" with alias \"" + str + "\"", Level.INFO);
                    }
                }
            } catch (InstantiationException | IllegalAccessException e) {
                logger.log(subclass.getName() + " failed to load!", Level.CRITICAL);
                logger.log("[" + e.getMessage() + "]", Level.CRITICAL);

            }
        });
        logger.log("Commands initialised!", Level.INFO);
    }

}
