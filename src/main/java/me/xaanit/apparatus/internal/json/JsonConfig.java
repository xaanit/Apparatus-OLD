package me.xaanit.apparatus.internal.json;

import me.xaanit.apparatus.objects.marriage.Marriage;
import me.xaanit.apparatus.objects.marriage.MultiHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Jacob on 4/21/2017.
 */
public class JsonConfig {

    private String token = "";

    private String github_user = "";

    private String github_password = "";

    private List<Long> blacklistedUsers = new ArrayList<>();

    private List<Long> blacklistedServers = new ArrayList<>();

    private List<String> apiKeys = new ArrayList<>();

    private int cleverbotCalls = 1;

    private JsonStats stats = new JsonStats();

    public Map<Integer, JsonStats> shardStats = new ConcurrentHashMap<>();

    public MultiHashMap<Long, Marriage> marriages = new MultiHashMap<>();

    public JsonConfig() {
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


    public String getApiKey(String type) {
        final String lower = type.toLowerCase();
        return apiKeys.stream().filter(k -> k.toLowerCase().startsWith(lower)).findFirst().orElse("");
    }

    public int getCleverbotCalls() {
        return cleverbotCalls;
    }

    public void setCleverbotCalls(int cleverbotCalls) {
        this.cleverbotCalls = cleverbotCalls;
    }

    public String getGithubUser() {
        return github_user;
    }

    public String getGithubPassword() {
        return github_password;
    }

    public JsonStats getStats() {
        return stats;
    }
}
