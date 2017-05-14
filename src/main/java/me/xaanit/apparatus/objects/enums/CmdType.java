package me.xaanit.apparatus.objects.enums;

/**
 * Created by Jacob on 5/13/2017.
 */
public enum CmdType {
    UTIL,
    FUN,
    DEV,
    MODERATION;

    public static String format(CmdType type) {
        String[] args = type.toString().split("_");
        String res = "";
        for(int i =0 ;i < args.length; i++)
            res += args[i].charAt(0) + args[i].substring(1).toLowerCase() + " ";
        return res.trim();
    }
}
