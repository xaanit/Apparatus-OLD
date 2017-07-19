package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.internal.json.JsonGuild;
import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.apparatus.util.Util;
import me.xaanit.simplelogger.SimpleLogger;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.RequestBuffer;

import java.util.Arrays;
import java.util.List;

public class GuildCreateListener implements IListener {

    private static int curr = 0;
    private static SimpleLogger logger = SimpleLogger.getLoggerByClass(Database.class);
    public final static List<String> MOD_LOGS = Arrays.asList("user_ban", "user_kick", "message_delete", "message_edit", "role_update", "role_delete", "role_create", "channel_create", "channel_delete", "channel_update", "guild_update", "message_pin", "message_unpin", "user_join", "user_leave", "user_pardon", "discord_ban", "webhook_create", "webhook_delete", "webhook_update", "nickname_change", "ownership_transfer", "user_role_update", "voice_join", "voice_leave", "voice_change");

    @EventSubscriber
    public void onGuildCreate(GuildCreateEvent event) {
        boolean left = false;
        if (ReadyListener.ready) {
            for (long l : GlobalVars.config.getBlacklistedServers()) {
                IGuild guild = GlobalVars.client.getGuildByID(l);
                if (guild != null) {
                    Util.sendMessage(guild.getOwner().getOrCreatePMChannel(), "Your server [" + guild.getName() + " ] has been blacklisted by the developer for one reason or another. I shall be leaving it.");
                    RequestBuffer.request(() -> guild.leave());
                    left = true;
                }
            }
            if (left)
                return;
        }
        synchronized (this) {
            IGuild guild = event.getGuild();
            if (!GlobalVars.guilds.containsKey(guild.getLongID())) {
                JsonGuild g = Database.loadGuild(guild);
                g.updateCommands();
                MOD_LOGS.forEach(m -> g.addModlog(m));
                GlobalVars.guilds.putIfAbsent(guild.getLongID(), g);
            }
            curr++;
        }
    }
}
