package me.xaanit.apparatus.util;

import me.xaanit.apparatus.GlobalVars;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Jacob on 4/19/2017.
 */
@SuppressWarnings("unused")
public class UserUtil extends RoleUtil {

    /**
     * Gets the username and descriminator combo for a user.
     *
     * @param user The user
     * @return The combo
     */
    public static String getNameAndDescrim(IUser user) {
        return user.getName() + "#" + user.getDiscriminator();
    }

    public static void addRole(IUser user, IRole role) {
        RequestBuffer.request(() -> {
            try {
                user.addRole(role);
            } catch (DiscordException ex) {
            }
        });
    }

    public static void banUser(IUser user, IGuild guild) {
        RequestBuffer.request(() -> {
            guild.banUser(user);
        });
    }

    public static void kickUser(IUser user, IGuild guild) {
        RequestBuffer.request(() -> {
            guild.kickUser(user);
        });
    }

    public static void removeRole(IUser user, IRole role) {
        RequestBuffer.request(() -> {
            user.removeRole(role);
        });
    }

    /**
     * Grabs a user from a string
     *
     * @param toLookFor The String to look with
     * @param guild     The guild
     * @return The user if found, null otherwise
     */
    public static IUser getUser(String toLookFor, IGuild guild) {
        toLookFor = toLookFor.trim();

        List<String> lol = new ArrayList<>();
        String k = lol.stream().filter(s -> s.length() == 4).findFirst().orElse(null);

        if (toLookFor.matches("<@!?[0-9]+>")) {
            IUser exists = guild.getUserByID(Long.parseLong(toLookFor.replaceAll("<@!>", "")));
            if (exists != null) {
                return exists;
            }
        }

        final String lower = toLookFor.toLowerCase();
        List<IUser> users = new ArrayList<>();
        List<IUser> us = guild.getUsers();
        users.addAll(us.stream().filter(u -> u.getName().equalsIgnoreCase(lower)).collect(Collectors.toList()));
        users.addAll(us.stream().filter(u -> u.getName().toLowerCase().contains(lower)).collect(Collectors.toList()));
        users.addAll(us.stream().filter(u -> (u.getName() + "#" + u.getDiscriminator()).equalsIgnoreCase(lower)).collect(Collectors.toList()));
        users.addAll(us.stream().filter(u -> u.getDiscriminator().equalsIgnoreCase(lower)).collect(Collectors.toList()));
        users.addAll(us.stream().filter(u -> u.getDisplayName(guild).equalsIgnoreCase(lower)).collect(Collectors.toList()));
        users.addAll(us.stream().filter(u -> u.getDisplayName(guild).toLowerCase().contains(lower)).collect(Collectors.toList()));
        if (!users.isEmpty()) {
            return users.get(0);
        }

        return null;
    }

    public static IUser getUser(String toLookFor, IMessage message) {
        return getUser(toLookFor, message.getGuild());
    }


    /**
     * Returns whether or not the user in question is the bot
     *
     * @param user The user to look for
     * @return True if it is; false otherwise
     */
    public static boolean isOurUser(IUser user) {
        return user.getStringID().equals(GlobalVars.client.getOurUser().getStringID());
    }

    /**
     * Formats the presence of a user into readable text.
     *
     * @param arg The presence
     * @return The readable string
     */
    public static String formatPresence(IPresence arg) {
        String res = "";
        Optional<String> playingo = arg.getPlayingText();
        String playing;

        if (playingo.isPresent()) {
            playing = playingo.toString().replace("Optional[", "").replace("]", "");
        } else {
            playing = "nothing";
        }

        Optional<String> streamingo = arg.getStreamingUrl();
        String streaming;
        if (streamingo.isPresent()) {
            streaming = streamingo.toString().replace("Optional[", "").replace("]", "");
        } else {
            streaming = "nothing";
        }

        StatusType online = arg.getStatus();

        res += "Playing " + playing + ".\nStreaming " + streaming + ".\nStatus: " + online;

        return res;
    }

    /**
     * Formats a role list to be more readable
     *
     * @param roles The role list
     * @return The formatted string
     */
    @SuppressWarnings("unused")
    public static String compactRoles(List<IRole> roles) {
        StringBuilder res = new StringBuilder();
        if (roles.isEmpty()) {
            return "No roles have been added";
        }
        roles.get(0).getPosition();
        for (IRole role : roles) {
            res.append(role.isEveryoneRole() ? "" : role.getName() + ", ");
        }
        return res.substring(0, res.toString().lastIndexOf(','));
    }


}
