package me.xaanit.apparatus.util;

import sx.blah.discord.handle.obj.IRole;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@SuppressWarnings("unused")
public class Util extends UserUtil{



    /**
     * Gets the current time in UTC
     *
     * @return The current time (UTC)
     */
    public static String getCurrentTime() {
        LocalDateTime date = LocalDateTime.now();
        LocalTime time = LocalTime.now(Clock.systemUTC());
        return (date.getDayOfWeek().toString().charAt(0)
                + date.getDayOfWeek().toString().substring(1).toLowerCase())
                + ", "
                + (date.getMonth().toString().charAt(0)
                + date.getMonth().toString().substring(1).toLowerCase())
                + " " + date.getDayOfMonth() + " " + date.getYear() + " | "
                + (time.getHour() > 12 ? time.getHour() - 12 : time) + ":" + time.getMinute() + ":"
                + time.getSecond() + (time.getHour() > 12 ? " PM" : " AM");
    }

    public static String formatRoleList(List<IRole> list) {
        StringBuilder builder = new StringBuilder();
        list.forEach(r -> builder.append(r.getName()).append(", "));
        return builder.toString().substring(0, builder.toString().indexOf(","));
    }




}
