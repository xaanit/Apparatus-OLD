package me.xaanit.apparatus.listeners;

import com.michaelwflaherty.cleverbotapi.CleverBotQuery;
import me.xaanit.apparatus.commands.customisation.Modlog;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.exceptions.PermissionsException;
import me.xaanit.apparatus.interfaces.ICommand;
import me.xaanit.apparatus.interfaces.IListener;
import me.xaanit.apparatus.util.Util;
import me.xaanit.simplelogger.SimpleLogger;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MentionEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageUpdateEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;

import static me.xaanit.apparatus.util.Util.*;


public class CommandListener implements IListener {

    private CleverBotQuery query = null;
    private String key = config.getApiKey("cleverbot").split(":::")[1];
    private SimpleLogger logger = new SimpleLogger();


    @EventSubscriber
    public void onCommand(MessageReceivedEvent event) {
        IUser user = event.getAuthor();
        IMessage message = event.getMessage();
        IChannel channel = event.getChannel();
        IGuild guild = event.getGuild();
        String content = message.getContent();

        if (content.isEmpty()) return;


        if (channel.isPrivate()) return;

        String[] args = content.split(" ");

        if (user.isBot()) return;

        if (config.getBlacklistedUsers().contains(user.getLongID())) return;

        if (hasPlaceholder(user)) return;

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
                sendMessage(channel, em.build());
                return;
            }
        }

        if (!content.startsWith(getGuild(guild).getPrefix())) return;


        try {
            String look = args[0].substring(getGuild(guild).getPrefix().length()).toLowerCase();
            if (commands.containsKey(look)) {
                config.shardStats.get(guild.getShard().getInfo()[0]).increaseCommandsExecuted();
                getGuild(guild).getStats().increaseCommandsExecuted();
                config.getStats().increaseCommandsExecuted();
                commands.get(look)
                        .runCommand(user, channel, guild, message, args, client);
            }
        } catch (PermissionsException ex) {
            config.shardStats.get(guild.getShard().getInfo()[0]).decreaseCommandsExecuted();
            getGuild(guild).getStats().decreaseCommandsExecuted();
            config.getStats().decreaseCommandsExecuted();
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


        if (user.isBot()) return;

        if (channel.isPrivate()) return;
        if (hasPlaceholder(user)) return;

        if (config.getBlacklistedUsers().contains(user.getLongID())) return;

        String[] oldArgs = content.split("\\s");
        if (!channel.getModifiedPermissions(client.getOurUser()).contains(Permissions.SEND_MESSAGES)) return;
        if (!oldArgs[0].replaceAll("<@!?305407264099926016>", "").isEmpty()) return;
        if (oldArgs.length == 1) return;
        if (config.getBlacklistedUsers().contains(user.getLongID())) return;
        String[] args = copy(oldArgs, 1);

        if (args[0].equalsIgnoreCase("prefix")) {
            sendMessage(channel, user.mention() + " | The prefix for this guild is `" + getGuild(guild).getPrefix() + "`");
            return;
        }

        try {
            if (commands.containsKey(args[0].toLowerCase())) {
                config.shardStats.get(guild.getShard().getInfo()[0]).increaseCommandsExecuted();
                getGuild(guild).getStats().increaseCommandsExecuted();
                config.getStats().increaseCommandsExecuted();
                commands.get(args[0].toLowerCase())
                        .runCommand(user, channel, guild, message, args, client);
                return;
            } else {
                if (isPatron(user)) {
                    sendMessage(channel, user.mention() + " | " + getCleverbotResponse(Util.combineArgs(args, 0, args.length)));
                }
            }
        } catch (PermissionsException ex) {
            config.shardStats.get(guild.getShard().getInfo()[0]).decreaseCommandsExecuted();
            getGuild(guild).getStats().decreaseCommandsExecuted();
            config.getStats().decreaseCommandsExecuted();
        }
    }

    @EventSubscriber
    public void onPlaceholder(MessageReceivedEvent event) {
        IUser user = event.getAuthor();
        IMessage message = event.getMessage();
        IChannel channel = event.getChannel();
        IGuild guild = event.getGuild();
        String content = message.getContent();

        if (content.isEmpty()) return;

        if (!hasPlaceholder(user)) return;

        for (ICommand command : commands.values()) {
            if (command.getPlaceInCommand(user) != null) {
                logger.critical(command.getPlaceInCommand(user).toString());
                switch (command.getName().toUpperCase()) {
                    case "MODLOG":
                        Modlog.place(user, message);
                        break;
                }
                break;
            }
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
        int calls = config.getCleverbotCalls();
        if (calls >= 4980) {
            return "Sorry! I have hit my maximum cleverbot API calls for this month! Please try again next month.";
        } else {
            calls++;
            config.setCleverbotCalls(calls);
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
