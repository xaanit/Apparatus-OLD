package me.xaanit.apparatus.commands.dev;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.internal.json.JsonGuild;
import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.interfaces.ICommand;
import me.xaanit.apparatus.listeners.ReadyListener;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.io.File;
import java.util.Arrays;

import static me.xaanit.apparatus.util.Util.*;

/**
 * Created by Jacob on 5/14/2017.
 */
public class Reload implements ICommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " <section>"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Reloads a specific section of the bot";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        if (args.length == 1) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("Please add an argument.");
            sendMessage(channel, em.build());
            return;
        }

        if (args[1].equalsIgnoreCase("guild")) {
            guilds.put(guild.getLongID(), Database.loadGuild(guild));
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(hexToColor(CColors.BASIC));
            em.withAuthorIcon(botAva());
            em.withAuthorName("Reload");
            em.withDesc("Reloaded the guild..");
            em.withFooterText("Reloaded guild " + guild.getName());
            sendMessage(channel, em.build());
            return;
        }

        if (args[1].equalsIgnoreCase("guilds")) {
            long now = System.currentTimeMillis();
            int s = 0;
            int f = 0;
            synchronized (this) {
                for (long key : GlobalVars.guilds.keySet()) {
                    IGuild g = GlobalVars.client.getGuildByID(key);
                    if (g == null) {
                        GlobalVars.guilds.remove(key);
                        f++;
                    } else {
                        GlobalVars.guilds.put(key, Database.loadGuild(g));
                        s++;
                    }
                }
                ReadyListener.initMusicManagers();
            }
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(hexToColor(CColors.BASIC));
            em.withAuthorIcon(botAva());
            em.withAuthorName("Reload");
            em.withDesc("Reloaded [ " + s + " ] guilds, removed [ " + f + " ].");
            long diff = System.currentTimeMillis() - now;
            em.withFooterText("Took " + (diff / 1000.0) + " second(s) to attempt to save [ " + GlobalVars.guilds.size() + " ] guilds.");
            sendMessage(channel, em.build());
            return;
        }

        if (args[1].equalsIgnoreCase("prune")) {
            int pruned = 0;
            for (File file : new File(GlobalVars.PATH + "guilds").listFiles()) {
                if (GlobalVars.client.getGuildByID(Long.parseUnsignedLong(file.getName().replaceAll("\\.json", ""))) == null) {
                    file.delete();
                    pruned++;
                }
            }
            EmbedBuilder em = basicEmbed(user, "Prune", CColors.BASIC);
            em.withDesc("Pruned [ " + pruned + " ] guilds.");
            sendMessage(channel, em.build());
        }

        if (args[1].equalsIgnoreCase("gcommands")) {
            long now = System.currentTimeMillis();

            synchronized (this) {
                for (long key : GlobalVars.guilds.keySet()) {
                    JsonGuild g = GlobalVars.guilds.get(key);
                    g.updateCommands();
                    GlobalVars.guilds.put(key, g);
                }
            }
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(hexToColor(CColors.BASIC));
            em.withAuthorIcon(botAva());
            em.withAuthorName("Reload");
            em.withDesc("Reloaded commands for all guilds.");
            long diff = System.currentTimeMillis() - now;
            em.withFooterText("Took " + (diff / 1000.0) + " second(s) to reload all commands for all guilds.");
            sendMessage(channel, em.build());
        }


    }
}
