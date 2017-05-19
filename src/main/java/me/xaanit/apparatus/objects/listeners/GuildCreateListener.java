package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.apparatus.objects.json.Guild;
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
        boolean left = false;
        if(ReadyListener.ready) {
            for (long l : GlobalVars.config.getBlacklistedServers()) {
                IGuild guild = GlobalVars.client.getGuildByID(l);
                if (guild != null) {
                    Util.sendMessage(guild.getOwner().getOrCreatePMChannel(), "Your server [" + guild.getName() + " ] has been blacklisted by the developer for one reason or another. I shall be leaving it.");
                    RequestBuffer.request(() -> guild.leave());
                    left = true;
                }
            }
            if(left)
                return;
        }
        IGuild guild = event.getGuild();
        Guild g = Database.loadGuild(guild);
        g.updateCommands();
        g.addModlog("user_join");
        g.addModlog("user_leave");
        g.addModlog("message_edit");
        g.addModlog("message_delete");
        GlobalVars.guilds.putIfAbsent(guild.getLongID(), g);
        RequestBuffer.request(() -> GlobalVars.client.streaming("@Apparatus prefix | " + GlobalVars.client.getGuilds().size() + " guild(s)", "https://www.twitch.tv"));


    }
}
