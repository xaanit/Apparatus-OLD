package me.xaanit.apparatus.objects.interfaces;

import me.xaanit.apparatus.objects.enums.CmdType;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;

import java.util.EnumSet;

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
    EnumSet<Permissions> getNeededPermission();

    /**
     * Gets the needed permissions for the user
     *
     * @return The permission
     */
    Permissions getUserPerm();

    /**
     * The help embed for this command
     *
     * @param user The user who requested it
     * @return The built EmbedObject
     */
    EmbedObject getHelp(IUser user);

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
     */
    void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args);

}

