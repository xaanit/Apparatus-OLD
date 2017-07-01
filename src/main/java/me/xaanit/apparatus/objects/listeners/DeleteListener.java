package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.simplelogger.SimpleLogger;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

/**
 * Created by Jacob on 5/15/2017.
 */
public class DeleteListener implements IListener {

    private SimpleLogger logger = SimpleLogger.getLoggerByClass(IListener.class);

    @EventSubscriber
    public void onDelete(MessageDeleteEvent event) {
        IGuild guild = event.getGuild();
        IUser user = event.getAuthor();
        IChannel channel = event.getChannel();

        logger.info("User [ " + user.getName() + " ] deleted a message in guild [ " + (guild == null ? "DM" : guild.getName()) + " ] in channel [ " + (channel == null ? "DM" : channel.getName()) + " ] with content [ " + (event.getMessage().getContent() == null ? "Embed/Attachment" : event.getMessage().getContent()) + " ] ");

    }
}
