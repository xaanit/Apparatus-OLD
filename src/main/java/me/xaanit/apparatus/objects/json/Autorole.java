package me.xaanit.apparatus.objects.json;

/**
 * Created by Jacob on 5/17/2017.
 */
public class Autorole {

    public boolean bot = false;
    public long id = 0;

    public Autorole(boolean bot, long id) {
        this.bot = bot;
        this.id = id;
    }

    public Autorole() {
    }

    public boolean isBot() {
        return this.bot;
    }

    public void setBot(boolean val) {
        this.bot = val;
    }

    public long getID() {
        return this.id;
    }

    public void setID(long val) {
        this.id = val;
    }
}
