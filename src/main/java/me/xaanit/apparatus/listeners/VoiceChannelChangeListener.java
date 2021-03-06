package me.xaanit.apparatus.listeners;


import me.xaanit.apparatus.commands.music.Music;
import me.xaanit.apparatus.interfaces.IListener;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.RequestBuffer;

public class VoiceChannelChangeListener implements IListener {

    @EventSubscriber
    public void onVoiceChannelChange(UserVoiceChannelMoveEvent event) {
        IUser user = event.getUser();
        IVoiceChannel channel = event.getNewChannel();
        IVoiceChannel oChannel = event.getOldChannel();
        if (oChannel.getConnectedUsers().size() == 1 && oChannel.getConnectedUsers().stream().filter(u -> event.getClient().getOurUser().getLongID() == u.getLongID()).count() == 1) {
            RequestBuffer.request(() -> oChannel.leave());
            Music.channels.remove(event.getGuild().getLongID());
            Music.managers.get(event.getGuild().getLongID()).player.stopTrack();
        }
    }
}
