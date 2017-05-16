package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.interfaces.IListener;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.RequestBuffer;

/**
 * Created by Jacob on 5/15/2017.
 */
public class GuildCreateListener implements IListener {

    @EventSubscriber
    public void onGuildCreate(GuildCreateEvent event) {
        IGuild guild = event.getGuild();
        GlobalVars.guilds.putIfAbsent(guild.getLongID(), Database.loadGuild(guild));
        RequestBuffer.request(() -> GlobalVars.client.changePlayingText("+help | " + guild.getShard().getGuilds().size() + " guild(s)"));

    }
}
