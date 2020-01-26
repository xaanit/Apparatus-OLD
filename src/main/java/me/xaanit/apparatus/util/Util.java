package me.xaanit.apparatus.util;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.interfaces.ICommand;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SuppressWarnings("unused")
public class Util extends UserUtil {
    

    public static boolean hasPlaceholder(IUser user) {
        for(ICommand command : commands.values()) {
            if(command.getPlaceInCommand(user) != null)
                return true;
        }
        return false;
    }

    /**
     * Gets the current time in UTC
     *
     * @return The current time (UTC)
     */
	public static String getCurrentTime(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM dd yyyy | hh:mm:ss aa",Locale.ENGLISH);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		return format.format(date);
	}

    public static String formatRoleList(List<IRole> list) {
        StringBuilder builder = new StringBuilder();
        list.forEach(r -> builder.append(r.getName()).append(", "));
        return list.size() == 0 ? "None" : builder.toString().substring(0, builder.toString().lastIndexOf(","));
    }

    public static String getShipName(IUser user, IUser user1) {
        String s1 = user1.getName();
        String s2 = user.getName();
        String s11 = s1.substring(0, (int) Math.floor(s1.length() / 2));
        String s22 = s2.substring((int) Math.floor(s2.length() / 2), s2.length());
        String newName = s11 + s22;
        return newName;
    }


    public static String botAva() {
        return GlobalVars.client.getOurUser().getAvatarURL();
    }

    public static boolean equalsAny(IRole role, long... id) {
        for (long l : id)
            if (l == role.getLongID())
                return true;
        return false;
    }

}
