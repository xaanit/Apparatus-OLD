package me.xaanit.apparatus.util;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.exceptions.PermissionsException;
import me.xaanit.apparatus.interfaces.ICommand;
import me.xaanit.simplelogger.SimpleLogger;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;


public class PermissionsUtil extends MessageUtil {

    private static IGuild devGuild = null;

    private static SimpleLogger logger = new SimpleLogger(Permissions.class);

    public static EnumSet<Permissions> makePermissions(EnumSet<Permissions> basic, Permissions... p) {
        Collections.addAll(basic, p);
        return basic;
    }

    public static EnumSet<Permissions> basicPermissions() {
        EnumSet<Permissions> perms = EnumSet.noneOf(Permissions.class);
        perms.add(Permissions.READ_MESSAGES);
        perms.add(Permissions.READ_MESSAGE_HISTORY);
        perms.add(Permissions.EMBED_LINKS);
        perms.add(Permissions.USE_EXTERNAL_EMOJIS);
        return perms;
    }

    private static EnumSet[] getPermissions(IGuild guild, ICommand command) {
        EnumSet<Permissions> has = EnumSet.noneOf(Permissions.class);
        EnumSet<Permissions> needs = EnumSet.noneOf(Permissions.class);
        EnumSet<Permissions> guildPerms = GlobalVars.client.getOurUser().getPermissionsForGuild(guild);
        EnumSet<Permissions> commandPerms = command.getNeededPermission();
        for (Permissions p : commandPerms) {
            if (guildPerms.contains(p)) has.add(p);
            else needs.add(p);
            commandPerms.remove(p);
        }
        return new EnumSet[]{has, needs};
    }

    private static boolean hasPerms(IGuild guild, ICommand command) {
        return getPermissions(guild, command)[1].isEmpty();
    }

    private static String getMissingPermissions(IGuild guild, ICommand command) {
        StringBuilder res = new StringBuilder();
        res.append("```diff\nI am missing some permissions on the guild ").append(guild.getName()).append(" (in red). Please contact the server admins to make sure I have these permissions.\n\n");
        EnumSet[] perms = getPermissions(guild, command);
        perms[0].forEach(p -> res.append("+").append(p.toString()).append("\n"));
        perms[1].forEach(p -> res.append("-").append(p.toString()).append("\n"));
        return res.append("```\n\nYou are receiving this message due to trying to execute the `").append(command.getName()).append("` command.").toString();
    }


    public static boolean checkChannel(IUser user, IGuild guild, IChannel channel, ICommand command) {
        if (getGuild(guild).getCommand(command.getName()).isChannelsWhitelist())
            return getGuild(guild).getCommand(command.getName()).getChannels().contains(channel.getLongID());
        if (!getGuild(guild).getCommand(command.getName()).isChannelsWhitelist())
            return !getGuild(guild).getCommand(command.getName()).getChannels().contains(channel.getLongID());
        return true;
    }


    private static boolean checkUserPerm(IUser user, IGuild guild, ICommand command) {
        if (getGuild(guild).isDevOverride())
            if (isDev(user))
                return true;
        if (command.getType() == CmdType.DEV) {
            return isDev(user);
        }

        if (command.getType() == CmdType.MUSIC)
            return getGuild(guild).whitelistedGuild || isDev(user);

        if (command.requiresPatron())
            return isPatron(user);

        if (guild.getOwnerLongID() == user.getLongID())
            return true;
        if (!GuildUtil.getGuild(guild).getCommand(command.getName()).isRole())
            return command.getUserPerm() == null || (user.getPermissionsForGuild(guild).contains(command.getUserPerm()) || user.getPermissionsForGuild(guild).contains(Permissions.ADMINISTRATOR));
        for (IRole r : guild.getRolesForUser(user)) {
            for (long l : GuildUtil.getGuild(guild).getCommand(command.getName()).getRoles()) {
                if (l == r.getLongID())
                    return true;
            }
        }
        return false;
    }

