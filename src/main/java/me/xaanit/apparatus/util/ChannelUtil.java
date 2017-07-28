package me.xaanit.apparatus.util;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jacob on 4/19/2017.
 */
public class ChannelUtil extends MarriageUtil {

    /**
     * Gets a channel from mention, or from a String
     *
     * @param toLookFor The String to look at
     * @param message   The message, incase of mention
     * @return The channel if found, otherwise null
     */
    public static IChannel getChannel(String toLookFor, IMessage message) {
        toLookFor = toLookFor.trim();
        IGuild guild = message.getGuild();
        toLookFor = toLookFor.trim();
        final String lower = toLookFor.toLowerCase();

        if (toLookFor.matches("<#[0-9]+>")) {
            IChannel exists = guild.getChannelByID(Long.parseLong(toLookFor.replaceAll("[<>#]", "")));
            if (exists != null) {
                return exists;
            }
        }

        List<IChannel> channels = new ArrayList<>();
        List<IChannel> cs = guild.getChannels();
        channels.addAll(cs.stream().filter(c -> c.getName().equalsIgnoreCase(lower)).collect(Collectors.toList()));
        channels.addAll(cs.stream().filter(c -> c.getName().toLowerCase().contains(lower)).collect(Collectors.toList()));
        if (!channels.isEmpty()) {
            return channels.get(0);
        }
        
        return null;
    }
}
