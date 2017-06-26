package me.xaanit.apparatus.objects.commands.moderation;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.exceptions.PermissionsException;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.PermissionsUtil;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.util.EnumSet;

import static me.xaanit.apparatus.util.EmbedUtil.basicEmbed;
import static me.xaanit.apparatus.util.MessageUtil.combineArgs;
import static me.xaanit.apparatus.util.MessageUtil.sendMessage;
import static me.xaanit.apparatus.util.PermissionsUtil.allChecks;
import static me.xaanit.apparatus.util.PermissionsUtil.makePermissions;
import static me.xaanit.apparatus.util.RoleUtil.getHighestRole;
import static me.xaanit.apparatus.util.UserUtil.getUser;


public class Kick implements ICommand {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public EnumSet<Permissions> getNeededPermission() {
        return makePermissions(PermissionsUtil.basicPermissions(), Permissions.KICK);
    }

    @Override
    public Permissions getUserPerm() {
        return Permissions.KICK;
    }

    @Override
    public String[] getAliases() {
        return new String[] {getName(), "kek"};
    }

    @Override
    public CmdType getType() {
        return CmdType.MODERATION;
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


        if (u == null) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("I could not find the specified user.");
            sendMessage(channel, em.build());
            return;
        }

        kickCheck(user, u, guild, channel);

        guild.kickUser(u);

        EmbedBuilder em = basicEmbed(user, "Kicked User", CColors.BASIC);

        em.withTitle("__User Kicked__");
        em.appendField("Staff", message.getAuthor().getName(), false);
        em.appendField("User", u.getName(), false);
        em.appendField("Reason", reason, false);

        sendMessage(channel, em.build());
    }


    private void kickCheck(IUser user, IUser u, IGuild guild, IChannel channel) {
        if (u == null) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("I could not find the user based on your input.");
            sendMessage(channel, em.build());
            throw new PermissionsException();
        }

        if (getHighestRole(u, guild).getPosition() >= getHighestRole(user, guild).getPosition() && !user.equals(guild.getOwner())) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("You can only ban users with a rank below yours! Their highest rank is: [ " + getHighestRole(u, guild).getName().toUpperCase() + " ]");
            sendMessage(channel, em.build());
            throw new PermissionsException();
        }

        if (u.equals(guild.getOwner())) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("You can not ban the owner!");
            sendMessage(channel, em.build());
            throw new PermissionsException();
        }

        if (getHighestRole(GlobalVars.client.getOurUser(), guild).getPosition() <= getHighestRole(u, guild).getPosition()) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("I can only ban users who have a role lower than my highest role!");
            sendMessage(channel, em.build());
            throw new PermissionsException();
        }

        if (u.equals(user)) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("You can not ban yourself!");
            sendMessage(channel, em.build());
            throw new PermissionsException();
        }
    }
}
