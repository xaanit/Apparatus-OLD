package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.api.outside.Requests;
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

import java.util.Arrays;

/**
 * Created by Jacob on 5/19/2017.
 */
public class Aww implements ICommand {
    @Override
    public String getName() {
        return "aww";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "cute"};
    }

    @Override
    public CmdType getType() {
        return CmdType.FUN;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{GuildUtil.getGuild(guild).getPrefix(), getName()}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Gets a cute picture.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.allChecks(user, guild, this, channel);
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(Util.hexToColor(CColors.BASIC));
        em.withAuthorIcon(Util.botAva());
        em.withAuthorName("Aww!");
        em.withImage(Requests.getCuteImage());
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested By: " + Util.getNameAndDescrim(user));
        Util.sendMessage(channel, em.build());
    }
}
