package me.xaanit.apparatus.internal.events;

import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class BanEvent extends UserBanEvent {

    private IUser banner;

    public BanEvent(IGuild guild, IUser user, IUser banner) {
        super(guild, user);
        this.banner = banner;
    }


    public IUser getBanner() {
        return banner;
    }
}
