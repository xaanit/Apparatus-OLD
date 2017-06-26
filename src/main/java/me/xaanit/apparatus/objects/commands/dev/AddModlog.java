package me.xaanit.apparatus.objects.commands.dev;

import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.internal.json.JsonGuild;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

import static me.xaanit.apparatus.util.Util.*;


/**
 * Created by Jacob on 5/27/2017.
 */
public class AddModlog implements ICommand {
    @Override
    public String getName() {
        return "addmodlog";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "addml", "aml"};
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " <name>"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Adds a modlog";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        if (args.length == 1) {
            sendMessage(channel, basicEmbed(user, "Error", CColors.ERROR).withDesc("You're missing the modlog argument").build());
            return;
        }

        if (args.length > 2) {
            if (args[2].equalsIgnoreCase("channel")) {
                getGuild(guild).getModlog(args[1]).addChannel(channel.getLongID());
                return;
            }
        }

        for (JsonGuild val : guilds.values()) {
            val.addModlog(args[1]);
            guilds.put(val.getId(), val);
            Database.saveGuild(val);
        }

        sendMessage(channel, basicEmbed(user, "Modlog", CColors.BASIC).withDesc("Added modlog [ " + args[1] + " ]").build());
    }
}
