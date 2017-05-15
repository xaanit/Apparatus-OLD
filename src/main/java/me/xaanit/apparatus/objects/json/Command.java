package me.xaanit.apparatus.objects.json;


import sx.blah.discord.handle.obj.IRole;

import java.util.ArrayList;
import java.util.List;

public class Command {

    private String name;
    private boolean role = false;
    private boolean whitelist = true;
    private List<Long> roles;

    protected Command(String name) {
        this.name = name;
        this.roles = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public boolean isWhitelist() {
        return whitelist;
    }

    public void setWhitelist(boolean whitelist) {
        this.whitelist = whitelist;
    }

    public List<Long> getRoles() {
        return roles;
    }

    public void addRole(IRole role) {
        if (roles.stream().filter(r -> r == role.getLongID()).count() != 1)
            roles.add(role.getLongID());
    }

    public void removeRole(IRole role) {
        removeRole(role.getLongID());
    }

    public void removeRole(long role) {
        if (roles.stream().filter(r -> r == role).count() == 1)
            roles.remove(role);
    }
}
