package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

import static me.xaanit.apparatus.GlobalVars.logger;

/**
 * Created by Jacob on 5/15/2017.
 */
public class Save implements ICommand {
    @Override
    public String getName() {
        return "save";
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
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{Util.getGuild(guild).getPrefix(), getName() + "[guildID]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Saves either the specified guild, or all if none are specified.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.allChecks(user, guild, this, channel);
        long now = System.currentTimeMillis();
        if (args.length == 1) {
            int s = 0;
            int f = 0;
            for (long key : GlobalVars.guilds.keySet()) {
                if (Database.saveGuild(GlobalVars.guilds.get(key))) {
                    logger.log("Saved guild [" + key + "]!", Level.INFO);
                    s++;
                } else {
                    logger.log("Failed to save guild [" + key + "]!", Level.CRITICAL);
                    f++;
                }
            }
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(Util.hexToColor(CColors.BASIC));
            em.withAuthorIcon(GlobalVars.client.getOurUser().getAvatarURL());
            em.withAuthorName("Save");
            em.withDesc("Saved [ " + s + " ] guild(s).\nFailed to save [ " + f + " ] guild(s)");
            long diff = System.currentTimeMillis() - now;

            em.withFooterText("Took " + (diff / 1000.0) + " second(s) to attempt to save [ " + (s + f) + " ] guilds.");
            Util.sendMessage(channel, em.build());
            return;
        } else {
            IGuild g;
            try {
                 g = GlobalVars.client.getGuildByID(Long.parseUnsignedLong(args[1]));
            } catch (NumberFormatException ex) {
                EmbedBuilder em = new EmbedBuilder();
                em.withColor(Util.hexToColor(CColors.ERROR));
                em.withAuthorIcon(GlobalVars.client.getApplicationIconURL());
                em.withAuthorName("Error");
                em.withDesc("That is not a valid ID");
                Util.sendMessage(channel, em.build());
                return;
            }
            if (g == null) {
                EmbedBuilder em = new EmbedBuilder();
                em.withColor(Util.hexToColor(CColors.ERROR));
                em.withAuthorIcon(GlobalVars.client.getApplicationIconURL());
                em.withAuthorName("Error");
                em.withDesc("Guild does not exist.");
                em.withFooterText("No guild with the ID " + args[1]);
                Util.sendMessage(channel, em.build());
                return;
            }
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(Util.hexToColor(CColors.BASIC));
            em.withAuthorIcon(GlobalVars.client.getApplicationIconURL());
            em.withAuthorName("Save");
            em.withDesc(Database.saveGuild(g) ? "Guild [ " + g.getName() + " ] saved successfully." : "Failed to save guild [ " + g.getName() + "].");
            em.withFooterText("Saved the guild with the ID " + args[1]);
            Util.sendMessage(channel, em.build());
            return;
        }
    }
}
