package me.xaanit.apparatus.util;

import me.xaanit.apparatus.GlobalVars;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jacob on 4/21/2017.
 */
@SuppressWarnings("unused")
public class MessageUtil extends GuildUtil {

    /**
     * Gets a message by the ID
     *
     * @param id    The ID to look for
     * @param guild The guild to look in
     * @return The message if it exists, null otherwise
     */
    public static IMessage getMessageByID(String id, IGuild guild) {
        return guild.getMessageByID(Long.parseLong(id));
    }

    /**
     * Sends a message to the specified channel
     *
     * @param channel The channel to send to
     * @param str     The {@link String} to send
     * @param em      The {@link EmbedObject} to send
     * @return The message
     */
    private static IMessage sendMessage(IChannel channel, String str, EmbedObject em) {
        return RequestBuffer.request(() -> {
            try {
                return channel.sendMessage(str, em);
            } catch (DiscordException ex) {
                if (!channel.isPrivate()) {
                    sendMessage(channel, str, em);
                } else {
                    ex.printStackTrace();
                }
            }
            return null;
        }).get();
    }

    /**
     * Sends a message to the specified channel
     *
     * @param channel The channel to send to.
     * @param str     The string to send
     * @return The message
     */
    static IMessage sendMessage(IChannel channel, String str) {
        return sendMessage(channel, str, null);
    }

    /**
     * Sends a message to the specified channel
     *
     * @param channel The channel to send to
     * @param em      The Embed to send
     * @return The message
     */
    public static IMessage sendMessage(IChannel channel, EmbedObject em) {
        return sendMessage(channel, null, em);
    }

    /**
     * Deletes a specified IMessage
     *
     * @param message The message to delete
     * @param channel The channel it's in, to avoid infinite recursion.
     */
    private static void deleteMessage(IMessage message, IChannel channel) {
        RequestBuffer.request(() -> {
            try {
                message.delete();
            } catch (DiscordException ex) {
                if (!channel.isPrivate()) {
                    deleteMessage(message, channel);
                } else {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Edits a specified message
     *
     * @param message The message to edit.
     * @param str     The string to edit it with
     * @param e       The EmbedObject to edit it with
     * @reutn The IMessage, for chaining.
     */

    private static IMessage editMessage(IMessage message, String str, EmbedObject e) {
        return RequestBuffer.request(() -> {
            try {
                return message.edit(str, e);
            } catch (DiscordException ex) {
                if (message.getAuthor().getLongID() == GlobalVars.client.getOurUser().getLongID()) {
                    editMessage(message, str, e);
                } else {
                    ex.printStackTrace();
                }
            }
            return null;
        }).get();
    }

    /**
     * Edits a specified message
     *
     * @param message The message to edit
     * @param str     The string to edit it with
     * @return The IMessage. For chaining
     */
    public static IMessage editMessage(IMessage message, String str) {
        return editMessage(message, str, null);
    }

    /**
     * Edits a specified message
     *
     * @param message The message to edit
     * @param e       The EmbedObject to edit it with
     * @return The IMessage, for chaining.
     */
    public static IMessage editMessage(IMessage message, EmbedObject e) {
        return editMessage(message, null, e);
    }

    /**
     * Deletes a specified command sent by the bot after a certain number of seconds
     *
     * @param command The command to delete
     * @param seconds The seconds to wait
     */
    public static void deleteCommand(IMessage command, int seconds) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> deleteMessage(command, command.getChannel());
        executor.schedule(task, seconds, TimeUnit.SECONDS);
        executor.shutdown();
    }

    /**
     * Combines the arguments of a String array
     *
     * @param args  The string arary
     * @param start The start index
     * @param end   The end index (args.length if end == -1)
     * @return The combined String (Empty if start > args.length)
     */
    public static String combineArgs(String[] args, int start, int end) {
        if (end == -1)
            end = args.length;
        if (start >= args.length) {
            return "";
        }
        String res = "";
        for (int i = start; i < end; i++) {
            res += args[i] + " ";
        }
        return res;
    }
}
