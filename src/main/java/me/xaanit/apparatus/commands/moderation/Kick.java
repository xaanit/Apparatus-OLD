package me.xaanit.apparatus.commands.moderation;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.internal.events.KickEvent;
import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.exceptions.PermissionsException;
import me.xaanit.apparatus.interfaces.ICommand;
import me.xaanit.apparatus.util.GuildUtil;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;
import java.util.EnumSet;

import static me.xaanit.apparatus.util.Util.*;

public class Kick implements ICommand {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String[] getAliases() {
        return new String[] {getName()};
    }

    @Override
    public CmdType getType() {
        return CmdType.MODERATION;
    }

    @Override
    public EnumSet<Permissions> getNeededPermission() {
        return makePermissions(basicPermissions(), Permissions.KICK);
    }

    @Override
    public Permissions getUserPerm() {
        return Permissions.KICK;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{GuildUtil.getGuild(guild).getPrefix(), getName() + " <user> [reason]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Kicks a user from the server.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        if (args.length == 1) {
            EmbedBuilder em = basicEmbed(user, "Missing arguments", CColors.ERROR);
            em.withDesc("Please provide a user and an optional reason.");
            sendMessage(channel, em.build());
            return;
        }

        String reason;
        if (args.length == 2) {
            reason = "None provided";
        } else {
            reason = combineArgs(args, 2, args.length);
        }

        IUser u = getUser(args[1], message);


        kickCheck(user, u, guild, channel);

          kickUser(u, guild);
        client.getDispatcher().dispatch(new KickEvent(guild, u, user, channel, reason));
    }


    private void kickCheck(IUser user, IUser u, IGuild guild, IChannel channel) {
        if (u == null) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("I could not find the user based on your input.");
            sendMessage(channel, em.build());
            throw new PermissionsException();
        }

        if (getHighestRole(u, guild).getPosition() >= getHighestRole(user, guild).getPosition()) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("You can only kick users with a rank below yours! Their highest rank is: [ " + getHighestRole(u, guild).getName().toUpperCase() + " ]");
            sendMessage(channel, em.build());
            throw new PermissionsException();
        }

        if (u.equals(guild.getOwner())) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("You can not kick the owner!");
            sendMessage(channel, em.build());
            throw new PermissionsException();
        }

        if (getHighestRole(GlobalVars.client.getOurUser(), guild).getPosition() <= getHighestRole(u, guild).getPosition()) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("I can only kick users who have a role lower than my highest role!");
            sendMessage(channel, em.build());
            throw new PermissionsException();
        }

        if (u.equals(user)) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("You can not kick yourself!");
            sendMessage(channel, em.build());
            throw new PermissionsException();
        }
    }
}
