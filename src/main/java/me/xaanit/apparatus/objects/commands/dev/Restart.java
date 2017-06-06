package me.xaanit.apparatus.objects.commands.dev;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
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
 * Created by Jacob on 5/21/2017.
 */
public class Restart implements ICommand {
    @Override
    public String getName() {
        return "restart";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "update"};
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + "<shard>"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Restarts a shard.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        if (args.length == 1) {
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(hexToColor(CColors.ERROR));
            em.withAuthorIcon(botAva());
            em.withAuthorName("Restart!!");
            em.withFooterIcon(user.getAvatarURL());
            em.withFooterText("Requested by: " + getNameAndDescrim(user));
            em.withDesc("Please provide a shard to restart!");
            sendMessage(channel, em.build());
            return;
        }

        int shard = -1;
        try {
            shard = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(hexToColor(CColors.ERROR));
            em.withAuthorIcon(botAva());
            em.withAuthorName("Restart!!");
            em.withFooterIcon(user.getAvatarURL());
            em.withFooterText("Requested by: " + getNameAndDescrim(user));
            em.withDesc("You must provide a valid number!");
            sendMessage(channel, em.build());
            return;
        }
        if (shard >= GlobalVars.client.getShardCount()) {
            EmbedBuilder em = new EmbedBuilder();
            em.withColor(hexToColor(CColors.ERROR));
            em.withAuthorIcon(botAva());
            em.withAuthorName("Restart!!");
            em.withFooterIcon(user.getAvatarURL());
            em.withFooterText("Requested by: " + getNameAndDescrim(user));
            em.withDesc("Said shard doesn't exist!");
            sendMessage(channel, em.build());
            return;
        }

        long start = System.currentTimeMillis();
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(hexToColor(CColors.BASIC));
        em.withAuthorIcon(botAva());
        em.withAuthorName("Restart!!");
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested by: " + getNameAndDescrim(user));
        em.withDesc("Restarting shard " + shard + "...");
        IMessage m = sendMessage(channel, em.build());
        IShard s = GlobalVars.client.getShards().get(shard);
        s.logout();
        s.login();
        em.withDesc("Shard restarted! Took " + (System.currentTimeMillis() - start) + "ms");
        editMessage(m, em.build());
    }
}
