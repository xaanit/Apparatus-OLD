package me.xaanit.apparatus.util;

import sx.blah.discord.handle.obj.IUser;

import java.util.HashMap;
import java.util.Map;

public class MarriageUtil extends MusicUtil {

    public static Map<Long, Long> proposals = new HashMap<>();

    public static boolean isBeingProposedTo(IUser user) {
        for (long l : proposals.values())
            if (l == user.getLongID())
                return true;
        return false;
    }

    public static boolean isMarried(IUser user) {
        return config.marriages.contains(user.getLongID());
    }

    public static IUser getMarried(IUser user) {
        if (isMarried(user)) {
            if (user.equals(config.marriages.get(user.getLongID()).getOther()))
                return config.marriages.get(user.getLongID()).getPerson();
            else
                return config.marriages.get(user.getLongID()).getOther();

        }
        return null;
    }
}
