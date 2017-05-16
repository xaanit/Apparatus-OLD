package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.GuildUtil;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.BotInviteBuilder;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jacob on 5/15/2017.
 */
public class Botinfo implements ICommand {
    private String invite = "";
    int totalCommands = -1;

    @Override
    public String getName() {
        return "botinfo";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
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
        return "Gets info on the bot";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.allChecks(user, guild, this, channel);
        if (invite.isEmpty()) {
            BotInviteBuilder builder = new BotInviteBuilder(GlobalVars.client);
            builder.withClientID(GlobalVars.client.getOurUser().getStringID());
            builder.withPermissions(Util.makePermissions(Util.basicPermissions(), Permissions.BAN, Permissions.KICK, Permissions.CHANGE_NICKNAME));
            invite = builder.build();
        }

        if (totalCommands == -1) {
            List<ICommand> commandsFound = new ArrayList<>();
            for (String key : GlobalVars.commands.keySet()) {
                ICommand command = GlobalVars.commands.get(key);
                if (commandsFound.stream().filter(c -> c.getName().equals(command.getName())).count() == 0)
                    commandsFound.add(command);
            }
            totalCommands = commandsFound.size();
        }
        int channels = 0;
        for (IGuild g : GlobalVars.client.getGuilds()) {
            channels += g.getChannels().size();
        }
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(Util.hexToColor(CColors.BASIC));
        em.withAuthorName("Bot info");
        em.withAuthorIcon(GlobalVars.client.getOurUser().getAvatarURL());
        em.withDesc("Hey there! I'm Apparatus. I'm made by <@!233611560545812480>.\n\nMy version is **" + GlobalVars.VERISON + "**\nI run on [Discord4J, version 2.8.1](https://github.com/austinv11/Discord4J)\n\nI am on " + GlobalVars.client.getGuilds().size() + " guild(s).\nI serve " + GlobalVars.client.getUsers().size() + " user(s).\nIn " + channels + " channel(s).\nI have a total of " + totalCommands + " command(s).\n\nYou can invite me with [this](" + invite + "}) link [BROKEN ATM]\nYou can join my support server [here.](https://discord.gg/SHTbdnJ)");
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested by: " + Util.getNameAndDescrim(user));
        Util.sendMessage(channel, em.build());
    }
}
