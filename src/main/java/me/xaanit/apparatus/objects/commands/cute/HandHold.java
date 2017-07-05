package me.xaanit.apparatus.objects.commands.cute;

import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.CuteUtil;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

import static me.xaanit.apparatus.util.Util.*;
import static me.xaanit.apparatus.util.CuteUtil.GifType.*;

public class HandHold implements ICommand {
    @Override
    public String getName() {
        return "handhold";
    }

    @Override
    public String[] getAliases() {
        return new String[] {getName()};
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
        return "Hold hands with a user!";
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

        EmbedBuilder em = new EmbedBuilder();
        em.withColor(hexToColor(CColors.BASIC));
        em.withAuthorIcon(guild.getIconURL());
        em.withAuthorName(getName());
        em.withImage(CuteUtil.getRandomGif(HOLDING_HANDS));
        em.withFooterText("Requested by: " + getNameAndDescrim(user));
        sendMessage(channel, user.mention() + " held hands with " + u.mention(), em.build());
    }
}
