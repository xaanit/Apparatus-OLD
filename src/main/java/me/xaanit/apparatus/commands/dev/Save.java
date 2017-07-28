package me.xaanit.apparatus.commands.dev;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.interfaces.ICommand;
import me.xaanit.simplelogger.SimpleLogger;
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
 * Created by Jacob on 5/15/2017.
 */
public class Save implements ICommand {

    private SimpleLogger logger = SimpleLogger.getLoggerByClass(Database.class);

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + "[guildID]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public String getInfo() {
        return "Saves either the specified guild, or all if none are specified.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);
        long now = System.currentTimeMillis();
        if (args.length == 1) {
            int s = 0;
            int f = 0;
            for (long key : GlobalVars.guilds.keySet()) {
                if (Database.saveGuild(GlobalVars.guilds.get(key))) {
                    logger.info("Saved guild [" + key + "]!");
                    s++;
                } else {
                    logger.critical("Failed to save guild [" + key + "]!");
                    f++;
                }
            }
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(hexToColor(CColors.BASIC));
            em.withAuthorIcon(botAva());
            em.withAuthorName("Save");
            em.withDesc("Saved [ " + s + " ] guild(s).\nFailed to save [ " + f + " ] guild(s)");
            long diff = System.currentTimeMillis() - now;

            em.withFooterText("Took " + (diff / 1000.0) + " second(s) to attempt to save [ " + (s + f) + " ] guilds.");
            sendMessage(channel, em.build());
            return;
        } else {
            IGuild g;
            try {
                g = GlobalVars.client.getGuildByID(Long.parseUnsignedLong(args[1]));
            } catch (NumberFormatException ex) {
                EmbedBuilder em = new EmbedBuilder();
                em.withColor(hexToColor(CColors.ERROR));
                em.withAuthorIcon(GlobalVars.client.getApplicationIconURL());
                em.withAuthorName("Error");
                em.withDesc("That is not a valid ID");
                sendMessage(channel, em.build());
                return;
            }
            if (g == null) {
                EmbedBuilder em = new EmbedBuilder();
                em.withColor(hexToColor(CColors.ERROR));
                em.withAuthorIcon(GlobalVars.client.getApplicationIconURL());
                em.withAuthorName("Error");
                em.withDesc("Guild does not exist.");
                em.withFooterText("No guild with the ID " + args[1]);
                sendMessage(channel, em.build());
                return;
            }
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(hexToColor(CColors.BASIC));
            em.withAuthorIcon(GlobalVars.client.getApplicationIconURL());
            em.withAuthorName("Save");
            em.withDesc(Database.saveGuild(g) ? "Guild [ " + g.getName() + " ] saved successfully." : "Failed to save guild [ " + g.getName() + "].");
            em.withFooterText("Saved the guild with the ID " + args[1]);
            sendMessage(channel, em.build());
            return;
        }
    }
}
