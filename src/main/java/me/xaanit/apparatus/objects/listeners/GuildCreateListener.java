package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.apparatus.util.Util;
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
        if(ReadyListener.ready) {
            for (long l : GlobalVars.config.getBlacklistedServers()) {
                IGuild guild = GlobalVars.client.getGuildByID(l);
                if (guild != null) {
                    Util.sendMessage(guild.getOwner().getOrCreatePMChannel(), "Your server [" + guild.getName() + " ] has been blacklisted by the developer for one reason or another. I shall be leaving it.");
                    RequestBuffer.request(() -> guild.leave());
                }
            }
            return;
        }
        IGuild guild = event.getGuild();
        GlobalVars.guilds.putIfAbsent(guild.getLongID(), Database.loadGuild(guild));
        RequestBuffer.request(() -> GlobalVars.client.changePlayingText("+help | " + guild.getShard().getGuilds().size() + " guild(s)"));

    }
}
