package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.objects.interfaces.IListener;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;


public class VCLeaveListener implements IListener {

    @EventSubscriber
    public void  onVcLeave(UserVoiceChannelLeaveEvent event) {

    }
}
