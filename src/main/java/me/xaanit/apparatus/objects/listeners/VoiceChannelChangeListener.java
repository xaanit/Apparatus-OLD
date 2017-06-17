package me.xaanit.apparatus.objects.listeners;


import me.xaanit.apparatus.objects.commands.music.MusicVariables;
import me.xaanit.apparatus.objects.interfaces.IListener;
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
            MusicVariables.channels.remove(event.getGuild().getLongID());
            MusicVariables.managers.get(event.getGuild().getLongID()).player.stopTrack();
        }
    }
}
