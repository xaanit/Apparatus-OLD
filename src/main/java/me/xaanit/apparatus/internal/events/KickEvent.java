package me.xaanit.apparatus.internal.events;

import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class KickEvent extends UserLeaveEvent {

    private final IUser kicker;
    private final IChannel kickedFrom;
    private final String reason;

    public KickEvent(IGuild guild, IUser user, IUser kicker, IChannel kickedFrom, String reason) {
        super(guild, user);
        this.kicker = kicker;
        this.kickedFrom = kickedFrom;
        this.reason = reason;
    }

    public IUser getKicker() {
        return kicker;
    }

    public IChannel getKickedFrom() {
        return kickedFrom;
    }

    public String getReason() {
        return reason;
    }
}
