package me.xaanit.apparatus.util;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.internal.json.embeds.CustomEmbed;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jacob on 5/15/2017.
 */
public class EmbedUtil extends ChannelUtil {

    public static final String BASIC_USAGE = "Usage: %s%s";
    public static final String BASIC_ALIAS = "Aliases: %s";

    /**
     * Returns an integer array of [R, G, B] from a provided hex
     *
     * @param hex The hex to convert.
     * @return A new int[] of R G B values
     */
    public static int[] hexToRGB(String hex) {
        hex = hex.replace("#", "");
        int r = Integer.valueOf(hex.substring(0, 2), 16);
        int g = Integer.valueOf(hex.substring(2, 4), 16);
        int b = Integer.valueOf(hex.substring(4, 6), 16);

        return new int[]{r, g, b};

    }

    /**
     * Returns a new Color object from the provided hex.
     *
     * @param hex The hex to convert
     * @return A color object.
     */
    public static Color hexToColor(String hex) {
        int[] arr = hexToRGB(hex);
        return new Color(arr[0], arr[1], arr[2]);
    }

    public static Color hexToColor(CColors color) {
        return hexToColor(color.getHex());
    }

    /**
     * Gets the basic EmbedBuilder for the help command
     *
     * @param user The user who ran
     * @return The EmbedBuilder
     */
    public static EmbedBuilder getBasicHelpEmbed(IUser user) {
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(hexToColor(CColors.BASIC));
        em.withAuthorIcon(GlobalVars.client.getApplicationIconURL());
        em.withAuthorName("Help");
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText(
                "Requested by: " + UserUtil.getNameAndDescrim(user) + " | <> = Required [] = Optional");
        return em;
    }

    public static EmbedBuilder addToHelpEmbed(ICommand command, IUser user, String[] firstFormat, String[] secondFormat) {
        EmbedBuilder em = Util.getBasicHelpEmbed(user);
        em.withDesc(command.getInfo() + "\n\n<> = Required | [] = Optional");
        em.appendField("Usage",
                String.format(Util.BASIC_USAGE, firstFormat[0], firstFormat[1]),
                false);
        em.appendField("Aliases",
                String.format(Util.BASIC_ALIAS, secondFormat[0]),
                false);
        return em;
    }

    public static EmbedObject argumentsError(IUser user) {
        EmbedBuilder em = new EmbedBuilder();
        em.withAuthorIcon(Util.botAva());
        em.withAuthorName("Error");
        em.withColor(Util.hexToColor(CColors.ERROR));
        em.withDesc("You need to provide arguments.");
        em.withFooterText("Requested by: " + Util.getNameAndDescrim(user));
        return em.build();
    }

    public static EmbedBuilder basicEmbed(IUser user, String name, CColors colors) {
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(Util.hexToColor(colors));
        em.withAuthorIcon(Util.botAva());
        em.withAuthorName(name);
        em.withFooterIcon(user.getAvatarURL());
        em.withFooterText("Requested by: " + Util.getNameAndDescrim(user));
        return em;
    }

    public static CustomEmbed customEmbedParser(IUser user, IGuild guild, IChannel channel, IMessage message, String str) {
        if (str.startsWith("http")) {
            if (!str.replaceAll("(http(s)?:\\/\\/raw\\.githubusercontent\\.com.+)", "").isEmpty())
                return null;
            else if (!str.replaceAll("(http(s)?:\\/\\/pastebin.com\\/raw.+)", "").isEmpty())
                return null;
        }


        Pattern p = Pattern
                .compile("\\{([a-zA-Z]+):([\\w\\s\\n.,\\--;!”“‘’?-@<>:/#$%^&*()**\\[\\]_=+=~`’\'\"\\\\|]+)}");

        Matcher m = p.matcher(str);
        String authorIcon = "";
        String authorName = "";
        String authorURL = "";
        String thumbnail = "";
        String description = "";
        String colour = "";
        String footerIcon = "";
        String footerText = "";
        String title = "";
        String titleURL = "";
        String image = "";
        List<String> fieldInfo = new ArrayList<>();

        EmbedBuilder em = new EmbedBuilder();
        while (m.find()) {
            String res = m.group(2);
            switch (m.group(1).toLowerCase()) {
                case "color": {
                    if (res.length() == 6 && !res.equals("")) {
                        colour = res;
                        break;
                    }
                }
                case "authoricon": {
                    if (!res.equals("")) {
                        authorIcon = getIcon(guild, user, res).equalsIgnoreCase("NONE") ? res : getIcon(guild, user, res);
                        break;
                    }
                }
                case "authorname": {
                    if (!res.equals("")) {
                        authorName = res;
                        break;
                    }
                }
                case "authorurl": {
                    if (!res.equals("")) {
                        authorURL = res;
                        break;
                    }
                }
                case "thumbnail": {
                    if (!res.equals("")) {
                        thumbnail = getIcon(guild, user, res).equalsIgnoreCase("NONE") ? res : getIcon(guild, user, res);
                        break;
                    }
                }
                case "title": {
                    if (!res.equals("")) {
                        title = res;
                        break;
                    }
                }
                case "titleurl": {
                    if (!res.equals("")) {
                    }
                }
                case "description": {
                    if (!res.equals("")) {
                        if (res.length() > 2048)
                            throw new IllegalArgumentException(
                                    "The Description can only be a max of 2048 chars! Yours is " + res.length());

                    }
                }
                case "fieldtitle": {
                    if (!res.equals("")) {
                        if (res.length() > 256)
                            throw new IllegalArgumentException(
                                    "Field Titles can only be a max of 256 characters! Yours is " + res.length());
                    }
                }
                case "fieldtext": {
                    if (!res.equals("")) {
                        if (res.length() > 1024)
                            throw new IllegalArgumentException(
                                    "Field Titles can only be a max of 1024 characters! Yours is " + res.length());

                    }
                }

                case "footericon": {
                    if (!res.equals("")) {

                    }
                }
                case "footertext": {
                    if (!res.equals("")) {

                    }
                }
                case "image": {
                    if (!res.equals("")) {
                    image =  getIcon(guild, user, res).equalsIgnoreCase("NONE") ? res : getIcon(guild, user, res);;
                    }
                }
            }
        }

        if (fieldInfo.size() % 2 != 0)
            throw new IllegalArgumentException("There isn't an even number of 'FieldTitle' and 'FieldText'!");
        if (fieldInfo.size() > 50)
            throw new IllegalArgumentException("Discord has a restriction of 25 fields!");
        if (!footerIcon.isEmpty() && footerText.isEmpty())
            throw new IllegalArgumentException("You can not have a footer icon without footer text!");
        if (!authorIcon.isEmpty() && authorName.isEmpty())
            throw new IllegalArgumentException("You can not have an author icon without author text!");
        if (!titleURL.isEmpty() && title.isEmpty())
            throw new IllegalArgumentException("You can not have an title URL without a title!");
        if (!authorURL.isEmpty() && authorName.isEmpty())
            throw new IllegalArgumentException("You can not have an author URL without an author!");
        if (colour.length() != 6 && !colour.isEmpty())
            throw new IllegalArgumentException("Your hex code must only be 6 length!");


        CustomEmbed c = new CustomEmbed();
        return c;
    }


    private static String getIcon(IGuild guild, IUser user, String str) {
        switch (str.toLowerCase()) {
            case "{guildicon}":
                return guild.getIconURL();
            case "{usericon}":
                return user.getAvatarURL();
            case "{boticon}":
                return Util.botAva();
            default:
                return "NONE";
        }
    }
}
