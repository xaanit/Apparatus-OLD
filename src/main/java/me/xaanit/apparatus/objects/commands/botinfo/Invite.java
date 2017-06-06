package me.xaanit.apparatus.objects.commands.botinfo;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.commands.util.Info;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.BotInviteBuilder;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

import static me.xaanit.apparatus.util.Util.*;

public class Invite implements ICommand {
    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "getbot", "howdoiaddyoutomyserver", "iloveyoucomejoinme"};
    }


    @Override
    public CmdType getType() {
        return CmdType.BOT_INFO;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName()}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Gets the invite links for the bot.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);
        if (Info.invite.isEmpty()) {
            BotInviteBuilder builder = new BotInviteBuilder(GlobalVars.client);
            builder.withClientID(GlobalVars.client.getOurUser().getStringID());
            builder.withPermissions(makePermissions(basicPermissions(), Permissions.BAN, Permissions.KICK, Permissions.CHANGE_NICKNAME, Permissions.USE_EXTERNAL_EMOJIS));
            Info.invite = builder.build();
        }

        sendMessage(channel, basicEmbed(user, "Invite", CColors.BASIC).withDesc("You can invite me with [this](" + Info.invite + "}) link [BROKEN ATM]\nYou can join my support server [here.](https://discord.gg/SHTbdnJ)").build());
    }
}
