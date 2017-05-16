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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;

/**
 * Created by Jacob on 5/15/2017.
 */
public class Eval implements ICommand {

    private ScriptEngine factory = new ScriptEngineManager().getEngineByName("nashorn");

    @Override
    public String getName() {
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
        try {
            o = factory.eval(input);
        } catch (Exception ex) {
            EmbedBuilder em = new EmbedBuilder();
            em.withAuthorIcon(GlobalVars.client.getOurUser().getAvatarURL());
            em.withAuthorName("Error");
            em.withColor(Util.hexToColor(CColors.ERROR));
            em.withDesc(ex.getMessage());
            em.withFooterText("Eval failed");
            Util.sendMessage(channel, em.build());
            return;
        }
        EmbedBuilder em = new EmbedBuilder();
        em.withAuthorIcon(GlobalVars.client.getOurUser().getAvatarURL());
        em.withAuthorName("Success!");
        em.withColor(Util.hexToColor(CColors.BASIC));
        em.withTitle("Evaluation output.");
        em.withDesc(o == null ? "No output, object is null" : o.toString());
        em.appendField("Input", "```kotlin\n" + input + "\n```", false);
        em.withFooterText("Eval successful!");
        Util.sendMessage(channel, em.build());
    }
}
