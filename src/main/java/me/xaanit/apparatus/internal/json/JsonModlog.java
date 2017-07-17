package me.xaanit.apparatus.internal.json;

import me.xaanit.apparatus.internal.json.embeds.CustomEmbed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 5/15/2017.
 */
public class JsonModlog {

    private String name;

    public CustomEmbed embed = new CustomEmbed();

    private boolean useEmbed = true;

    private String stringLog = "";

    private List<Long> channels = new ArrayList<>();

    private boolean useDefault = true;

    public JsonModlog() {
    }

    public JsonModlog(String name) {
        this.name = name;
    }


    public String getName() {
        return this.name;
    }

    public CustomEmbed getEmbed() {
        return this.embed;
    }

    public boolean isUseEmbed() {
        return useEmbed;
    }

    public void setUseEmbed(boolean useEmbed) {
        this.useEmbed = useEmbed;
    }

    public boolean isUseDefault() {
        return useDefault;
    }

    public void setUseDefault(boolean useDefault) {
        this.useDefault = useDefault;
    }

    public String getStringLog() {
        return stringLog;
    }

    public void setStringLog(String stringLog) {
        this.stringLog = stringLog;
    }

    public List<Long> getChannels() {
        return channels;
    }

    public void addChannel(long l1) {
                if(channels.stream().filter(l -> l == l1).count() == 0)
                    channels.add(l1);
    }

    public void removeChannel(long l1) {
        for(int i = 0; i < channels.size(); i++) {
            if(channels.get(i) == l1)
                channels.remove(i);
        }
    }

    @Override
    public String toString() {
        return "JsonModlog{" +
                "name='" + name + '\'' +
                ", embed=" + embed +
                ", useEmbed=" + useEmbed +
                ", stringLog='" + stringLog + '\'' +
                ", channels=" + channels +
                ", useDefault=" + useDefault +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonModlog)) return false;

        JsonModlog that = (JsonModlog) o;

        if (useEmbed != that.useEmbed) return false;
        if (useDefault != that.useDefault) return false;
        if (!name.equals(that.name)) return false;
        if (!embed.equals(that.embed)) return false;
        if (!stringLog.equals(that.stringLog)) return false;
        return channels.equals(that.channels);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + embed.hashCode();
        result = 31 * result + (useEmbed ? 1 : 0);
        result = 31 * result + stringLog.hashCode();
        result = 31 * result + channels.hashCode();
        result = 31 * result + (useDefault ? 1 : 0);
        return result;
    }
}
