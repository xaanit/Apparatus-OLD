package me.xaanit.apparatus.commands.cute;

import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.interfaces.ICommand;
import me.xaanit.apparatus.util.CuteUtil;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

import static me.xaanit.apparatus.util.CuteUtil.GifType.HEAD_PAT;
import static me.xaanit.apparatus.util.Util.*;

public class HeadPat implements ICommand {
    @Override
    public String getName() {
        return "headpat";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
    }

    @Override
    public CmdType getType() {
        return CmdType.CUTE;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " <user>"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Pat a user's head!";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        if (args.length == 1) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("Missing arguments!");
            sendMessage(channel, em.build());
            return;
        }

        IUser u = getUser(combineArgs(args, 1, args.length), message);
        if (u == null) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("I could not find that user!");
            sendMessage(channel, em.build());
            return;
        }

        if (u.getLongID() == 235542999063461888L && user.getLongID() != 233611560545812480L) return; // mine
        if (u.getLongID() == 233611560545812480L && user.getLongID() != 235542999063461888L) return; // I'm hers

        EmbedBuilder em = new EmbedBuilder();
        em.withColor(hexToColor(CColors.BASIC));
        em.withAuthorIcon(guild.getIconURL());
        em.withAuthorName(getName());
        em.withImage(CuteUtil.getRandomGif(HEAD_PAT));
        em.withFooterText("Requested by: " + getNameAndDescrim(user));
        sendMessage(channel, user.mention() + " patted " + u.mention() + "'s head.", em.build());
    }
}
