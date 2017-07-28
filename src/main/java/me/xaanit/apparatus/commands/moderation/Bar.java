package me.xaanit.apparatus.commands.moderation;

import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.util.EnumSet;

import static me.xaanit.apparatus.util.Util.*;

public class Bar implements ICommand {
    @Override
    public String getName() {
        return "bar";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
    }

    @Override
    public CmdType getType() {
        return null;
    }

    @Override
    public EnumSet<Permissions> getNeededPermission() {
        return makePermissions(basicPermissions(), Permissions.BAN);
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        return null;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        if (!user.equals(guild.getOwner())) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("Only the owner can use this command!");
            sendMessage(channel, em.build());
            return;
        }

        if(args.length == 1) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("Missing arguments!");
            sendMessage(channel, em.build());
            return;
        }


    }
}
