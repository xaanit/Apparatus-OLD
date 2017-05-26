package me.xaanit.apparatus.objects.listeners;

import me.xaanit.apparatus.objects.interfaces.IListener;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;

/**
 * Created by Jacob on 5/24/2017.
 */
public class DeleteBotCommandListener implements IListener {

    @EventSubscriber
    public void onXAdd(ReactionAddEvent event) {
        if (!Util.isDev(event.getUser()))
            return;
        if (event.getAuthor().getLongID() != event.getClient().getOurUser().getLongID())
            return;
        if (event.getReaction().getUnicodeEmoji().getHtmlDecimal().equals("&#10060;"))
            Util.deleteMessage(event.getMessage());
    }
}
