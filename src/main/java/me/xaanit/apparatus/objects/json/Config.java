package me.xaanit.apparatus.objects.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 4/21/2017.
 */
public class Config {

    private String token;

    private List<Long> blacklistedUsers;

    private List<Long> blacklistedServers;

    public Config() {
    }

    public Config(boolean val) {
        this.token = "";
        this.blacklistedUsers = new ArrayList<>();
        this.blacklistedServers = new ArrayList<>();
    }


    public String getToken() {
        return this.token;
    }


    public List<Long> getBlacklistedUsers() {
        return blacklistedUsers;
    }

    public void blacklistUser(long l) {
        if (blacklistedUsers.stream().filter(u -> u == l).count() == 0)
            this.blacklistedUsers.add(l);
    }

    public void unBlacklistUser(long l) {
        if (blacklistedUsers.stream().filter(u -> u == l).count() == 1)
            this.blacklistedUsers.remove(l);
    }

    public List<Long> getBlacklistedServers() {
        return blacklistedServers;
    }

    public void blacklistServer(long l) {
        if (blacklistedServers.stream().filter(u -> u == l).count() == 0)
            this.blacklistedServers.add(l);
    }

    public void unBlacklistServer(long l) {
        if (blacklistedServers.stream().filter(u -> u == l).count() == 1)
            this.blacklistedServers.remove(l);
    }
}
