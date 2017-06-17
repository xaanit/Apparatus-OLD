package me.xaanit.apparatus.internal.json;

/**
 * Created by Jacob on 5/17/2017.
 */
public class JsonAutorole {

    public boolean bot = false;
    public long id = 0;

    public JsonAutorole(boolean bot, long id) {
        this.bot = bot;
        this.id = id;
    }

    public JsonAutorole() {
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
