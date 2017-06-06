package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.internal.events.BanEvent;
import me.xaanit.apparatus.internal.json.Modlog;
import me.xaanit.apparatus.internal.json.embeds.CustomEmbed;
import me.xaanit.apparatus.objects.enums.CColors;
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
        String str = m.isUseDefault() ? getBasicString(banner, banned, bannedIn, reason) : build(m.getStringLog(), banned, bannedIn, bannedIn.getGuild(), banner, reason);
        EmbedObject em = m.isUseDefault() ? getBasicEmbed(banner, banned, bannedIn, reason) : build(m.getEmbed(), banned, bannedIn, bannedIn.getGuild(), banner, reason);
        System.out.println("USE EMBED: " + useEmbed);
        System.out.println("STR: " + str);
        System.out.println("EMBED: " + em.toString());
        final List<Long> channels = m.getChannels();
        List<Long> channelsToRemove = new ArrayList<>();
        for (long c : channels) {
            System.out.println("C: " + c);
            IChannel channel = client.getChannelByID(c);
            if (channel == null) {
                channelsToRemove.add(c);
                continue;
            }
            System.out.println("GOT TO HERE");
            if (useEmbed)
                sendMessage(channel, em);
            else
                sendMessage(channel, str);
        }

        for (long l : channelsToRemove)
            m.removeChannel(l);

    }

    public String getBasicString(IUser user, IUser u, IChannel channel, String reason) {
        String input = "[[modname]]#[[moddescrim]] banned [[username]]#[[userdescrim]] for [[reason]] from channel #[[channelname]]\nTime: [[timestamp]]";
        System.out.println("CALLED");
        return format(input, u, channel, channel.getGuild(), user, reason);
    }

    public EmbedObject getBasicEmbed(IUser user, IUser u, IChannel channel, String reason) {
        EmbedBuilder em = new EmbedBuilder();
        em.withAuthorIcon(format("[[guildicon]]", u, channel, channel.getGuild(), user, reason));
        em.withAuthorName("Ban!");
        em.withColor(hexToColor(CColors.ERROR));
        em.withThumbnail(format("[[usericon]]", u, channel, channel.getGuild(), user, reason));
        em.appendField("User banned", format("[[username]]#[[userdescrim]] [ [[userid]] ]", u, channel, channel.getGuild(), user, reason), false);
        em.appendField("Reason", format("[[reason]]", u, channel, channel.getGuild(), user, reason), false);
        em.appendField("Time", format("[[timestamp]]", u, channel, channel.getGuild(), user, reason), false);
        em.withFooterIcon(format("[[modicon]]", u, channel, channel.getGuild(), user, reason));
        em.withFooterText(format("[[modname]]#[[moddescrim]] [ [[modid]] ] banned this user from channel #[[channelname]]", u, channel, channel.getGuild(), user, reason));
        return em.build();
    }

    public EmbedObject build(CustomEmbed em, IUser user, IChannel channel, IGuild guild, IUser banner, String reason) {
        return null;
    }

    public String build(String input, IUser user, IChannel channel, IGuild guild, IUser banner, String reason) {
        return format(input, user, channel, guild, banner, reason);
    }

    public String format(String input, IUser user, IChannel channel, IGuild guild, IUser banner, String reason) {
        return input
                .replace("[[userid]]", user.getStringID())
                .replace("[[username]]", user.getName())
                .replace("[[usermention]]", user.mention())
                .replace("[[userdescrim]]", user.getDiscriminator())
                .replace("[[guildicon]]", guild.getIconURL())
                .replace("[[usericon]]", user.getAvatarURL())
                .replace("[[guildname]]", guild.getName())
                .replace("[[guildid]]", guild.getStringID())
                .replace("[[botname]]", client.getOurUser().getName())
                .replace("[[botdescrim]]", client.getOurUser().getDiscriminator())
                .replace("[[boticon]]", botAva())
                .replace("[[channelname]]", channel.getName())
                .replace("[[channelmention]]", channel.mention())
                .replace("[[usercount]]", guild.getTotalMemberCount() + "")
                .replace("[[timestamp]]", getCurrentTime())
                .replace("[[reason]]", reason)
                .replace("[[modid]]", banner.getStringID())
                .replace("[[modname]]",banner.getName())
                .replace("[[moddescrim]]", banner.getDiscriminator())
                .replace("[[modmention]]", banner.mention())
                .replace("[[modicon]]", banner.getAvatarURL());
    }
}
