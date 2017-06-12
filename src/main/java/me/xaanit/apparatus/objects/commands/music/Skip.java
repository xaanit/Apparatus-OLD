package me.xaanit.apparatus.objects.commands.music;

import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;

import java.util.EnumSet;

/**
 * Created by Jacob on 6/10/2017.
 */
public class Skip extends MusicVariables implements ICommand {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public CmdType getType() {
        return null;
    }

    @Override
    public EnumSet<Permissions> getNeededPermission() {
        return null;
    }

    @Override
    public Permissions getModPerm() {
        return null;
    }

    @Override
    public String getInfo() {
        return "Votes to skip a song";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {

    }
}
