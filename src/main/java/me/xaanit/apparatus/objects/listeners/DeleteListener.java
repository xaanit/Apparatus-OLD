package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.objects.interfaces.IListener;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

/**
 * Created by Jacob on 5/15/2017.
 */
public class DeleteListener implements IListener {

@EventSubscriber
    public void onDelete(MessageDeleteEvent event) {
    IGuild guild = event.getGuild();
    IUser user = event.getAuthor();
    IChannel channel = event.getChannel();

}
}
