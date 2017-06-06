package me.xaanit.apparatus.objects.commands.secret;

import me.xaanit.apparatus.internal.json.embeds.CustomEmbed;
import me.xaanit.apparatus.internal.json.embeds.Field;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import static me.xaanit.apparatus.util.Util.*;

public class Test implements ICommand {
    @Override
    public String getName() {
        return "test";
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
    public String getInfo() {
        return "You can't see this";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);
        CustomEmbed c = customEmbedParser(user, guild, channel, message, combineArgs(args, 1, args.length));
        EmbedBuilder em = new EmbedBuilder();
        if (!c.getColorHex().isEmpty())
            em.withColor(hexToColor(c.getColorHex()));
        if (!c.getAuthorIcon().isEmpty())
            em.withAuthorIcon(c.getAuthorIcon());
        if (!c.getAuthorName().isEmpty())
            em.withAuthorName(c.getAuthorName());
        if (!c.getAuthorURL().isEmpty())
            em.withAuthorUrl(c.getAuthorURL());
        if (!c.getThumbnail().isEmpty())
            em.withThumbnail(c.getThumbnail());
        if (!c.getTitle().isEmpty())
            em.withTitle(c.getTitle());
        if (!c.getTitleURL().isEmpty())
            em.withUrl(c.getTitleURL());
        if (!c.getDesc().isEmpty())
            em.withDesc(c.getDesc());
        for (Field f : c.getFields())
            em.appendField(f.getFieldTitle(), f.getFieldValue(), f.isInline());
        if (!c.getImage().isEmpty())
            em.withImage(c.getImage());
        if (!c.getFooterIcon().isEmpty())
            em.withFooterIcon(c.getFooterIcon());
        if (!c.getFooterText().isEmpty())
            em.withFooterText(c.getFooterText());
        if (c.isIncludeTimestamp())
            em.withFooterText((c.getFooterText().isEmpty() ? "" : " | ") + getCurrentTime());

        sendMessage(channel, em.build());
    }
}
