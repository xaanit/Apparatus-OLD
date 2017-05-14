package me.xaanit.apparatus.util;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;

import java.util.EnumSet;

/**
 * Created by Jacob on 5/13/2017.
 */
public class PermissionsUtil {

    /* private static EnumSet<Permissions> makePermissions(Permissions... p) {
        EnumSet<Permissions> perms = EnumSet.noneOf(Permissions.class);
        Arrays.asList(p).forEach(perm -> perms.add(perm));
        return perms;
    }*/

    public static EnumSet<Permissions> makePermissions(EnumSet<Permissions> basic, Permissions... p) {
        for (Permissions perm : p)
            basic.add(perm);
        return basic;
    }

    public static EnumSet<Permissions> basicPermissions() {
        EnumSet<Permissions> perms = EnumSet.noneOf(Permissions.class);
        perms.add(Permissions.READ_MESSAGES);
        perms.add(Permissions.READ_MESSAGE_HISTORY);
        perms.add(Permissions.EMBED_LINKS);
        return perms;
    }

    public static EnumSet<Permissions>[] getPermissions(IGuild guild, ICommand command) {
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

    public static boolean hasPerms(IGuild guild, ICommand command) {
        return getPermissions(guild, command)[1].isEmpty();
    }

    public static String getMissingPermissions(IGuild guild, ICommand command) {
        StringBuilder res = new StringBuilder();
        res.append("```diff\nI am missing some permissions (in red). Please contact the server admins to make sure I have these permissions.\n\n");
        EnumSet[] perms = getPermissions(guild, command);
        perms[0].forEach(p -> res.append("+" + p.toString() + "\n"));
        perms[1].forEach(p -> res.append("-" + p.toString() + "\n"));
        return res.append("```").toString();
    }
}
