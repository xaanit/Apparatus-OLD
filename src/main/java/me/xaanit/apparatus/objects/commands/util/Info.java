package me.xaanit.apparatus.objects.commands.util;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.BotInviteBuilder;
import sx.blah.discord.util.EmbedBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.xaanit.apparatus.util.Util.*;

/**
 * Created by Jacob on 5/15/2017.
 */
public class Info implements ICommand {
    public static String invite = "";
    int totalCommands = -1;

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
    }

    @Override
    public CmdType getType() {
        return CmdType.UTIL;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " [arg]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Gets info on the server, a role, a user, or the bot!";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);
        if (args.length == 1) {
            moduleHelp(user, channel, guild);
            return;
        }
        IMessage m = null;
        switch (args[1].toLowerCase()) {
            case "{channel}":
                m = moduleChannel(user, channel, guild, channel);
                break;
            case "{guild}":
                m = moduleGuild(user, channel, guild);
                break;
            case "{me}":
                m = moduleUser(user, channel, guild, user);
                break;
            case "{bot}":
                m = moduleBot(user, channel, guild);
                break;
            case "{dev}":
                m = moduleDev(user, guild, channel);
                break;
        }

        if (m != null) {
            return;
        }
        String look = combineArgs(args, 1, -1);
        IUser u = getUser(look, message, guild);
        IRole r = getRole(look, message, guild);
        IChannel c = getChannel(look, message);
        if (u != null) {
            m = moduleUser(user, channel, guild, u);
        } else if (r != null) {
            m = moduleRole(user, channel, guild, r);
        } else if (c != null) {
            if (c.getUsersHere().contains(user)) {
                m = moduleChannel(user, channel, guild, c);
            }
        }

        if (m != null) {
            return;
        }
        moduleHelp(user, channel, guild);
    }

    private IMessage moduleHelp(IUser user, IChannel channel, IGuild guild) {
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(hexToColor("249999"));
        em.withAuthorIcon(guild.getIconURL());
        em.withAuthorName("Info | Help");
        em.withDesc(
                "Hey there! This was triggered because either you were **missing arguments** or **I couldn't find what you requested**. If this is a bug please report in the bugs channel on the Support server.\n");
        em.appendField("What can I do with this command?",
                "Well info is a command to get you... well info about some stuff! There's a few ways to do this.\n"
                        +
                        "-> You can use the ID of the role, channel, user, or message you're looking for! (note, messages NEED ids)\n"
                        +
                        "-> You can use a partial name, for finding info about the owner you can use `" + guild
                        .getOwner().getName().substring(0, guild.getOwner().getName().length() / 2) + " `\n" +
                        "-> You can use the exact name\n" +
                        "-> You can use keywords to bypass it all!", false);
        em.appendField("You.. You mentioned keywords?",
                "Yep! There are a number of keywords you can use!\n-> `{channel}` - To get the info about the channel you ran the command in.\n"
                        +
                        "-> `{guild}` - To get info about the guild you're in!\n" +
                        "-> `{me}` - Gets the info about YOU!\n" +
                        "-> `{bot}` - Gets some info about me!\n" +
                        "-> {dev} - Gets some info on the dev!", false);

        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText(
                "Requested by: " + getNameAndDescrim(user));
        return sendMessage(channel, em.build());

    }

    private IMessage moduleDev(IUser user, IGuild guild, IChannel channel) {
        EmbedBuilder em = new EmbedBuilder();
        IUser xaanit = GlobalVars.client.getUserByID(Long.parseUnsignedLong("233611560545812480"));
        em.withAuthorIcon(xaanit.getAvatarURL());
        em.withAuthorName("The Developer");
        em.withDesc("Hey there! I'm a Java developer who's been doing Java for a couple years now. I made Apparatus with one goal in mind. Make the bot" +
                " for the people who couldn't code. It's main purpose is to be super customisable.");
        em.appendField("Patreon", "You can support me [here!](https://www.patreon.com/xaanit)", false);
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText(
                "Requested by: " + getNameAndDescrim(user));
        em.withColor(hexToColor(CColors.BASIC));
        IMessage m = sendMessage(channel, em.build());
        return m;
    }

    private IMessage moduleChannel(IUser user, IChannel channel, IGuild guild, IChannel c) {
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(hexToColor("249999"));
        em.withAuthorIcon(guild.getIconURL());
        em.withAuthorName("Info | Channel");
        em.appendField("Name", c.getName(), false);
        em.appendField("ID", c.getStringID(), false);
        em.appendField("Topic", c.getTopic() == null || c.getTopic().isEmpty() ? "None set" : c.getTopic(), false);
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText(
                "Requested by: " + getNameAndDescrim(user));
        return sendMessage(channel, em.build());
    }

    private IMessage moduleUser(IUser user, IChannel channel, IGuild guild, IUser u) {
        EmbedBuilder em = new EmbedBuilder();
        em.withAuthorIcon(u.getAvatarURL());
        em.withAuthorName(getNameAndDescrim(u));
        em.withThumbnail(u.getAvatarURL());
        em.withColor(hexToColor("249999"));
        em.appendField("ID", u.getStringID(), true);
        em.appendField("Status", formatPresence(u.getPresence()), true);
        em.appendField("Display name", u.getDisplayName(guild), false);
        LocalDateTime cDate = u.getCreationDate();
        LocalTime cTime = cDate.toLocalTime();
        em.appendField("Creation date",
                (cDate.getDayOfWeek().toString().charAt(0) + cDate.getDayOfWeek().toString().substring(1)
                        .toLowerCase())
                        + ", "
                        + (cDate.getMonth().toString().charAt(0)
                        + cDate.getMonth().toString().substring(1).toLowerCase())
                        + " " + cDate.getDayOfMonth() + " " + cDate.getYear() + " | "
                        + (cTime.getHour() > 12 ? cTime.getHour() - 12 : cTime) + ":" + cTime.getMinute() + ":"
                        + (cTime.getSecond() < 10 ? "0" + cTime.getSecond() : cTime.getSecond()) + (
                        cTime.getHour() > 12 ? " PM" : " AM"),
                false);
        em.appendField("Roles [" + (u.getRolesForGuild(guild).size() - 1) + "]",
                compactRoles(u.getRolesForGuild(guild)), true);
        em.appendField("Is bot?", u.isBot() + "", true);
        em.appendField("Shared servers on this shard", guild.getShard().getGuilds().stream().filter(
                g -> g.getUsers().stream().anyMatch(ur -> ur.getStringID().equalsIgnoreCase(u.getStringID()))).count() + "", false);
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText(
                "Requested by: " + getNameAndDescrim(user));
        return sendMessage(channel, em.build());
    }

    private IMessage moduleBot(IUser user, IChannel channel, IGuild guild) {
        if (invite.isEmpty()) {
            BotInviteBuilder builder = new BotInviteBuilder(GlobalVars.client);
            builder.withClientID(GlobalVars.client.getOurUser().getStringID());
            builder.withPermissions(makePermissions(basicPermissions(), Permissions.BAN, Permissions.KICK, Permissions.CHANGE_NICKNAME, Permissions.USE_EXTERNAL_EMOJIS));
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
        em.withColor(hexToColor(CColors.BASIC));
        em.withAuthorName("Bot info");
        em.withAuthorIcon(botAva());
        em.withDesc("Hey there! I'm Apparatus. I'm made by <@!233611560545812480>.\n\nMy version is **" + GlobalVars.VERISON + "**\nI run on [Discord4J, version 2.8.1](https://github.com/austinv11/Discord4J)\n\nI am on " + GlobalVars.client.getGuilds().size() + " guild(s).\nI serve " + GlobalVars.client.getUsers().size() + " user(s).\nIn " + channels + " channel(s).\nI have a total of " + totalCommands + " command(s).\n\nYou can invite me with [this](" + invite + "}) link [BROKEN ATM]\nYou can join my support server [here.](https://discord.gg/SHTbdnJ)");
        em.appendField("Special Thanks", "I have gotten so much inspiration from Tatsumaki. Thank you.", false);
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested by: " + getNameAndDescrim(user));
        IMessage m = sendMessage(channel, em.build());
        return m;
    }

    private IMessage moduleGuild(IUser user, IChannel channel, IGuild guild) {
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(hexToColor("249999"));
        em.withThumbnail(guild.getIconURL());
        em.withAuthorName(guild.getName());
        em.withAuthorIcon(guild.getIconURL());
        em.appendField("ID", guild.getStringID(), true);
        em.appendField("Owner",
                getNameAndDescrim(guild.getOwner()) + " ("
                        + guild.getOwner().getStringID() + ") " + "[" + guild.getOwner().getDisplayName(guild)
                        + "]", true);
        LocalDateTime cDate = guild.getCreationDate();
        LocalTime cTime = cDate.toLocalTime();
        em.appendField("Creation date",
                (cDate.getDayOfWeek().toString().charAt(0) + cDate.getDayOfWeek().toString().substring(1)
                        .toLowerCase())
                        + ", "
                        + (cDate.getMonth().toString().charAt(0)
                        + cDate.getMonth().toString().substring(1).toLowerCase())
                        + " " + cDate.getDayOfMonth() + " " + cDate.getYear() + " | "
                        + (cTime.getHour() > 12 ? cTime.getHour() - 12 : cTime) + ":" + cTime.getMinute() + ":"
                        + cTime.getSecond() + (cTime.getHour() > 12 ? " PM" : " AM"),
                true);
        em.appendField(
                "Channels [" + (guild.getChannels().size() + guild.getVoiceChannels().size()) + "]",
                "Text: " + guild.getChannels().size() + "\nVoice: "
                        + guild.getVoiceChannels().size(),
                true);
        List<IRole> roleList = guild.getRoles().stream()
                .filter(r -> !r.isManaged() && !r.isEveryoneRole())
                .collect(
                        Collectors.toList());
        roleList.get(0).getPosition();
        String roles = formatRoleList(roleList);

        em.appendField("Roles [" + roles.split("(?:,\\s)+").length + "]", roles, true);
        em.appendField("Users [" + guild.getUsers().size() + "]", "Online: " + guild.getUsers().stream()
                .filter(u -> u.getPresence().getStatus() == StatusType.ONLINE).count() + "\nDND: " + guild
                .getUsers().stream().filter(u -> u.getPresence().getStatus() == StatusType.DND).count()
                + "\nIdle: " + guild.getUsers().stream()
                .filter(u -> u.getPresence().getStatus() == StatusType.IDLE).count() + "\nOffline: " + guild
                .getUsers().stream().filter(u -> u.getPresence().getStatus() == StatusType.OFFLINE).count()
                + "\nStreaming: " + guild.getUsers().stream()
                .filter(u -> u.getPresence().getStatus() == StatusType.STREAMING).count() + "\nUnknown: "
                + guild.getUsers().stream().filter(u -> u.getPresence().getStatus() == StatusType.UNKNOWN)
                .count(), true);
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText(
                "Requested by: " + getNameAndDescrim(user));
        return sendMessage(channel, em.build());

    }

    private IMessage moduleRole(IUser user, IChannel channel, IGuild guild, IRole role) {
        String users = "";
        for (IUser u : guild.getUsersByRole(role)) {
            users += u.getName().replace("*", "\\*").replace("~", "\\~").replace("`", "\\`")
                    .replace("_", "\\_") + ", ";
        }
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(hexToColor("249999"));
        em.withAuthorIcon(guild.getIconURL());
        em.withAuthorName(role.getName());
        em.withColor(role.getColor());
        em.withTitle(role.getStringID());
        em.appendField("Managed", role.isManaged() + "", true);
        em.appendField("Hoisted", role.isHoisted() + "", true);
        em.appendField("Mentionable", role.isMentionable() + "", true);
        em.appendField("Permissions", role.getPermissions() + "", true);
        em.appendField("Users", users.length() == 0 ? "No Users" : users.substring(0, users.length() - 2), true);
        return sendMessage(channel, em.build());


    }
}
