package me.xaanit.apparatus.commands.customisation;

import me.xaanit.apparatus.api.outside.Requests;
import me.xaanit.apparatus.internal.json.JsonModlog;
import me.xaanit.apparatus.internal.json.embeds.CustomEmbed;
import me.xaanit.apparatus.internal.json.embeds.Field;
import me.xaanit.apparatus.objects.PlaceInCommand;
import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static me.xaanit.apparatus.listeners.GuildCreateListener.MOD_LOGS;
import static me.xaanit.apparatus.util.Util.*;

/**
 * If you are reading this class.
 * Stop now.
 * It is a mess.
 * Fuck menus.
 * Seriously.
 * Fuck them.
 * I hate them.
 * Continue at your own risk.
 */
@SuppressWarnings("all")
public class Modlog implements ICommand {
    private static Map<Long, PlaceInCommand> map = new HashMap<>();
    private static final String MODLOG_WIKI = "https://github.com/NegotiumBots/ApparatusWiki/wiki/";

    @Override
    public String getName() {
        return "modlog";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
    }

    @Override
    public CmdType getType() {
        return CmdType.CUSTOMISATION;
    }

    @Override
    public PlaceInCommand getPlaceInCommand(IUser user) {
        return map.containsKey(user.getLongID()) ? map.get(user.getLongID()) : null;
    }

