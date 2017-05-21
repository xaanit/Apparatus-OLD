package me.xaanit.apparatus.objects.json;

import me.xaanit.apparatus.objects.enums.PatronLevel;
import sx.blah.discord.handle.obj.IUser;

public class User {

    private long id = 0;
    private boolean isDev = false;
    private PatronLevel level = PatronLevel.NONE;

    public User() {

    }

    public User(IUser user) {
        this.id = user.getLongID();
    }


    public long getId() {
        return id;
    }

    public boolean isDev() {
        return isDev;
    }

    public PatronLevel getLevel() {
        return level;
    }
}
