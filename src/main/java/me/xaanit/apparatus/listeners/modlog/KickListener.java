package me.xaanit.apparatus.listeners.modlog;

import me.xaanit.apparatus.internal.events.KickEvent;
import me.xaanit.apparatus.internal.json.JsonModlog;
import me.xaanit.apparatus.internal.json.embeds.CustomEmbed;
import me.xaanit.apparatus.internal.json.embeds.Field;
import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.interfaces.IListener;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

import static me.xaanit.apparatus.util.Util.*;


public class KickListener implements IListener {

    @EventSubscriber
    public void onUserBan(KickEvent event) {
        IUser banned = event.getUser();
        IUser banner = event.getKicker();
        IGuild guild = event.getGuild();
        IChannel bannedIn = event.getKickedFrom();
        String reason = event.getReason();

        JsonModlog m = getGuild(guild).getModlog("user_kick");

        boolean useEmbed = m.isUseEmbed();
        String str = m.isUseDefault() ? getBasicString(banner, banned, bannedIn, reason) : build(m.getStringLog(), banned, bannedIn, bannedIn.getGuild(), banner, reason);
        EmbedObject em = m.isUseDefault() ? getBasicEmbed(banner, banned, bannedIn, reason) : build(m.getEmbed(), banned, bannedIn, bannedIn.getGuild(), banner, reason);
        final List<Long> channels = m.getChannels();
        List<Long> channelsToRemove = new ArrayList<>();
        for (long c : channels) {
            IChannel channel = client.getChannelByID(c);
            if (channel == null) {
                channelsToRemove.add(c);
                continue;
            }
            if (useEmbed)
                sendMessage(channel, em);
            else
                sendMessage(channel, str);
        }

        for (long l : channelsToRemove)
            m.removeChannel(l);

    }

    public String getBasicString(IUser user, IUser u, IChannel channel, String reason) {
        String input = "[[modname]]#[[moddescrim]] kicked [[username]]#[[userdescrim]] for reason ```fix\n[[reason]]``` from channel #[[channelname]]\nTime: [[timestamp]]";
        return format(input, u, channel, channel.getGuild(), user, reason);
    }

    public EmbedObject getBasicEmbed(IUser user, IUser u, IChannel channel, String reason) {
        EmbedBuilder em = new EmbedBuilder();
        em.withAuthorIcon(format("[[guildicon]]", u, channel, channel.getGuild(), user, reason));
        em.withAuthorName("Ban!");
        em.withColor(hexToColor(CColors.ERROR));
        em.withThumbnail(format("[[usericon]]", u, channel, channel.getGuild(), user, reason));
        em.appendField("User Kicked", format("[[username]]#[[userdescrim]] [ [[userid]] ]", u, channel, channel.getGuild(), user, reason), false);
        em.appendField("Reason", format("[[reason]]", u, channel, channel.getGuild(), user, reason), false);
        em.appendField("Time", format("[[timestamp]]", u, channel, channel.getGuild(), user, reason), false);
        em.withFooterIcon(format("[[modicon]]", u, channel, channel.getGuild(), user, reason));
        em.withFooterText(format("[[modname]]#[[moddescrim]] [ [[modid]] ] kicked this user from channel #[[channelname]]", u, channel, channel.getGuild(), user, reason));
        return em.build();
    }

    public EmbedObject build(CustomEmbed c, IUser user, IChannel channel, IGuild guild, IUser banner, String reason) {
        EmbedBuilder em = new EmbedBuilder();
        if (!c.getColorHex().isEmpty())
            em.withColor(hexToColor(c.getColorHex()));
        if (!c.getAuthorIcon().isEmpty())
            em.withAuthorIcon(format(c.getAuthorIcon(), user, channel, guild, banner, reason));
        if (!c.getAuthorName().isEmpty())
            em.withAuthorName(format(c.getAuthorName(), user, channel, guild, banner, reason));
        if (!c.getAuthorURL().isEmpty())
            em.withAuthorUrl(format(c.getAuthorURL(), user, channel, guild, banner, reason));
        if (!c.getThumbnail().isEmpty())
            em.withThumbnail(format(c.getThumbnail(), user, channel, guild, banner, reason));
        if (!c.getTitle().isEmpty())
            em.withTitle(format(c.getTitle(), user, channel, guild, banner, reason));
        if (!c.getTitleURL().isEmpty())
            em.withUrl(format(c.getTitleURL(), user, channel, guild, banner, reason));
        if (!c.getDesc().isEmpty())
            em.withDesc(format(c.getDesc(), user, channel, guild, banner, reason));
        for (Field f : c.getFields())
            em.appendField(format(f.getFieldTitle(), user, channel, guild, banner, reason), format(f.getFieldValue(), user, channel, guild, banner, reason), f.isInline());
        if (!c.getImage().isEmpty())
            em.withImage(format(c.getImage(), user, channel, guild, banner, reason));
        if (!c.getFooterIcon().isEmpty())
            em.withFooterIcon(format(c.getFooterIcon(), user, channel, guild, banner, reason));
        if (!c.getFooterText().isEmpty())
            em.withFooterText(format(c.getFooterText(), user, channel, guild, banner, reason));
        if (c.isIncludeTimestamp())
            em.withFooterText((c.getFooterText().isEmpty() ? "" : " | ") + format("[[timestamp]]", user, channel, guild, banner, reason));
        return em.build();
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
                .replace("[[modname]]", banner.getName())
                .replace("[[moddescrim]]", banner.getDiscriminator())
                .replace("[[modmention]]", banner.mention())
                .replace("[[modicon]]", banner.getAvatarURL());
    }
}
