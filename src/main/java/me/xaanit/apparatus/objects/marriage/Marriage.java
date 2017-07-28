package me.xaanit.apparatus.objects.marriage;

import sx.blah.discord.handle.obj.IUser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import static me.xaanit.apparatus.GlobalVars.client;

public class Marriage {
    private final long person;
    private final long other;
    private final long epoch;
    private long epochOfAnniversary = -1;

    public Marriage(long arg0, long arg1, long arg2) {
        this.person = arg0;
        this.other = arg1;
        this.epoch = arg2;
    }

    public Marriage(long arg0, long arg1) {
        this(arg0, arg1, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public IUser getPerson() {
        return client.getUserByID(person);
    }

    public IUser getOther() {
        return client.getUserByID(other);
    }

    public String marriedSince() {
        long seconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - epoch;
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        int hours = (int) (TimeUnit.SECONDS.toHours(seconds) - (day * 24));
        int minute = (int) (TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60));
        int second = (int) (TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60));
        int years = 0;
        while (day >= 365) {
            years++;
            day -= 365;
        }
        String uptime = "";
        int[] arr = {years, day, hours, minute, second};
        String[] arr1 = {"year", "day", "hour", "minute", "second"};
        for (int i = 0; i < arr.length; i++) {
            int j = arr[i];
            if (i == arr.length - 1) {
                if (uptime.isEmpty()) {
                    uptime += j + " " + arr1[i] + (j > 1 ? "s" : "") + ".";
                } else if (j > 0) {
                    uptime += "and " + j + " " + arr1[i] + (j > 1 ? "s" : "") + ".";
                }
            } else {
                if (j > 0) {
                    uptime += j + " " + arr1[i] + (j > 1 ? "s" : "") + ", ";
                }
            }
        }
        uptime = uptime.trim();
        return uptime.charAt(uptime.length() - 1) == ',' ? uptime.substring(0, uptime.length() - 1) + "." : uptime;
    }

    public long getEpochOfAnniversary() {
        return epochOfAnniversary;
    }

    public void setEpochOfAnniversary(long epochOfAnniversary) {
        this.epochOfAnniversary = epochOfAnniversary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Marriage)) return false;

        Marriage marriage = (Marriage) o;

        if (person != marriage.person) return false;
        if (other != marriage.other) return false;
        return epoch == marriage.epoch;
    }


    @Override
    public int hashCode() {
        int result = (int) (person ^ (person >>> 32));
        result = 31 * result + (int) (other ^ (other >>> 32));
        result = 31 * result + (int) (epoch ^ (epoch >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Marriage{" +
                "person=" + person +
                ", other=" + other +
                ", married=" + marriedSince() +
                '}';
    }
}
