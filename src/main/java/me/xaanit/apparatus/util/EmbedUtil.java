package me.xaanit.apparatus.util;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;

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
}
