package me.xaanit.apparatus.util;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.api.outside.Requests;
import me.xaanit.apparatus.internal.json.embeds.CustomEmbed;
import me.xaanit.apparatus.internal.json.embeds.Field;
import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.interfaces.ICommand;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jacob on 5/15/2017.
 */
public class EmbedUtil extends ChannelUtil {


    public static final String BASIC_USAGE = "Usage: %s %s";
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
        EmbedBuilder em = new EmbedBuilder()
                .withColor(Util.hexToColor(colors))
                .withAuthorIcon(Util.botAva())
                .withAuthorName(name)
                .withFooterIcon(user.getAvatarURL())
                .withFooterText("Requested by: " + Util.getNameAndDescrim(user));
        return em;
    }

    public static CustomEmbed customEmbedParser(String str) {
        if (str.startsWith("http")) {
            if (!str.startsWith("https://pastebin.com/raw/") && !str.startsWith("https://raw.githubusercontent.com/") && !str.startsWith("https://hastebin.com/raw/"))
                return null;
            try {
                str = Requests.get(str);
            } catch (IOException ex) {
                return null;
            }
        }
        str = str.replaceAll("\\sFieldText", "} {FieldText").replaceAll("\\sInline", "} {Inline");

        List<String> look = new ArrayList<>();
        String s;
        try {
            s = Requests.get("https://raw.githubusercontent.com/NegotiumBots/ApparatusWiki/master/variables.md");
        } catch (IOException ex) {
            return null;
        }

        String[] split = s.replaceAll("[\\{\\}]", "").split(":");
        for (String s1 : split) {
            String s2 = s1.split(";")[0];
            look.add(s2);
        }

        for (String s1 : look) {
            str = str.replace("{" + s1 + "}", "[[" + s1 + "]]");
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
        boolean includeTimestamp = false;

        EmbedBuilder em = new EmbedBuilder();
        while (m.find()) {
            String res = m.group(2);
            switch (m.group(1).toLowerCase()) {
                case "colour":
                case "color": {
                    res = res.replace("#", "");
                    if (res.length() == 6 && !res.equals("")) {
                        colour = res;
                        break;
                    }
                }
                case "authoricon": {
                    if (!res.isEmpty()) {
                        authorIcon = res;
                    }
                    break;
                }
                case "authorname": {
                    if (!res.isEmpty()) {
                        authorName = res;
                    }
                    break;
                }
                case "authorurl": {
                    if (!res.isEmpty()) {
                        authorURL = res;
                        break;
                    }
                }
                case "thumbnail": {
                    if (!res.isEmpty()) {
                        thumbnail = res;
                    }
                    break;
                }
                case "title": {
                    if (!res.isEmpty()) {
                        title = res;
                    }
                    break;
                }
                case "titleurl": {
                    if (!res.isEmpty()) {
                        titleURL = res;
                    }
                    break;
                }
                case "description": {
                    if (!res.isEmpty()) {
                        if (res.length() > 2048)
                            throw new IllegalArgumentException(
                                    "The Description can only be a max of 2048 chars! Yours is " + res.length());
                        description = res;
                    }
                    break;
                }
                case "fieldtitle": {
                    if (!res.isEmpty()) {
                        if (res.length() > 256)
                            throw new IllegalArgumentException(
                                    "Field Titles can only be a max of 256 characters! Yours is " + res.length());
                        fieldInfo.add(res);
                        break;
                    }
                }
                case "fieldtext": {
                    if (!res.isEmpty()) {
                        if (res.length() > 1024)
                            throw new IllegalArgumentException(
                                    "Field Titles can only be a max of 1024 characters! Yours is " + res.length());
                        fieldInfo.add(res);
                        break;
                    }
                }

                case "inline": {
                    if (!res.isEmpty()) {
                        if (!res.equalsIgnoreCase("true") && !res.equalsIgnoreCase("false"))
                            throw new IllegalArgumentException("Inline must be true or false!");
                        fieldInfo.add(res);
                        break;
                    }
                }

                case "timestamp": {
                    if (!res.isEmpty()) {
                        includeTimestamp = res.equalsIgnoreCase("true");
                    }
                }

                case "footericon": {
                    if (!res.isEmpty()) {
                        footerIcon = res;
                        break;
                    }
                }
                case "footertext": {
                    if (!res.isEmpty()) {
                        footerText = res;
                        break;
                    }
                }
                case "image": {
                    if (!res.isEmpty()) {
                        image = res;
                        break;
                    }
                }
            }
        }

        if (fieldInfo.size() % 3 != 0)
            throw new IllegalArgumentException("There isn't an even number of 'FieldTitle', 'FieldText', and `Inline`!");
        if (fieldInfo.size() > (3 * 25))
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

        List<Field> fields = new ArrayList<>();
        for (int i = 0; i < fieldInfo.size(); i += 3)
            fields.add(new Field(fieldInfo.get(i), fieldInfo.get(i + 1), fieldInfo.get(i + 2).equalsIgnoreCase("true")));

        CustomEmbed c = new CustomEmbed(
                authorIcon, authorName, authorURL, thumbnail, title, titleURL,
                description, fields, image, colour, footerIcon, footerText, includeTimestamp
        );
        return c;
    }


    /* PICTURE

    .replace("[[guildicon]]", guild.getIconURL())
    .replace("[[usericon]]", user.getAvatarURL())
    .replace("[[boticon]]", Util.botAva());

    */

    /* TEXT

    .replace("[[timestamp]]", Util.getCurrentTime())
    .replace("[[userid]]", user.getStringID())
    .replace("[[username}], user.getName())
    .replace("[[usermention]]," user.mention())
    .replace("[[userdescrim]]", user.getDiscriminator())
    .replace("[[guildname]]", guild.getName())
    .replace("[[guildid]]", guild.getStringID())
    .replace("[[botname]]", "Apparatus")
    .replace("[[botdescrim]]", GlobalVars.client().getOurUser().getDiscriminator())
    .replace("[[channelname]]",channel.getName())
    .replace("[[channelmention]]", channel.mention())
    .replace("[[oldmessag]]e", oldmessage)
    .replace("[[newmessage]]", newmessage)
    .replace("[[deletedmessage]]", deletedmessage)

     */
}
