package me.xaanit.apparatus.objects.listeners;

import com.michaelwflaherty.cleverbotapi.CleverBotQuery;
import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.exceptions.PermissionsException;
import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MentionEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageUpdateEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;

import static me.xaanit.apparatus.GlobalVars.client;
import static me.xaanit.apparatus.GlobalVars.commands;


public class CommandListener implements IListener {

    private CleverBotQuery query = null;
    private String key = GlobalVars.config.getApiKey("cleverbot").split(":::")[1];


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
        String[] args = content.split(" ");

        if (user.isBot())
            return;


        if (GlobalVars.config.getBlacklistedUsers().contains(user.getLongID())) {
            return;
        }
        if (content.startsWith("❌")) {
            if (!Util.isDev(user))
                return;
            if (args.length != 1) {
                IMessage m = null;
                try {
                    m = client.getMessageByID(Long.parseUnsignedLong(args[1]));
                } catch (NumberFormatException ex) {
                    return;
                }

                if (m == null)
                    return;
                Util.deleteMessage(m);
                EmbedBuilder em = Util.basicEmbed(user, "Message Deletion", CColors.BASIC);
                em.withDesc("Deleted message by Apparatus with ID " + args[1]);
                Util.sendMessage(channel, em.build());
                return;
            }
        }

        if (!content.startsWith(Util.getGuild(guild).getPrefix())) {
            return;
        }
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


        if (user.isBot())
            return;

        String[] oldArgs = content.split("\\s");
        if (!channel.getModifiedPermissions(GlobalVars.client.getOurUser()).contains(Permissions.SEND_MESSAGES)) return;
        if (!oldArgs[0].replaceAll("<@(!)?305407264099926016>", "").isEmpty()) return;
        if (oldArgs.length == 1) return;
        if (GlobalVars.config.getBlacklistedUsers().contains(user.getLongID())) return;
        String[] args = copy(oldArgs, 1);

        if (args[0].equalsIgnoreCase("prefix")) {
            Util.sendMessage(channel, user.mention() + " | The prefix for this guild is `" + Util.getGuild(guild).getPrefix() + "`");
            return;
        }

        try {
            if (GlobalVars.commands.containsKey(args[0].toLowerCase()))
                GlobalVars.commands.get(args[0].toLowerCase())
                        .runCommand(user, channel, guild, message, args);
            else {
                Util.sendMessage(channel, user.mention() + " | " + getCleverbotResponse(Util.combineArgs(args, 0, args.length)));
            }
        } catch (PermissionsException ex) {

        }
    }

    @EventSubscriber
    public void onCommand(MessageUpdateEvent event) {
        if (!Util.isDev(event.getAuthor()))
            return;

        event.getClient().getDispatcher().dispatch(new MessageReceivedEvent(event.getNewMessage()));
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


    public String getCleverbotResponse(String str) {
        int calls = GlobalVars.config.getCleverbotCalls();
        if (calls >= 4980) {
            return "Sorry! I have hit my maximum cleverbot API calls for this month! Please try again next month.";
        } else {
            calls++;
            GlobalVars.config.setCleverbotCalls(calls);
            Database.saveConfig();
        }
        query = new CleverBotQuery(key, str);
        try {
            query.sendRequest();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Sorry! I couldn't get a response from cleverbot.";
        }
        return query.getResponse();
    }
}
