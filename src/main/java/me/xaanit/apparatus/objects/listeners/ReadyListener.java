package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.objects.commands.Apply;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;

/**
 * Created by Jacob on 4/21/2017.
 */
public class ReadyListener {

	@EventSubscriber
	public void onReady(ReadyEvent event) {
	}


	private void initCommands() {
		ICommand commands = new ICommand[] {new Apply()};
		for(ICommand command : commands){}

	}

}
