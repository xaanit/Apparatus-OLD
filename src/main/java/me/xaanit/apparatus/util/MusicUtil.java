package me.xaanit.apparatus.util;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.xaanit.apparatus.objects.commands.music.Music;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.music.GuildMusicManager;
import me.xaanit.simplelogger.SimpleLogger;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

/**
 * Lots of code taken (or adapted) from: https://github.com/sedmelluq/lavaplayer/blob/master/demo-d4j/src/main/java/com/sedmelluq/discord/lavaplayer/demo/d4j/Main.java
 */
public class MusicUtil extends Music {

    private static SimpleLogger logger = new SimpleLogger(Music.class);

    public static synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
        long guildId = guild.getLongID();
        GuildMusicManager musicManager = managers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(manager);
            managers.put(guildId, musicManager);
        }

        guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

        return musicManager;
    }

    public static void loadAndPlay(final IUser user, final IChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        manager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                EmbedBuilder em = Util.basicEmbed(user, "Music", CColors.BASIC);
                em.withDesc("Adding to queue " + track.getInfo().title);
                Util.sendMessage(channel, em.build());

                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                EmbedBuilder em = new EmbedBuilder();
                em.withColor(Util.hexToColor(CColors.BASIC));
                em.withAuthorIcon(channel.getGuild().getIconURL());
                em.withAuthorName("Music");
                em.withDesc("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");
                Util.sendMessage(channel, em.build());
                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                EmbedBuilder em = new EmbedBuilder();
                em.withColor(Util.hexToColor(CColors.BASIC));
                em.withAuthorIcon(channel.getGuild().getIconURL());
                em.withAuthorName("Music");
                em.withDesc("Nothing found by " + trackUrl);
                Util.sendMessage(channel, em.build());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedBuilder em = new EmbedBuilder();
                em.withColor(Util.hexToColor(CColors.BASIC));
                em.withAuthorIcon(channel.getGuild().getIconURL());
                em.withAuthorName("Music");
                em.withDesc("Could not play! Reason: " + exception.getMessage());
                Util.sendMessage(channel, em.build());
            }
        });
    }

    public static void play(IGuild guild, GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
        logger.info("Queued track with info " + trackInfo(track.getInfo()) + " on guild [ " + guild.getName() + " ] ");
    }

    public static String trackInfo(AudioTrackInfo info) {
        return "[AUTHOR=" + info.author + "[DURATION=" + info.length + "[TITLE=" + info.title + "[IS_STREAM=" + info.isStream + "[IDENTIFIER=" + info.identifier + "]]]]]";
    }

    public static void skipTrack(IChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();
        EmbedBuilder em = new EmbedBuilder();
        em.withColor(Util.hexToColor(CColors.BASIC));
        em.withAuthorIcon(channel.getGuild().getIconURL());
        em.withAuthorName("Music");
        em.withDesc("Skipped to next track.");
        Util.sendMessage(channel, em.build());
    }

}
