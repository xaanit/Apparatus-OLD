package me.xaanit.apparatus.commands.botinfo;

import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static me.xaanit.apparatus.util.Util.*;

/**
 * Created by Jacob on 5/17/2017.
 */
public class Announcements implements ICommand {
    private IChannel announcements = null;
    private IChannel updates = null;
    private IChannel todo = null;

    @Override
    public String getName() {
        return "announcements";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "updates", "todo"};
    }

    @Override
    public CmdType getType() {
        return CmdType.BOT_INFO;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " [all]"}, new String[]{Arrays.toString(getAliases()).replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Gets the latest Announcements/Updates/TODO about Apparatus :)";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);
        while (announcements == null || updates == null || todo == null) {
            announcements = client.getChannelByID(Long.parseUnsignedLong("313745078340419585"));
            updates = client.getChannelByID(Long.parseUnsignedLong("313745091619454979"));
            todo = client.getChannelByID(Long.parseUnsignedLong("313812891939635240"));
        }
        boolean sendA = false;
        boolean sendB = false;
        boolean sendT = false;
        if (args.length > 1) {
            sendA = true;
            sendT = sendB = sendA;
        }
        if (args[0].toLowerCase().contains("announce")) sendA = true;
        else if (args[0].toLowerCase().contains("update")) sendB = true;
        else sendT = true;
        List<IMessage> am = announcements.getMessageHistoryFrom(LocalDateTime.now(), 1);
        String ams = am.isEmpty() ? "No current announcements." : am.get(0).getContent();
        List<IMessage> um = updates.getMessageHistoryFrom(LocalDateTime.now(), 1);
        String ums = um.isEmpty() ? "No current updates." : um.get(0).getContent();
        List<IMessage> tom = todo.getMessageHistoryFrom(LocalDateTime.now(), 1);
        String toms = tom.isEmpty() ? "No current TODO." : tom.get(0).getContent();
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(hexToColor(CColors.BASIC));
        em.withAuthorName("Announcements!");
        em.withAuthorIcon(botAva());
        if (sendA) em.appendField("Announcements", ams.length() > 1024 ? ams.substring(0, 1021) + "..." : ams, false);
        if (sendB) em.appendField("Updates", ums.length() > 1024 ? ums.substring(0, 1021) + "..." : ums, false);
        if (sendT) em.appendField("TODO", toms.length() > 1024 ? toms.substring(0, 1021) + "..." : toms, false);
        em.withDesc("Things cut off? Join the support server [here!](https://discord.gg/SHTbdnJ)");
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested by: " + getNameAndDescrim(user));
        sendMessage(channel, em.build());
    }
}