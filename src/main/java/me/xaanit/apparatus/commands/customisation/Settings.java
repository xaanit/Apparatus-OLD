package me.xaanit.apparatus.commands.customisation;

import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

import static me.xaanit.apparatus.util.Util.*;

public class Settings implements ICommand {
    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "gs"};
    }

    @Override
    public CmdType getType() {
        return CmdType.CUSTOMISATION;
    }

    @Override
    public Permissions getUserPerm() {
        return Permissions.MANAGE_SERVER;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " [info/help]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Shows or edits guild settings.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        if (args.length == 1 || (args.length >= 2 && args[1].equalsIgnoreCase("check"))) {
            EmbedBuilder em = new EmbedBuilder();
            em.withAuthorIcon(guild.getIconURL());
            em.withAuthorName(guild.getName() + " - Settings");
            em.withColor(hexToColor(CColors.BASIC));
            em.withTitle(guild.getStringID());
            em.appendField("Is whitelisted", getGuild(guild).whitelistedGuild + "", true);
            em.appendField("Dev override", getGuild(guild).isDevOverride() + "", true);
            em.appendField("Send Crash Reports", getGuild(guild).isCrashReports() + "", true);
            em.appendField("Welcome/Goodbye", "Your welcome message is: " + (getGuild(guild).isWelcomeOn() ? "Enable" : "Disable") + "d.\nYour goodbye message is: " + (getGuild(guild).isGoodbyeOn() ? "Enable" : "Disable") + "d.", false);
            em.appendField("Autorole", "You have a total of [ " + getGuild(guild).getAutoRoles().size() + " ] auto role(s)\n[ " + getGuild(guild).getAutoRoles().stream().filter(a -> a.isBot()).count() + " ] are bot Autoroles\n[ " + getGuild(guild).getAutoRoles().stream().filter(a -> !a.isBot()).count() + "] are user Autoroles.", false);
            em.appendField("Prefix", getGuild(guild).getPrefix(), false);
            em.withFooterIcon(user.getAvatarURL());
            em.withFooterText("Requested By: " + getNameAndDescrim(user));
            sendMessage(channel, em.build());
        }
    }
}
