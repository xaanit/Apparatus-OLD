package me.xaanit.apparatus.objects.commands.util;

import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.time.ZoneOffset;

import static me.xaanit.apparatus.util.Util.*;

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
    public String getInfo() {
        return "Ping!";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        IMessage m = sendMessage(channel, "Pong!");

        long diff = m.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli() - message.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli();
        EmbedBuilder em = new EmbedBuilder();
        em.withAuthorIcon(botAva());
        em.withColor(hexToColor(CColors.BASIC));
        em.withAuthorName("Ping! Stats");
        em.withDesc("Time in between messages: " + diff + "ms.\nTime it took the Rest API to get back to me: " + guild.getShard().getResponseTime() + "ms");
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested by: " + getNameAndDescrim(user));
        editMessage(m, em.build());
    }
}
