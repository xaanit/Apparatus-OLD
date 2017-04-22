package me.xaanit.apparatus.util;

import java.util.List;
import java.util.stream.Collectors;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by Jacob on 4/19/2017.
 */
public class ChannelUtil {

	public static IChannel getChannel(String toLookFor, IMessage message, IGuild guild) {

		toLookFor = toLookFor.trim();
		final String lower = toLookFor.toLowerCase();
		if(!message.getChannelMentions().isEmpty()) {
			return message.getChannelMentions().get(0);
		}

		if(toLookFor.replaceAll("[0-9]", "").isEmpty()) {
			IChannel exists = guild.getChannelByID(Long.parseLong(toLookFor));
			if(exists != null) {
				return exists;
			}
		}

		List<IChannel> channels = guild.getChannels().stream().filter(
				c -> c.getName().contains(lower) || c.getName().equalsIgnoreCase(lower) || c.getStringID()
						.equals(lower)).collect(Collectors.toList());

		if(!channels.isEmpty()) {
			return channels.get(0);
		}

		return null;
	}
}
