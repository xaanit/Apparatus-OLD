package me.xaanit.apparatus.objects.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.music.GuildMusicManager;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.HashMap;
import java.util.Map;


public class MusicVariables extends GlobalVars {

    public static Map<Long, IVoiceChannel> channels = new HashMap<>();
    public static Map<Long, GuildMusicManager> managers = new HashMap<>();
    public static Map<Long, Map<AudioTrack, Integer>> skips = new HashMap<>();

    public static final AudioPlayerManager manager = new DefaultAudioPlayerManager();

    static {
        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);
    }


}
