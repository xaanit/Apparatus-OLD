package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IShard;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

import static me.xaanit.apparatus.util.Util.*;

/**
 * Created by Jacob on 5/15/2017.
 */
public class Shardinfo implements ICommand {
    @Override
    public String getName() {
        return "shardinfo";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "shards"};
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName()}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Returns info on all shards.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        allChecks(user, guild, this, channel);
        EmbedBuilder em = basicEmbed(user, "Shard info!", CColors.BASIC);
        for (IShard shard : GlobalVars.client.getShards()) {
            String temp = "";
            temp += " Shard #" + shard.getInfo()[0] + " | " + shard.getGuilds().size() + " guild(s) | " + shard.getUsers().size() + " user(s) | " + shard.getChannels().size() + " channel(s)";
            em.appendDesc(temp + "\n");
        }
        sendMessage(channel, em.build());
    }

}
