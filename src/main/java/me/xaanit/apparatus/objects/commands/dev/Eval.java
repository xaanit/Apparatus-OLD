package me.xaanit.apparatus.objects.commands.dev;

import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.*;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;
import java.util.Random;

import static me.xaanit.apparatus.util.Util.*;

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
    private Random random = new Random();

    {
        factory.put("commands", commands);
        factory.put("guilds", guilds);
        factory.put("gson", gson);
        factory.put("util", util);
        factory.put("userUtil", userUtil);
        factory.put("roleUtil", roleUtil);
        factory.put("permUtil", permUtil);
        factory.put("messageUtil", messageUtil);
        factory.put("guildUtil", guildUtil);
        factory.put("embedUtil", embedUtil);
        factory.put("channelUtil", channelUtil);
        factory.put("config", config);
        factory.put("commandnames", commandNames);
        factory.put("random", random);
        factory.put("colorError", hexToColor(CColors.ERROR));
        factory.put("colorBasic", hexToColor(CColors.BASIC));
    }

    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "evaluate"};
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " <expression>"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public String getInfo() {
        return "Evaluates the given code.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        String input = message.getContent().substring((getGuild(guild).getPrefix() + "eval").length()).replaceAll("`", "");
        Object o = null;
        factory.put("guild", guild);
        factory.put("channel", channel);
        factory.put("user", user);
        factory.put("message", message);
        factory.put("command", this);
        factory.put("client", client);
        factory.put("currGuild", getGuild(guild));
        factory.put("builder", new EmbedBuilder());
        factory.put("cUser", client.getOurUser());



        try {
            String imports = "var imports = new JavaImporter(java.io, java.lang, java.util, \"Packages.sx.blah.discord.handle.obj\", \"Packages.sx.blah.discord.util\", \"Packages.me.xaanit.apparatus.util\");";
            factory.eval(imports);
            o = factory.eval(input);
        } catch (Exception ex) {
            EmbedBuilder em = new EmbedBuilder();
            em.withAuthorIcon(botAva());
            em.withAuthorName("Error");
            em.withColor(hexToColor(CColors.ERROR));
            em.withDesc(ex.getMessage());
            em.withFooterText("Eval failed");
            sendMessage(channel, em.build());
            return;
        }

        EmbedBuilder em = new EmbedBuilder();
        em.withAuthorIcon(botAva());
        em.withAuthorName("Success!");
        em.withColor(hexToColor(CColors.BASIC));
        em.withTitle("Evaluation output.");
        em.withDesc(o == null ? "No output, object is null" : o.toString());
        em.appendField("Input", "```java\n" + input + "\n```", false);
        em.withFooterText("Eval successful!");
        sendMessage(channel, em.build());
    }
}
