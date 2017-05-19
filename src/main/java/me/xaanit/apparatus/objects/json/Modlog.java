package me.xaanit.apparatus.objects.json;

import me.xaanit.apparatus.objects.json.embeds.CustomEmbed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 5/15/2017.
 */
public class Modlog {

    private String name;

    private CustomEmbed embed = new CustomEmbed();

    private boolean useEmbed = true;

    private String stringLog = "";

    private List<Long> channels;

    private boolean useDefault = true;

    public Modlog() {
    }

    public Modlog(String name) {
        this.name = name;
        channels = new ArrayList<>();
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
}
