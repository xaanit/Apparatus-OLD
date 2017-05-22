package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.objects.Update;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.GuildUtil;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

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
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{GuildUtil.getGuild(guild).getPrefix(), getName()}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Restarts the bot (and updates it)";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.allChecks(user, guild, this, channel);

        if (args.length > 1)
            if (!Update.execute(user, channel, message))
                return;

        long start = System.currentTimeMillis();
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(Util.hexToColor(CColors.BASIC));
        em.withAuthorIcon(Util.botAva());
        em.withAuthorName("Restart!!");
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested by: " + Util.getNameAndDescrim(user));
        em.withDesc("Restarting the bot... Please wait...");
        IMessage m = Util.sendMessage(channel, em.build());
        System.exit(22);
        em.withDesc("Restarted! Took " + (System.currentTimeMillis() - start) + "ms");
        Util.editMessage(m, em.build());
    }
}
