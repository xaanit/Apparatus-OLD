package me.xaanit.apparatus.internal.json;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class JsonAutomod {

    private String name;
    private boolean enabled = false;
    private List<Long> users = new ArrayList<>();
    private List<Long> channels = new ArrayList<>();
    private List<Long> roles = new ArrayList<>();
    private boolean userBlacklist = true;
    private boolean channelBlacklist = true;
    private boolean roleBlacklist = true;

    public JsonAutomod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public void setUserBlacklist(boolean userBlacklist) {
        this.userBlacklist = userBlacklist;
    }

    public boolean isChannelBlacklist() {
        return channelBlacklist;
    }

    public void setChannelBlacklist(boolean channelBlacklist) {
        this.channelBlacklist = channelBlacklist;
    }

    public boolean isRoleBlacklist() {
        return roleBlacklist;
    }

    public void setRoleBlacklist(boolean roleBlacklist) {
        this.roleBlacklist = roleBlacklist;
    }

    public boolean isUserBlacklist() {
        return userBlacklist;
    }

    public List<Long> getChannels() {
        return channels;
    }


    public List<Long> getUsers() {
        return users;
    }

    public List<Long> getRoles() {
        return roles;
    }

    public void addUser(long id) {
        if (this.users.stream().filter(t -> t == id).count() == 0)
            this.users.add(id);
    }

    public void removeUser(long id) {
        if (this.users.stream().filter(t -> t == id).count() == 1)
            this.users.remove(id);
    }

    public void addChannel(long id) {
        if (this.channels.stream().filter(t -> t == id).count() == 0)
            this.channels.add(id);
    }

    public void removeChannel(long id) {
        if (this.channels.stream().filter(t -> t == id).count() == 1)
            this.channels.remove(id);
    }

    public void addRole(long id) {
        if (this.roles.stream().filter(t -> t == id).count() == 0)
            this.roles.add(id);
    }

    public void removeRole(long id) {
        if (this.roles.stream().filter(t -> t == id).count() == 1)
            this.roles.remove(id);
    }
}
