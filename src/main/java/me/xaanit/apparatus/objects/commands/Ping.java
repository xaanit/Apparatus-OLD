package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.GuildUtil;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.time.ZoneOffset;
import java.util.Arrays;

/**
 * Created by Jacob on 5/21/2017.
 */
public class Ping implements ICommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
    }

    @Override
    public CmdType getType() {
        return CmdType.UTIL;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{GuildUtil.getGuild(guild).getPrefix(), getName()}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Ping!";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.allChecks(user, guild, this, channel);

        IMessage m = Util.sendMessage(channel, "Pong!");

        long diff = m.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli() - message.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli();
        EmbedBuilder em = new EmbedBuilder();
        em.withAuthorIcon(Util.botAva());
        em.withColor(Util.hexToColor(CColors.BASIC));
        em.withAuthorName("Ping! Stats");
        em.withDesc("Time in between messages: " + diff + "ms.\nTime it took the Rest API to get back to me: " + guild.getShard().getResponseTime() + "ms");
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested by: " + Util.getNameAndDescrim(user));
        Util.editMessage(m, em.build());
    }
}
