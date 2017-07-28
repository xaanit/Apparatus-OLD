package me.xaanit.apparatus.commands.fun;

import me.xaanit.apparatus.api.outside.Requests;
import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import static me.xaanit.apparatus.util.Util.*;

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
    public String getInfo() {
        return "Gets a cute picture.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(hexToColor(CColors.BASIC));
        em.withAuthorIcon(botAva());
        em.withAuthorName("Aww!");
        em.withImage(Requests.getCuteImage());
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested By: " + getNameAndDescrim(user));
        sendMessage(channel, em.build());
    }
}
