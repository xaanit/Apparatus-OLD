package me.xaanit.apparatus.objects.interfaces;

import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;

import java.util.EnumSet;
import java.util.Map;

public interface ICommand {

    /**
     * The name of the command
     *
     * @return The name
     */
    String getName();

    /**
     * All the aliases for the command
     *
     * @return The aliases
     */
    String[] getAliases();

    /**
     * Gets the command type
     *
     * @return The type.
     */
    CmdType getType();

    /**
     * The required permissions for this command
     *
     * @return
     */
    default EnumSet<Permissions> getNeededPermission() {
        return Util.basicPermissions();
    }

    default Map<Long, Integer> getPlaceInCommand(IUser user) {
        return null;
    }

    /**
     * Gets the needed permissions for the user
     *
     * @return The permission
     */
    default Permissions getUserPerm() {
        return null;
    }

    /**
     * The help embed for this command
     *
     * @param user  The user who requested it
     * @param guild The guild it was run it
     * @return The built EmbedObject
     */
    EmbedObject getHelp(IUser user, IGuild guild);

    default Permissions getModPerm() {
        return null;
    }

    /**
     * Gets if the command requires a Patron role or not.
     *
     * @return {@code true} if it does; otherwise {@code false}
     */
    default boolean requiresPatron() {
        return false;
    }

    /**
     * The info text for this command
     *
     * @return The info string
     */
    String getInfo();

    /**
     * Runs the command
     *
     * @param user    The user who ran
     * @param channel The channel it was ran in
     * @param guild   The guild it was ran in
     * @param message The message
     * @param args    The arguments associated with it
     * @param client  The client
     */
    void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client);

}

