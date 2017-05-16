package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.objects.interfaces.IListener;
import org.reflections.Reflections;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;

import static me.xaanit.apparatus.GlobalVars.logger;

/**
 * Created by Jacob on 4/21/2017.
 */
public class ReadyListener implements IListener {

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        GlobalVars.sponseredGuild = GlobalVars.client.getGuildByID(283076860936454144L);
        logger.log("Ready event start...", Level.INFO);
        initCommands();
        logger.log("Guilds loaded.", Level.INFO);
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
