package me.xaanit.apparatus.objects;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

import java.util.HashMap;
import java.util.Map;

public class PlaceInCommand {

    public IChannel currChannel = null;
    public IMessage lastMessage = null;
    public int place = 0;
    public Map<String, Object> tempObjs = new HashMap<>();

    @Override
    public String toString() {
        return "PlaceInCommand{" +
                "currChannel=" + currChannel +
                ", lastMessage=" + lastMessage +
                ", place=" + place +
                ", tempObjs=" + tempObjs +
                '}';
    }
}
