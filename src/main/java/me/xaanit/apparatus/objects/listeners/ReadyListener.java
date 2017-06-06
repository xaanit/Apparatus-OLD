package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.internal.json.Stats;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.objects.interfaces.IListener;
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
        for (long l : GlobalVars.config.getBlacklistedServers()) {
            IGuild guild = GlobalVars.client.getGuildByID(l);
            if (guild != null) {
                Util.sendMessage(guild.getOwner().getOrCreatePMChannel(), "Your server [" + guild.getName() + " ] has been blacklisted by the developer for one reason or another. I shall be leaving it.");
                RequestBuffer.request(() -> guild.leave());
            }
        }
        RequestBuffer.request(() -> GlobalVars.client.streaming("@Apparatus prefix | " + GlobalVars.client.getGuilds().size() + " guild(s)", "https://www.twitch.tv/p/about"));
        //RequestBuffer.request(() -> GlobalVars.client.changeAvatar(Image.forUrl("png", "https://cdn.discordapp.com/attachments/245615097559515136/313983485775708160/XanXan.png")));
        initCommands();
        if (!ready) {
            save();
            initShardStats();
        }
        logger.log("Bot ready!", Level.INFO);
        ready = true;
    }

    public void initShardStats() {
        for (IShard shard : client.getShards()) {
            config.shardStats.putIfAbsent(shard.getInfo()[0], new Stats());
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
                        GlobalVars.commands.putIfAbsent(str.toLowerCase(), command);
                        String lol = str + ":" + command.getName();
                        if (!GlobalVars.commandNames.contains(lol))
                            GlobalVars.commandNames.add(lol);
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
