package me.xaanit.apparatus.objects.interfaces;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public interface ICommand {

	String getName();

	Permissions getNeededPermission();

	void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message,
			String[] args);

}


