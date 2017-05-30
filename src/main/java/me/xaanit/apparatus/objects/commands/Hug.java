package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

import static me.xaanit.apparatus.util.Util.*;

/**
 * Created by Jacob on 5/23/2017.
 */
public class Hug implements ICommand {
    @Override
    public String getName() {
        return "hug";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
    }

    @Override
    public CmdType getType() {
        return CmdType.FUN;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " [arg]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Hugs a user";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        if (args.length == 1) {
            EmbedBuilder em = basicEmbed(user, "Error!", CColors.ERROR);
            em.withDesc("You need to specify a user to hug!");
            sendMessage(channel, em.build());
            return;
        }

        IUser u = getUser(args[1], message);
        if(u == null) {
            EmbedBuilder em = basicEmbed(user, "Error!", CColors.ERROR);
            em.withDesc("I could not find that user!");
            sendMessage(channel, em.build());
            return;
        }

        if(u.getLongID() == user.getLongID()) {
            EmbedBuilder em = basicEmbed(user, "Hug!", CColors.BASIC);
            em.withDesc("Aw! Self hug!");
            sendMessage(channel, em.build());
            return;
        }

        if (u.getLongID() == guild.getClient().getOurUser().getLongID()) {
            EmbedBuilder em = basicEmbed(user, "Hug!", CColors.BASIC);
            em.withDesc("Aw! Thank you! *Hugs back*");
            sendMessage(channel, em.build());
            return;
        }

        EmbedBuilder em = basicEmbed(user, "Hug!", CColors.BASIC);
        em.withDesc("Aw! You have given a hug to " + u.mention() + "!");
        sendMessage(channel, em.build());
        EmbedBuilder e = basicEmbed(user, "Hug!", CColors.BASIC);
        e.withDesc(user.mention() + " hugged you on guild " + guild.getName() + "!");
        sendMessage(u, e.build());

    }
}
