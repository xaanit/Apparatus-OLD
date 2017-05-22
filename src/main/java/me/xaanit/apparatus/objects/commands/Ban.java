package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.GuildUtil;
import me.xaanit.apparatus.util.PermissionsUtil;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * Created by Jacob on 5/13/2017.
 */
public class Ban implements ICommand {
    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "banish"};
    }

    @Override
    public CmdType getType() {
        return CmdType.MODERATION;
    }

    @Override
    public EnumSet<Permissions> getNeededPermission() {
        return PermissionsUtil.makePermissions(PermissionsUtil.basicPermissions(), Permissions.BAN);
    }

    @Override
    public Permissions getUserPerm() {
        return Permissions.BAN;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{GuildUtil.getGuild(guild).getPrefix(), getName() + " <user> [reason]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Bans a user from the server.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.sendMessage(channel, "This is an update!");
    }
}