    public static boolean isDev(IUser user) {
        for (long l : DEV_IDs)
            if (l == user.getLongID())
                return true;
        return false;
    }

    public static boolean isPatron(IUser user) {
        return isDev(user) || user.getRolesForGuild(devGuild).stream().filter(r -> Util.equalsAny(r, 313909639161053185L, 313909493060861952L, 313909773546291203L, 313909405982916610L)).count() > 0;
    }


    public static void allChecks(IUser user, IGuild guild, ICommand command, IChannel channel) {
        if (command.getType() != CmdType.DEV && !getGuild(guild).getCommand(command.getName()).isEnabled())
            throw new PermissionsException();
        logger.info("Doing all checks on command [" + command.getName()
                + "] from user [" + Util.getNameAndDescrim(user)
                + "] in guild [" + guild.getName()
                + "] in channel [" + channel.getName() + "]");
        if (!hasPerms(guild, command)) {
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(hexToColor(CColors.BASIC));
            em.withDesc(getMissingPermissions(guild, command));
            em.withAuthorName("Missing permissions");
            em.withAuthorIcon(Util.botAva());
            MessageUtil.sendMessage(user.getOrCreatePMChannel(), em.build());
            logger.low("Missing permissions for command [" + command.getName() + "] on guild [" + guild.getName() + "]");
            throw new PermissionsException();
        }

        if (!checkChannel(user, guild, channel, command))
            throw new PermissionsException();

        if (!checkUserPerm(user, guild, command)) {

            if (command.getType() == CmdType.DEV) {
                logger.medium("User [" + Util.getNameAndDescrim(user) + "] tried to use developer command [" + command.getName() + "] on guild [" + guild.getName() + "]");
                EmbedBuilder em = new EmbedBuilder();
                em.withTitle("Error!");
                em.withDesc("This is a developer command, it can only be run by a developer.");
                em.withFooterIcon(user.getAvatarURL());
                em.withFooterText("Requested By: " + UserUtil.getNameAndDescrim(user));
                em.withColor(Util.hexToColor(CColors.ERROR));
                MessageUtil.sendMessage(channel, em.build());
                throw new PermissionsException();
            }

            if (command.getType() == CmdType.MUSIC) {
                EmbedBuilder em = new EmbedBuilder();
                em.withTitle("Error!");
                em.withDesc("This can only be used on a guild that's whitelisted!");
                em.withFooterIcon(user.getAvatarURL());
                em.withFooterText("Requested By: " + UserUtil.getNameAndDescrim(user));
                em.withColor(Util.hexToColor(CColors.ERROR));
                MessageUtil.sendMessage(channel, em.build());
                throw new PermissionsException();
            }


            logger.low("User [" + user.getName() + "] doesn't have correct permissions or role for command [" + command.getName() + "] in guild [" + guild.getName() + "]");
            EmbedBuilder em = new EmbedBuilder();
            em.withTitle("Error!");
            em.withDesc("You either need a specific permission, or a specific role to run this command. Please contact the server admins if you think this is in error.");
            em.appendField("Permission for this command.", command.getUserPerm() == null ? "None" : command.getUserPerm().toString(), false);
            List<IRole> roles = new ArrayList<>();
            List<Long> ids = GuildUtil.getGuild(guild).getCommand(command.getName()).getRoles();
            for (long l : ids) {
                IRole role = guild.getRoleByID(l);
                if (role == null)
                    GuildUtil.getGuild(guild).getCommand(command.getName()).removeRole(l);
                else
                    roles.add(role);
            }
            em.appendField("Roles(s) for this command.", Util.formatRoleList(roles), false);
            em.withFooterIcon(user.getAvatarURL());
            em.withFooterText("Requested By: " + UserUtil.getNameAndDescrim(user));
            em.withColor(Util.hexToColor(CColors.ERROR));
            MessageUtil.sendMessage(channel, em.build());
            throw new PermissionsException();
        }

        logger.info("User [" + user.getName() + "] passed all checks.");
    }
}
