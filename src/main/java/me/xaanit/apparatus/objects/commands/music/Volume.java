package me.xaanit.apparatus.objects.commands.music;

import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.audio.AudioPlayer;

import java.util.Arrays;
import java.util.EnumSet;

import static me.xaanit.apparatus.util.Util.*;

public class Volume implements ICommand {
    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "adjustvolume"};
    }

    @Override
    public CmdType getType() {
        return CmdType.MUSIC;
    }

    @Override
    public EnumSet<Permissions> getNeededPermission() {
        EnumSet<Permissions> perms = basicPermissions();
        perms.add(Permissions.VOICE_CONNECT);
        perms.add(Permissions.VOICE_SPEAK);
        perms.add(Permissions.VOICE_USE_VAD);
        return perms;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " [increase/decrease/set/check] [num]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Check or adjust the volume";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        AudioPlayer player = AudioPlayer.getAudioPlayerForGuild(guild);

        if (args.length == 1 || (args.length >= 2 && args[1].equalsIgnoreCase("check"))) {
            if (user.getVoiceStateForGuild(guild).getChannel() == null) {
                EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
                em.withDesc("You must be in a voice channel!");
                sendMessage(channel, em.build());
                return;
            }

            EmbedBuilder em = basicEmbed(user, "Volume", CColors.BASIC);
            em.withDesc("Current volume: " + AudioPlayer.getAudioPlayerForGuild(guild).getVolume());
            sendMessage(channel, em.build());
            return;
        }

        if (args[1].equalsIgnoreCase("increase") || args[1].equalsIgnoreCase("decrease")|| args[1].equalsIgnoreCase("set")) {
            if (args.length == 2) {
                sendMessage(channel, missingNum(user));
                return;
            }
            float volume;
            try {
                float f = Float.parseFloat(args[2]);
                if (args[1].equalsIgnoreCase("increase"))
                    volume = player.getVolume() + f;
                else if (args[1].equalsIgnoreCase("decrease"))
                    volume = player.getVolume() - f;
                else
                    volume = f;

                if (volume > 2)
                    throw new IllegalArgumentException("Volume can not exceed 2");
            } catch (NumberFormatException ex) {
                EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
                em.withDesc("Could not convert " + ex.getMessage());
                sendMessage(channel, em.build());
                return;
            } catch (IllegalArgumentException ex) {
                EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
                em.withDesc(ex.getMessage());
                sendMessage(channel, em.build());
                return;
            }

            player.setVolume(volume);
            EmbedBuilder em = basicEmbed(user, "Volume", CColors.BASIC);
            em.withDesc("Set volume to " + volume);
            sendMessage(channel, em.build());
            return;
        }

    }

    private EmbedObject missingNum(IUser user) {
        EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
        em.withDesc("You need to give a number!");
        return em.build();
    }
}
