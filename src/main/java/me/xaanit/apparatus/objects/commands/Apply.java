package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;


/**
 * Created by Jacob on 4/21/2017.
 */
public class Apply implements ICommand {

	public Apply() {
	}

	@Override
	public String getName() {
		return "apply";
	}

	@Override
	public Permissions getNeededPermission() {
		return null;
	}

	@Override
	public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message,
			String[] args) {

	}
}
