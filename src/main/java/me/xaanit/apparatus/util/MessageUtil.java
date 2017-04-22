package me.xaanit.apparatus.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

/**
 * Created by Jacob on 4/21/2017.
 */
public class MessageUtil {

	/**
	 * Gets a message by the ID
	 *
	 * @param id The ID to look for
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
	 * @param str The string to send
	 * @param em The Embed to send
	 * @return The message
	 */
	public static IMessage sendMessage(IChannel channel, String str, EmbedObject em) {
		return RequestBuffer.request(() -> {
			try {
				return channel.sendMessage(str, em);
			} catch(DiscordException ex) {
				if(!channel.isPrivate()) {
					sendMessage(channel, str, em);
				}
			} catch(MissingPermissionsException e) {
				return null;
			}
			return null;
		}).get();
	}

	/**
	 * Sends a message to the specified channel
	 *
	 * @param channel The channel to send to.
	 * @param str The string to send
	 * @return The message
	 */
	public static IMessage sendMessage(IChannel channel, String str) {
		return RequestBuffer.request(() -> {
			try {
				return channel.sendMessage(str);
			} catch(DiscordException ex) {
				if(!channel.isPrivate()) {
					sendMessage(channel, str);
				}
			} catch(MissingPermissionsException e) {
				return null;
			}
			return null;
		}).get();
	}

	/**
	 * Sends a message to the specified channel
	 *
	 * @param channel The channel to send to
	 * @param em The Embed to send
	 * @return The message
	 */
	public static IMessage sendMessage(IChannel channel, EmbedObject em) {
		return RequestBuffer.request(() -> {
			try {
				return channel.sendMessage(em);
			} catch(DiscordException ex) {
				if(!channel.isPrivate()) {
					sendMessage(channel, em);
				}
			} catch(MissingPermissionsException e) {

			}
			return null;
		}).get();
	}

	/**
	 * Deletes a specified IMessage
	 *
	 * @param message The message to delete
	 * @param channel The channel it's in, to avoid infinite recursion.
	 */
	public static void deleteMessage(IMessage message, IChannel channel) {
		RequestBuffer.request(() -> {
			try {
				message.delete();
			} catch(DiscordException ex) {
				if(!channel.isPrivate()) {
					deleteMessage(message, channel);
				}
			}
		});
	}

	/**
	 * Edits a specified message
	 *
	 * @param message The message to edit.
	 * @param str The string to edit it with
	 * @param e The EmbedObject to edit it with
	 */
	public static void editMessage(IMessage message, String str, EmbedObject e) {
		RequestBuffer.request(() -> {
			try {
				message.edit(str, e);
			} catch(DiscordException ex) {
				if(UserUtil.isOurUser(message.getAuthor())) {
					editMessage(message, str, e);
				}
			}
		});
	}

	/**
	 * Edits a specified message
	 *
	 * @param message The message to edit
	 * @param str The string to edit it with
	 */
	public static void editMessage(IMessage message, String str) {
		RequestBuffer.request(() -> {
			try {
				message.edit(str);
			} catch(DiscordException ex) {
				if(UserUtil.isOurUser(message.getAuthor())) {
					editMessage(message, str);
				}
			}
		});
	}

	/**
	 * Edits a specified message
	 *
	 * @param message The message to edit
	 * @param e The EmbedObject to edit it with
	 */
	public static void editMessage(IMessage message, EmbedObject e) {
		RequestBuffer.request(() -> {
			try {
				message.edit(e);
			} catch(DiscordException ex) {
				if(UserUtil.isOurUser(message.getAuthor())) {
					editMessage(message, e);
				}
			}
		});
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
	 * Combines a list of arguments into a single string
	 *
	 * @param args The string array
	 * @param start The index to start at
	 * @return The combined String
	 */
	public static String combineArgs(String[] args, int start) {
		if(start >= args.length) {
			return "";
		}
		String res = "";
		for(int i = start; i < args.length; i++) {
			res += args[i] + " ";
		}
		return res;
	}
}
