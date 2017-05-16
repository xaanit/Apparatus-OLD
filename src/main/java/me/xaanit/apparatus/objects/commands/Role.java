package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.objects.FailedRole;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Jacob on 5/15/2017.
 */
public class Role implements ICommand {
    @Override
    public String getName() {
        return "role";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "roleme", "selfrole"};
    }

    @Override
    public CmdType getType() {
        return CmdType.UTIL;
    }

    @Override
    public EnumSet<Permissions> getNeededPermission() {
        return Util.makePermissions(Util.basicPermissions(), Permissions.MANAGE_ROLES);
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{Util.getGuild(guild).getPrefix(), getName() + "<add/remove/role names> [role names]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Allows you to assign yourself multiple self assignable roles.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.allChecks(user, guild, this, channel);

        if (args.length == 1) {
            List<IRole> roles = new ArrayList<>();
            for (long l : Util.getGuild(guild).getAssignableRoles()) {
                IRole role = guild.getRoleByID(l);
                if (role == null) {
                    Util.getGuild(guild).removeAutorole(l);
                } else {
                    roles.add(role);
                }
            }
            EmbedBuilder em = new EmbedBuilder();
            em.withAuthorName("Self assignable roles");
            em.withAuthorIcon("");
        }

        if (args[1].equalsIgnoreCase("list")) {

        }

        List<IRole> rolesAdded = new ArrayList<>();
        List<FailedRole> rolesFailed = new ArrayList<>();

    }

    private void moduleAddRoles(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
    }
}
