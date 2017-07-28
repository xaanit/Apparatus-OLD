package me.xaanit.apparatus.commands.music;


import me.xaanit.apparatus.enums.CColors;
import me.xaanit.apparatus.enums.CmdType;
import me.xaanit.apparatus.interfaces.ICommand;
import me.xaanit.simplelogger.SimpleLogger;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.Arrays;
import java.util.EnumSet;

import static me.xaanit.apparatus.util.Util.*;

public class Play extends Music implements ICommand {

    private static SimpleLogger logger = SimpleLogger.getLoggerByClass(Music.class);

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
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
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " <title/link>"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Plays a youtube video.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);


        if (user.getVoiceStateForGuild(guild).getChannel() == null) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("You must be in a voice channel!");
            sendMessage(channel, em.build());
            return;
        }

        if (args.length == 1) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("You must provide a title or a link!");
            sendMessage(channel, em.build());
            return;
        }

        IVoiceChannel vChannel = user.getVoiceStateForGuild(guild).getChannel();

        if (!channels.containsKey(guild.getLongID())) {
            RequestBuffer.request(() -> vChannel.join());
            channels.put(guild.getLongID(), vChannel);
            logger.info("Joining voice channel [ " + vChannel.getName() + " ] in guild [ " + guild.getName() + " ] due to request by [ " + getNameAndDescrim(user) + " ]");
        }

        String info = combineArgs(args, 1, -1);

        if (!info.startsWith("http"))
              info = "ytsearch: " + info;


        loadAndPlay(user, channel, info);


    }


}
