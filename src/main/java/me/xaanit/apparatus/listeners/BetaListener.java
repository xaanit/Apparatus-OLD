package me.xaanit.apparatus.listeners;

import me.xaanit.apparatus.interfaces.IListener;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.util.RequestBuffer;

/**
 * Created by Jacob on 5/15/2017.
 */
public class BetaListener implements IListener {

    public void onDevLeave(UserLeaveEvent event) {
        if (event.getUser().getLongID() == 233611560545812480L) {
            RequestBuffer.request(() -> event.getGuild().leave());
        }
    }
}
