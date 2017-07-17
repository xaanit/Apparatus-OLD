package me.xaanit.apparatus.util;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class RoleUtil extends PermissionsUtil {

    /**
     * Grabs a role based on a string, or mention
     *
     * @param toLookFor The String
     * @param m         The message
     * @param guild     The guild
     * @return The role if found, null otherwise
     */
    public static IRole getRole(String toLookFor, IMessage m, IGuild guild) {
        toLookFor = toLookFor.trim();
        final String lower = toLookFor.toLowerCase();

        if (toLookFor.matches("<@&[0-9]+>")) {
            IRole exists = guild.getRoleByID(Long.parseLong(toLookFor.replaceAll("[<@&>]", "")));
            if (exists != null) {
                return exists;
            }
        }
        List<IRole> roles = new ArrayList<>();
        List<IRole> rs = guild.getRoles();
        roles.addAll(rs.stream().filter(r -> r.getName().equalsIgnoreCase(lower)).collect(Collectors.toList()));
        roles.addAll(rs.stream().filter(r -> r.getName().toLowerCase().contains(lower)).collect(Collectors.toList()));
        if (!roles.isEmpty()) {
            return roles.get(0);
        }

        return null;
    }

    public static IRole getRole(String toLookFor, IMessage message) {
        return getRole(toLookFor, message, message.getGuild());
    }

    public static IRole getHighestRole(IUser user, IGuild guild) {
        List<IRole> roles = guild.getRoles();
        roles.get(0).getPosition();
        for (int i = roles.size() - 1; i >= 0; i--) {
            final IRole role = roles.get(i);
            if (user.getRolesForGuild(guild).stream().filter(r -> r.getLongID() == role.getLongID()).count() == 1)
                return roles.get(i);
        }
        return guild.getEveryoneRole();
    }

    /**
     * Formats a role list
     *
     * @param roles The role list
     * @return The formatted string
     */
    public static String formatRoleList(List<IRole> roles) {
        StringBuilder res = new StringBuilder();
        for (IRole role : roles) {
            res.append(role.getName()).append(", ");
        }
        return res.substring(0, res.length() - 2);
    }
}
