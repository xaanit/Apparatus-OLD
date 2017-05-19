package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.exceptions.PermissionsException;
import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MentionEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import static me.xaanit.apparatus.GlobalVars.commands;

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


        if (GlobalVars.config.getBlacklistedUsers().contains(user.getLongID())) {
            return;
        }

        if (content.isEmpty()) {
            return;
        }
        if (!content.startsWith(Util.getGuild(guild).getPrefix())) {
            return;
        }
        String[] args = content.split(" ");
        try {
            String look = args[0].substring(Util.getGuild(guild).getPrefix().length()).toLowerCase();
            if (commands.containsKey(look))
                commands.get(look)
                        .runCommand(user, channel, guild, message, args);
        } catch (PermissionsException ex) {

        }
    }

    @EventSubscriber
    public void onCommand(MentionEvent event) {
        event.getClient();
        IUser user = event.getAuthor();
        IMessage message = event.getMessage();
        IChannel channel = event.getChannel();
        IGuild guild = event.getGuild();
        String content = message.getContent();


        String[] oldArgs = content.split("\\s");
        if (!oldArgs[0].replaceAll("<@(!)?305407264099926016>", "").isEmpty()) return;
        if (oldArgs.length == 1) return;
        String[] args = copy(oldArgs, 1);

        if (args[0].equalsIgnoreCase("prefix")) {
            Util.sendMessage(channel, user.mention() + " | The prefix for this guild is `" + Util.getGuild(guild).getPrefix() + "`");
        }

        try {
            if (GlobalVars.commands.containsKey(args[0].toLowerCase()))
                GlobalVars.commands.get(args[0].toLowerCase())
                        .runCommand(user, channel, guild, message, args);
        } catch (PermissionsException ex) {

        }
    }

    public String[] copy(String[] args, int start) {
        String[] arr = new String[args.length - start];
        int j = 0;
        for (int i = start; i < args.length; i++) {
            arr[j] = args[i];
            j++;
        }
        return arr;
    }
}
