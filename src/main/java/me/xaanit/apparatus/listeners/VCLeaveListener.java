package me.xaanit.apparatus.listeners;

import me.xaanit.apparatus.interfaces.IListener;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;


public class VCLeaveListener implements IListener {

    @EventSubscriber
    public void  onVcLeave(UserVoiceChannelLeaveEvent event) {

    }
}
