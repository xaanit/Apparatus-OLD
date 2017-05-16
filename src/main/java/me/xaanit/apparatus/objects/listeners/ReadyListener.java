package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.apparatus.util.Util;
import org.reflections.Reflections;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.RequestBuffer;

import static me.xaanit.apparatus.GlobalVars.logger;

/**
 * Created by Jacob on 4/21/2017.
 */
public class ReadyListener implements IListener {

    public static boolean ready = false;

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        ready = true;
        GlobalVars.sponseredGuild = GlobalVars.client.getGuildByID(283076860936454144L);
        logger.log("Ready event start...", Level.INFO);
        for (long l : GlobalVars.config.getBlacklistedServers()) {
            IGuild guild = GlobalVars.client.getGuildByID(l);
            if (guild != null) {
                Util.sendMessage(guild.getOwner().getOrCreatePMChannel(), "Your server [" + guild.getName() + " ] has been blacklisted by the developer for one reason or another. I shall be leaving it.");
                RequestBuffer.request(() -> guild.leave());
            }
        }
        initCommands();
        logger.log("Bot ready!", Level.INFO);
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
                        logger.log("Logged command \"" + command.getName() + "\" with alias \"" + str + "\"", Level.INFO);
                    }
                }
            } catch (InstantiationException | IllegalAccessException e) {
                // Some logging here about how it couldn't register
            }
        });
        logger.log("Commands initialised!", Level.INFO);
    }

}
