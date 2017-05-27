package me.xaanit.apparatus.objects.commands;

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
 * Created by Jacob on 5/26/2017.
 */
public class Credits implements ICommand {

    private EmbedObject info = null;

    @Override
    public String getName() {
        return "credits";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
    }

    @Override
    public CmdType getType() {
        return CmdType.FUN;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{GuildUtil.getGuild(guild).getPrefix(), getName()}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Gives some credits for the bot!";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.allChecks(user, guild, this, channel);
        if (info == null) {
            EmbedBuilder em = new EmbedBuilder();
            em.withAuthorIcon(Util.botAva());
            em.withAuthorName("Credits");
            em.withColor(Util.hexToColor(CColors.BASIC));
            em.withDesc("Buyable roles descriptions and features taken (with permission) from [Adopt Shop Cafe](https://discord.gg/4rF5wq).\nReflections taken (with permissions) from Arraying (Maker of Arraybot & Copycat)");
            this.info = em.build();
        }
        Util.sendMessage(channel, info);
    }
}
