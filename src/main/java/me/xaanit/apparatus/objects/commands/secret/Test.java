package me.xaanit.apparatus.objects.commands.secret;

import me.xaanit.apparatus.internal.json.embeds.CustomEmbed;
import me.xaanit.apparatus.internal.json.embeds.Field;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.Update;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

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
        channel.sendMessage("UPDATE WORKED.");
        if(true) return;
        Update.execute(user, channel, message);
        if (true) return;
        //   client.getOurUser().getVoiceStateForGuild(guild).getChannel().leave();

        //  if (true) return;
        CustomEmbed c = customEmbedParser(combineArgs(args, 1, args.length));
        //sendMessage(channel, "```json\n" + gson.toJson(c) + "```");
        //if (true) return;
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


    private String getInfo(String str) {
        String res = "";
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL(str);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                res += line;
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }

        return res;
    }
}
