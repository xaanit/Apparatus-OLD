package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.internal.events.BanEvent;
import me.xaanit.apparatus.internal.json.Modlog;
import me.xaanit.apparatus.internal.json.embeds.CustomEmbed;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.objects.interfaces.IListener;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

import static me.xaanit.apparatus.util.EmbedUtil.hexToColor;
import static me.xaanit.apparatus.util.MessageUtil.sendMessage;
import static me.xaanit.apparatus.util.Util.*;

/**
 * Created by Jacob on 5/27/2017.
 */
public class BanListener implements IListener {

    @EventSubscriber
    public void onUserBan(BanEvent event) {
        IUser banned = event.getUser();
        IUser banner = event.getBanner();
        IGuild guild = event.getGuild();
        IChannel bannedIn = event.getBannedIn();
        String reason = event.getBanReason();

        Modlog m = getGuild(guild).getModlog("user_ban");


        boolean useEmbed = m.isUseEmbed();
        String str = m.isUseDefault() ? getBasicString(banner, banned, bannedIn, reason) : build(m.getStringLog());
        EmbedObject em = m.isUseDefault() ? getBasicEmbed(banner, banned, bannedIn, reason) : build(m.getEmbed());
        final List<Long> channels = m.getChannels();
        List<Long> channelsToRemove = new ArrayList<>();
        for (long c : channels) {
            IChannel channel = client.getChannelByID(c);
            if (channel == null) {
                channelsToRemove.add(c);
                logger.log("removing " + c, Level.MEDIUM);
                continue;
            }
            logger.log("sending", Level.INFO);
            if (useEmbed)
                sendMessage(channel, em);
            else
                sendMessage(channel, str);
        }

        for (long l : channelsToRemove)
            m.removeChannel(l);

    }


    public static String getBasicString(IUser user, IUser u, IChannel channel, String reason) {
        return getNameAndDescrim(user) + " banned " + getNameAndDescrim(u) + " for " + reason + " from channel #" + channel.getName() + "\nTime: " + getCurrentTime();
    }

    public static EmbedObject getBasicEmbed(IUser user, IUser u, IChannel channel, String reason) {
        return new EmbedBuilder().withAuthorIcon(channel.getGuild().getIconURL()).withAuthorName("Ban!").withColor(hexToColor(CColors.ERROR)).withThumbnail(u.getAvatarURL()).appendField("User banned", getNameAndDescrim(u) + " [ " + u.getStringID() + " ] ", false).appendField("Reason", reason, false).appendField("Time", getCurrentTime(), false).withFooterIcon(user.getAvatarURL()).withFooterText(getNameAndDescrim(user) + "[ " + user.getStringID() + " ] banned this user from channel " + channel.getName()).build();
    }

    public static EmbedObject build(CustomEmbed em) {
        return null;
    }

    public static String build(String input) {
        return "";
    }
}
