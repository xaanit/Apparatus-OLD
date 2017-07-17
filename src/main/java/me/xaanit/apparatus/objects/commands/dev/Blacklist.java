package me.xaanit.apparatus.objects.commands.dev;

import me.xaanit.apparatus.database.Database;
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
import sx.blah.discord.util.RequestBuffer;

import java.util.Arrays;

import static me.xaanit.apparatus.util.Util.*;


public class Blacklist implements ICommand {
    @Override
    public String getName() {
        return "blacklist";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "exclude"};
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " <guild/user> [id]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Blacklists a user/guild";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        String type = args.length == 1 ? "GUILD" : args[1];
        long id = args.length == 2 ? guild.getLongID() : Long.parseUnsignedLong(args[2]);


        if (type.equalsIgnoreCase("USER")) {
            if (!config.getBlacklistedUsers().contains(id)) {
                config.blacklistUser(id);
                EmbedBuilder em = basicEmbed(user, "Blacklist", CColors.BASIC);
                em.withDesc("Blacklisted " +(client.getUserByID(id) != null ? getNameAndDescrim(client.getUserByID(id)) : id));
                sendMessage(channel, em.build());
            } else {
                config.unBlacklistUser(id);
                EmbedBuilder em = basicEmbed(user, "Blacklist", CColors.BASIC);
                em.withDesc("Unblacklisted " + (client.getUserByID(id) != null ? getNameAndDescrim(client.getUserByID(id)) : id));
                sendMessage(channel, em.build());
            }
        } else {
            if (!config.getBlacklistedServers().contains(id)) {
                if (client.getGuildByID(id) == null && type.equalsIgnoreCase("GUILD")) {
                    EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
                    em.withDesc("That guild does not exist.");
                    sendMessage(channel, em.build());
                    return;
                }
                config.blacklistServer(id);
                EmbedBuilder em = basicEmbed(user, "Blacklist", CColors.BASIC);
                em.withDesc("Blacklisted " + (client.getGuildByID(id) != null ? client.getGuildByID(id).getName() : id));
                sendMessage(channel, em.build());
                RequestBuffer.request(() -> client.getGuildByID(id).leave());
            } else {
                config.unBlacklistServer(id);
                EmbedBuilder em = basicEmbed(user, "Blacklist", CColors.BASIC);
                em.withDesc("Unblacklisted " + (client.getGuildByID(id) != null ? client.getGuildByID(id).getName() : id));
                sendMessage(channel, em.build());
            }
        }
        Database.saveConfig();
    }
}
