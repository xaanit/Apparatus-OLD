package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.exceptions.PermissionsException;
import me.xaanit.apparatus.objects.interfaces.IListener;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

/**
 * Created by Jacob on 5/14/2017.
 */
public class CommandListener implements IListener {

    @EventSubscriber
    public void onCommand(MessageReceivedEvent event) {
        IUser user = event.getAuthor();
        IMessage message = event.getMessage();
        IChannel channel = event.getChannel();
        IGuild guild = event.getGuild();
        String content = message.getContent();

        if (content.isEmpty()) {
            return;
        }
        if (!content.startsWith("+")) {
            return;
        }
        String[] args = content.split(" ");
        try {
            if (GlobalVars.commands.containsKey(args[0].substring(1).toLowerCase()))
                GlobalVars.commands.get(args[0].substring(1).toLowerCase())
                        .runCommand(user, channel, guild, message, args);
        } catch (PermissionsException ex) {

        }
    }
}
