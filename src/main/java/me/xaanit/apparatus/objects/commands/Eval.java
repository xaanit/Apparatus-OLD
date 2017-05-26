package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.*;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;

/**
 * Created by Jacob on 5/15/2017.
 */
public class Eval implements ICommand {

    private ScriptEngine factory = new ScriptEngineManager().getEngineByName("nashorn");
    private Util util = new Util();
    private UserUtil userUtil = new UserUtil();
    private RoleUtil roleUtil = new RoleUtil();
    private PermissionsUtil permUtil = new PermissionsUtil();
    private MessageUtil messageUtil = new MessageUtil();
    private GuildUtil guildUtil = new GuildUtil();
    private EmbedUtil embedUtil = new EmbedUtil();
    private ChannelUtil channelUtil = new ChannelUtil();

    @Override
    public String getName() {
        RequestBuffer.request(() -> GlobalVars.client.streaming("@Apparatus prefix | " + GlobalVars.client.getGuilds().size() + " guild(s)", "https://www.twitch.tv/awdawdadwwd"));
        return "eval";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "evaluate"};
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{GuildUtil.getGuild(guild).getPrefix(), getName() + " <expression>"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Evaluates the given code.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.allChecks(user, guild, this, channel);

        String input = message.getContent().substring((Util.getGuild(guild).getPrefix() + "eval").length()).replaceAll("`", "");
        Object o = null;
        factory.put("guild", guild);
        factory.put("channel", channel);
        factory.put("commands", GlobalVars.commands);
        factory.put("guilds", GlobalVars.guilds);
        factory.put("gson", GlobalVars.gson);
        factory.put("util", util);
        factory.put("userUtil", userUtil);
        factory.put("roleUtil", roleUtil);
        factory.put("permUtil", permUtil);
        factory.put("messageUtil", messageUtil);
        factory.put("guildUtil", guildUtil);
        factory.put("embedUtil", embedUtil);
        factory.put("channelUtil", channelUtil);
        factory.put("users", GlobalVars.users);
        factory.put("user", user);
        factory.put("message", message);
        factory.put("command", this);
        factory.put("client", GlobalVars.client);
        factory.put("commandnames", GlobalVars.commandNames);

        try {
            String imports = "var imports = new JavaImporter(java.io, java.lang, java.util, \"Packages.sx.blah.discord.handle.obj\", \"Packages.sx.blah.discord.util\", \"Packages.me.xaanit.apparatus.util\");";
            factory.eval(imports);
            o = factory.eval(input);
        } catch (Exception ex) {
            EmbedBuilder em = new EmbedBuilder();
            em.withAuthorIcon(Util.botAva());
            em.withAuthorName("Error");
            em.withColor(Util.hexToColor(CColors.ERROR));
            em.withDesc(ex.getMessage());
            em.withFooterText("Eval failed");
            Util.sendMessage(channel, em.build());
            return;
        }
        EmbedBuilder em = new EmbedBuilder();
        em.withAuthorIcon(Util.botAva());
        em.withAuthorName("Success!");
        em.withColor(Util.hexToColor(CColors.BASIC));
        em.withTitle("Evaluation output.");
        em.withDesc(o == null ? "No output, object is null" : o.toString());
        em.appendField("Input", "```java\n" + input + "\n```", false);
        em.withFooterText("Eval successful!");
        Util.sendMessage(channel, em.build());
    }
}