    @Override
    public Permissions getUserPerm() {
        return Permissions.MANAGE_SERVER;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " <list/edit/check> [modlog]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Lists the modlogs, or edits one.";
    }


    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("list"))) {
            EmbedBuilder em = basicEmbed(user, "Modlog list", CColors.BASIC);
            em.withDesc("Avaliable modlogs: **" + MOD_LOGS + "**\n\n\nModlogs you don't have set up: **" + (MOD_LOGS.stream().filter(n -> getGuild(guild).getModlog(n).getChannels().isEmpty()).collect(Collectors.toList())) + "**");
            sendMessage(channel, em.build());
            return;
        }

        if (args[1].equalsIgnoreCase("edit")) {
            if (args.length == 2) {
                EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
                em.withDesc("Missing a modlog to edit!");
                sendMessage(channel, em.build());
                return;
            }

            if (!MOD_LOGS.contains(args[2])) {
                EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
                em.withDesc("Modlog is invalid!");
                sendMessage(channel, em.build());
                return;
            }
            PlaceInCommand command = new PlaceInCommand();
            command.place = 0;
            command.tempObjs.put("modlog", args[2]);
            command.currChannel = channel;
            for (ICommand c : commands.values())
                if (c.getMap() != null && c.getMap().containsKey(user.getLongID()))
                    c.getMap().remove(user.getLongID());
            map.put(user.getLongID(), command);
            EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
            em.withDesc("Please remember! You can stop at any time by typing `cancel` (will not save values) or `exit` (will save values)!");
            sendMessage(channel, em.build());
            place(user, message, false);

        }

        if (args[1].equalsIgnoreCase("check")) {
            if (args.length == 2) {
                EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
                em.withDesc("Missing a modlog to check!");
                sendMessage(channel, em.build());
                return;
            }

            if (!MOD_LOGS.contains(args[2])) {
                EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
                em.withDesc("Modlog is invalid!");
                sendMessage(channel, em.build());
                return;
            }


            EmbedBuilder em1 = basicEmbed(user, "Check " + args[2], CColors.BASIC);
            em1.withDesc("Description of the given modlog:");

            JsonModlog m = getGuild(guild).getModlog(args[2].toLowerCase());

            em1.appendField("Channels activated", getChannelList(m.getChannels(), message).isEmpty() ? "None" : getChannelList(m.getChannels(), message), false);

            em1.appendField("Logging", "Uses strings? **" + (!m.isUseEmbed()) + "**\nUses the deafult? **" + m.isUseDefault() + "**\nHas a custom String? **" + !m.getStringLog().isEmpty() + "**\nHas a custom embed? **" + !m.getEmbed().isEmpty() + "**", false);

            channel.sendMessage(em1.build());


        }
    }

    public static void place(IUser user, IMessage message) {
        place(user, message, false);
    }

    private static void place(IUser user, IMessage message, boolean firstTime) {
        PlaceInCommand command = map.get(user.getLongID());
        if (command.currChannel.getLongID() != message.getChannel().getLongID()) return;
        if (message.getContent().equalsIgnoreCase("cancel") || message.getContent().equalsIgnoreCase("exit")) {
            EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog") + " - Exit", CColors.BASIC);
            em.withDesc("Exiting modlog edit! " + (message.getContent().equalsIgnoreCase("exit") ? "Saved all values!" : ""));
            sendMessage(command.currChannel, em.build());
            map.remove(user.getLongID());
            if (message.getContent().equalsIgnoreCase("cancel")) return;
            saveModlog(command);
            return;
        }

        switch (command.place) {
            case 0:
                channelCheck(user, message);
                break;
            case 1:
                channelPick(user, message);
                break;
            case 2:
            case 3:
                channelNotif(user, message);
                break;
            case 4:
                command.place = 7;
                map.put(user.getLongID(), command);
                place(user, message, true);
                break;
            case 5:
                channelChange(user, message, "add");
                break;
            case 6:
                channelChange(user, message, "remove");
                break;
            case 7:
                useDefualtNotif(user, message);
                break;
            case 8:
                useDefaultPick(user, message);
                break;
            case 9:
                useDefaultSet(user, message, true);
                break;
            case 10:
                useDefaultSet(user, message, false);
                break;
            case 11:
                setCustomStringNotif(user, message);
                break;
            case 12:
                setCustomStringPick(user, message);
                break;
            case 13:
                setCustomStringSetNotif(user, message);
                break;
            case 14:
                setCustomStringSetListen(user, message);
                break;
            case 15:
                setCustomStringSetNotif(user, message);
                break;
            case 16:
                setCustomEmbedAsk(user, message);
                break;
            case 17:
                setCustomEmbedChoiceListen(user, message);
                break;
            case 18:
                setCustomEmbedAskToSet(user, message);
                break;
            case 19:
                setCustomEmbedListenForSet(user, message);
                break;
            case 20:
                setCustomEmbedSetConfirm(user, message);
                break;
            case 21:
                done(user, message);
                break;

        }
    }


    private static void saveModlog(PlaceInCommand command) {
        IMessage message = command.lastMessage;
        if (command.tempObjs.containsKey("channels")) {
            for (IChannel c : (List<IChannel>) command.tempObjs.get("channels")) {
                if (!c.isDeleted()) {
                    if (command.tempObjs.get("channeltype").toString().equals("add"))
                        getGuild(message.getGuild()).getModlog(command.tempObjs.get("modlog").toString()).addChannel(c.getLongID());
                    else
                        getGuild(message.getGuild()).getModlog(command.tempObjs.get("modlog").toString()).removeChannel(c.getLongID());

                }
            }
        }


        if (command.tempObjs.containsKey("usedefault")) {
            getGuild(message.getGuild()).getModlog(command.tempObjs.get("modlog").toString()).setUseDefault(Boolean.valueOf(command.tempObjs.get("usedefault").toString()));
        }

        if (command.tempObjs.containsKey("stringcustom")) {
            getGuild(message.getGuild()).getModlog(command.tempObjs.get("modlog").toString()).setStringLog(command.tempObjs.get("stringcustom").toString());
        }

        if (command.tempObjs.containsKey("embedcustom")) {
            getGuild(message.getGuild()).getModlog(command.tempObjs.get("modlog").toString()).embed = (CustomEmbed) command.tempObjs.get("embedcustom");
        }

        if (command.tempObjs.containsKey("useembed")) {
            getGuild(message.getGuild()).getModlog(command.tempObjs.get("modlog").toString()).setUseEmbed(Boolean.valueOf(command.tempObjs.get("useembed").toString()));
        }
    }

    private static void channelCheck(IUser user, IMessage message) {
        // 0
        PlaceInCommand command = map.get(user.getLongID());
        IChannel channel = command.currChannel;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withFooterText("Requested by: " + getNameAndDescrim(user) + " | Use 'exit' or 'cancel' to exit at any time.");
        em.withDesc("Please enter in what you'd like to do:\n1) Add a channel\n2) Remove a channel\n3) Skip");
        command.lastMessage = sendMessage(channel, em.build());
        command.place = 1;
        map.put(user.getLongID(), command);
    }

    private static void channelPick(IUser user, IMessage message) {
        // 1
        PlaceInCommand command = map.get(user.getLongID());
        int choice;
        try {
            choice = Integer.parseInt(message.getContent());
            if (choice < 0 || choice > 3)
                choice = 3;
        } catch (NumberFormatException ex) {
            choice = 3;
        }

        command.place += choice;
        // 2 = add
        // 3 = remove
        // 4 skip
        map.put(user.getLongID(), command);
        place(user, message, true);
    }

    private static void channelNotif(IUser user, IMessage message) {
        // 2 || 3
        PlaceInCommand command = map.get(user.getLongID());
        IChannel channel = command.currChannel;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("Please say the channel(s) you'd like to " + (command.place == 2 ? "add" : "remove") + ".");
        command.lastMessage = sendMessage(channel, em.build());
        command.place = command.place == 2 ? 5 : 6;
        map.put(user.getLongID(), command);
    }

    private static void channelChange(IUser user, IMessage message, String todo) {
        // 5 "add"
        // 6 "remove"
        PlaceInCommand command = map.get(user.getLongID());
        List<IChannel> channels = new ArrayList<>();
        String[] args = message.getContent().split("\\s+");
        for (String str : args) {
            System.out.println(str);
            IChannel c = getChannel(str, message);
            if (c == null) continue;
            if (channels.stream().filter(c1 -> c.getName().equalsIgnoreCase(c1.getName())).count() == 1) continue;
            channels.add(c);
        }
        command.tempObjs.put("channeltype", todo.toLowerCase());
        command.tempObjs.put("channels", channels);
        command.place = 7;
        IChannel c = command.currChannel;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("You have " + (todo.equalsIgnoreCase("add") ? "added" : "removed") + " the following channels:\n" + command.tempObjs.get("channels"));
        command.lastMessage = sendMessage(c, em.build());
        map.put(user.getLongID(), command);
        place(user, message, true);
    }

    private static void useDefualtNotif(IUser user, IMessage message) {
        // 7
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("Would you like to use the default messages?\n1) Yes\n2) No\n3) Skip");
        command.lastMessage = sendMessage(c, em.build());
        command.place = 8;
        map.put(user.getLongID(), command);
    }

    private static void useDefaultPick(IUser user, IMessage message) {
        // 8
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        int choice;
        try {
            choice = Integer.parseInt(message.getContent());
            if (choice < 0 || choice > 3)
                choice = 3;
        } catch (NumberFormatException ex) {
            choice = 3;
        }
        command.place += choice;
        // 9 = yes
        // 10 = no
        // 11 = skip
        map.put(user.getLongID(), command);
        place(user, message, true);
    }

    private static void useDefaultSet(IUser user, IMessage message, boolean type) {
        // 9 true
        // 10 false
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        command.tempObjs.put("usedefault", type);
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("You are " + (type ? "now using " : "no longer using ") + " the default messaages.");
        command.lastMessage = sendMessage(c, em.build());
        command.place = 11;
        map.put(user.getLongID(), command);
        place(user, message, true);
    }

    private static void setCustomStringNotif(IUser user, IMessage message) {
        // 11
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("Would you like to set a custom string?\n1) Yes\n2) No");
        command.lastMessage = sendMessage(c, em.build());
        command.place = 12;
        map.put(user.getLongID(), command);
    }

    private static void setCustomStringPick(IUser user, IMessage message) {
        // 12
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        int choice;
        try {
            choice = Integer.parseInt(message.getContent());
            if (choice < 0 || choice > 2)
                choice = 2;
        } catch (NumberFormatException ex) {
            choice = 2;
        }

        if (choice == 1) command.place = 13;
        else command.place = 16;
        map.put(user.getLongID(), command);
        place(user, message, true);
    }


    private static void setCustomStringSetNotif(IUser user, IMessage message) {
        // 13
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("Please set a custom string. To see all variables please refer to [this link.](" + (MODLOG_WIKI + command.tempObjs.get("modlog").toString()) + ")");
        command.lastMessage = sendMessage(c, em.build());
        command.place = 14;
        map.put(user.getLongID(), command);
    }

    private static void setCustomStringSetListen(IUser user, IMessage message) {
        // 14
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        command.place = 15;
        command.tempObjs.put("stringcustom", message.getContent());
        map.put(user.getLongID(), command);
        place(user, message);
    }

    private static void setCustomStringSetConfirmedNotif(IUser user, IMessage message) {
        // 15
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        command.place = 16;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("Custom string set to: ```\n" + command.tempObjs.get("stringcustom").toString() + "\n```");
        command.lastMessage = sendMessage(c, em.build());
        map.put(user.getLongID(), command);
        place(user, message, true);
    }

    private static void setCustomEmbedAsk(IUser user, IMessage message) {
        //16
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("Would you like to set a custom embed?\n1) Yes\n2) No");
        command.place = 17;
        command.lastMessage = sendMessage(c, em.build());
        map.put(user.getLongID(), command);
    }

    private static void setCustomEmbedChoiceListen(IUser user, IMessage message) {
        // 17
        PlaceInCommand command = map.get(user.getLongID());
        int choice;
        try {
            choice = Integer.parseInt(message.getContent());
            if (choice < 0 || choice > 2)
                choice = 2;
        } catch (NumberFormatException ex) {
            choice = 2;
        }
        if (choice == 1) command.place = 18;
        else command.place = 21;
        map.put(user.getLongID(), command);
        place(user, message);
    }

    private static void setCustomEmbedAskToSet(IUser user, IMessage message) {
        // 18
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("Please input your custom embed.");
        command.place = 19;
        command.lastMessage = sendMessage(c, em.build());
        map.put(user.getLongID(), command);
    }

    private static void setCustomEmbedListenForSet(IUser user, IMessage message) {
        // 19
        PlaceInCommand command = map.get(user.getLongID());
        CustomEmbed c = customEmbedParser(message.getContent());
        command.tempObjs.put("embedcustom", c);
        command.place = 20;
        map.put(user.getLongID(), command);
        place(user, message);
    }

    private static void setCustomEmbedSetConfirm(IUser user, IMessage message) {
        // 20
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("You have set a custom embed. This is a preview:");
        command.lastMessage = sendMessage(c, em.build());
        command.place = 21;
        sendMessage(c, preview((CustomEmbed) command.tempObjs.get("embedcustom")));
        map.put(user.getLongID(), command);
        place(user, message);
    }

    private static void done(IUser user, IMessage message) {
        // 21
        PlaceInCommand command = map.get(user.getLongID());
        IChannel c = command.currChannel;
        EmbedBuilder em = basicEmbed(user, "Edit " + command.tempObjs.get("modlog"), CColors.BASIC);
        em.withDesc("Editing done! Saving all values now.");
        IMessage m = sendMessage(c, em.build());
        saveModlog(command);
        em.withDesc("Values saved! Goodbye!");
        editMessage(m, em.build());
        map.remove(user.getLongID());
    }


    private static EmbedObject preview(CustomEmbed c) {
        EmbedBuilder em = new EmbedBuilder();
        if (!c.getColorHex().isEmpty())
            em.withColor(hexToColor(convert(c.getColorHex())));
        if (!c.getAuthorIcon().isEmpty() && !isVar(c.getAuthorIcon()))
            em.withAuthorIcon(convert(c.getAuthorIcon()));
        if (!c.getAuthorName().isEmpty())
            em.withAuthorName(convert(c.getAuthorName()));
        if (!c.getAuthorURL().isEmpty())
            em.withAuthorUrl(convert(c.getAuthorURL()));
        if (!c.getThumbnail().isEmpty() && !isVar(c.getThumbnail()))
            em.withThumbnail(convert(c.getThumbnail()));
        if (!c.getTitle().isEmpty())
            em.withTitle(convert(c.getTitle()));
        if (!c.getTitleURL().isEmpty())
            em.withUrl(convert(c.getTitleURL()));
        if (!c.getDesc().isEmpty())
            em.withDesc(convert(c.getDesc()));
        for (Field f : c.getFields())
            em.appendField(convert(f.getFieldTitle()), convert(f.getFieldValue()), f.isInline());
        if (!c.getImage().isEmpty() && !isVar(c.getImage()))
            em.withImage(convert(c.getImage()));
        if (!c.getFooterIcon().isEmpty() && !isVar(c.getFooterIcon()))
            em.withFooterIcon(convert(c.getFooterIcon()));
        if (!c.getFooterText().isEmpty())
            em.withFooterText(convert(c.getFooterText()));
        if (c.isIncludeTimestamp())
            em.withFooterText((c.getFooterText().endsWith("") ? "" : " | ") + getCurrentTime());
        return em.build();
    }

    private static String convert(String str) {
        return str.replace("[[", "{").replace("]]", "}");
    }


    private static boolean isVar(String str) {
        String s;
        try {
            s = Requests.get("https://raw.githubusercontent.com/NegotiumBots/ApparatusWiki/master/variables.md");
        } catch (IOException ex) {
            return true;
        }
        String[] split = s.split(":");
        for (String string : split) {
            String st = string.split(";")[0];
            final String REGEX = "[\\{\\}\\[\\]]";
            if (str.replaceAll(REGEX, "").equalsIgnoreCase(st.replaceAll(REGEX, "")))
                return true;
        }
        return false;
    }


    private static String getChannelList(List<Long> list, IMessage m) {
        String res = "";
        if (list.isEmpty()) return "";
        for (long l : list) {
            IChannel c = m.getGuild().getChannelByID(l);
            if (c == null) continue;
            res += c.getName() + ", ";
        }
        return res.isEmpty() ? "" : res.trim().substring(0, res.length() >= 1024 ? 1021 : res.length() - 1) + (res.length() >= 1024 ? "..." : "");
    }
}
