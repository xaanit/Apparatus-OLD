package me.xaanit.apparatus.objects;

import sx.blah.discord.handle.obj.IRole;

/**
 * Created by Jacob on 5/15/2017.
 */
public class FailedRole {

    private IRole role = null;
    private String reason = "";

    public FailedRole(IRole role, String reason) {
        this.role = role;
        this.reason = reason;
    }

    public IRole getRole() {
        return role;
    }

    public String getReason() {
        return reason;
    }
}

