package me.xaanit.apparatus.objects.commands.dev;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import static me.xaanit.apparatus.util.Util.*;

/**
 * Created by Jacob on 5/17/2017.
 */
public class Logout implements ICommand {
    @Override
    public String getName() {
        return "logout";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "shutdown"};
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public String getInfo() {
        return "Logs out the bot.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        EmbedBuilder em = new EmbedBuilder();
        em.withAuthorIcon(botAva());
        em.withAuthorName("Logout!");
        em.withDesc("Logging out.... Saving guilds...");
        em.withColor(hexToColor(CColors.BASIC));
        IMessage m = sendMessage(channel, em.build());
        long begin = System.currentTimeMillis();
        for (long key : GlobalVars.guilds.keySet()) {
            Database.saveGuild(GlobalVars.guilds.get(key));
        }
        em.withDesc("Guilds saved!");
        em.withFooterText("Took me [ " + (System.currentTimeMillis() - begin) + " ms ] to save [ " + GlobalVars.guilds.size() + " ] guilds.");
        editMessage(m, em.build());
        client.logout();
        System.exit(0);
    }
}
