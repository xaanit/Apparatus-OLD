package me.xaanit.apparatus.internal.events;

import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class BanEvent extends UserBanEvent {

    private final IUser banner;
    private final IChannel bannedIn;
    private final String banReason;

    public BanEvent(IGuild guild, IUser user, IUser banner, IChannel bannedIn, String banReason) {
        super(guild, user);
        this.banner = banner;
        this.bannedIn = bannedIn;
        this.banReason = banReason;
    }


    public IUser getBanner() {
        return banner;
    }

    public IChannel getBannedIn() {
        return bannedIn;
    }

    public String getBanReason() {
        return banReason;
    }
}
