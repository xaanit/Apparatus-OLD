package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.GlobalVars;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{GuildUtil.getGuild(guild).getPrefix(), getName()}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Gets the latest Announcements/Updates/TODO about Apparatus :)";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.allChecks(user, guild, this, channel);

       while (announcements == null || updates == null || todo == null) {
            announcements = GlobalVars.client.getChannelByID(Long.parseUnsignedLong("313745078340419585"));
            updates = GlobalVars.client.getChannelByID(Long.parseUnsignedLong("313745091619454979"));
            todo =  GlobalVars.client.getChannelByID(Long.parseUnsignedLong("313812891939635240"));
        }

        List<IMessage> am = announcements.getMessageHistoryFrom(LocalDateTime.now(), 1);
        String ams = am.isEmpty() ? "No current announcements." : am.get(0).getContent();
        List<IMessage> um = updates.getMessageHistoryFrom(LocalDateTime.now(), 1);
        String ums = um.isEmpty() ? "No current updates." : um.get(0).getContent();
        List<IMessage> tom = todo.getMessageHistoryFrom(LocalDateTime.now(), 1);
        String toms = tom.isEmpty() ? "No current TODO." : tom.get(0).getContent();

        EmbedBuilder em = new EmbedBuilder();
        em.withColor(Util.hexToColor(CColors.BASIC));
        em.withAuthorName("Announcements!");
        em.withAuthorIcon(Util.botAva());
        em.appendField("Announcements", ams.length() > 1024 ? ams.substring(0, 1021) + "..." : ams, false);
        em.appendField("Updates", ums.length() > 1024 ? ums.substring(0, 1021) + "..." : ums, false);
        em.appendField("TODO", toms.length() > 1024 ? toms.substring(0, 1021) + "..." : toms, false);
        em.withDesc("Things cut off? Join the support server [here!](https://discord.gg/SHTbdnJ)");
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested by: " + Util.getNameAndDescrim(user));
        Util.sendMessage(channel, em.build());
    }
}
