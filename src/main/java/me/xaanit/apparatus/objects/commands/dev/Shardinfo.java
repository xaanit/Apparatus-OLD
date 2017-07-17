package me.xaanit.apparatus.objects.commands.dev;

import com.google.gson.Gson;
import com.jakewharton.fliptables.FlipTable;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.xaanit.apparatus.internal.json.github.GithubRequest;
import me.xaanit.apparatus.internal.json.github.GithubResponse;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IShard;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import static me.xaanit.apparatus.util.Util.*;

public class Shardinfo implements ICommand {
    private static final String GIST_LINK = "https://api.github.com/gists";
    private static final Gson gson = new Gson();

    @Override
    public String getName() {
        return "shardinfo";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "shards"};
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName()}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Returns info on all shards.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);
        EmbedBuilder em = basicEmbed(user, "Shard info!", CColors.BASIC);
        String[] headers = {"Shard #", "Guilds", "Users", "Channels", "Online"};
        String[][] data = new String[client.getShards().size()][5];
        for (int i = 0; i < client.getShards().size(); i++) {
            IShard shard = client.getShards().get(i);
            data[i][0] = format(shard.getInfo()[0]);
            data[i][1] = format(shard.getGuilds().size());
            data[i][2] = format(shard.getUsers().size());
            data[i][3] = format(shard.getChannels().size());
            data[i][4] = shard.isLoggedIn() + "";
        }
        String table = FlipTable.of(headers, data);
        if (args.length == 4) {
            System.out.println("lol ok");
            for (int i = 0; i < 4000; i++)
                table += "a";
            System.out.println("lol done");
        }

        if (table.length() < 2000) {
            em.withDesc("```java\n" + table + "\n```");
        } else {
            sendFileInstead(channel, table, user);
            return;
        }

        channel.sendMessage(em.build());

    }

    private void sendFileInstead(IChannel channel, String table, IUser user) {
        try {
            EmbedBuilder em = basicEmbed(user, "Shardinfo", CColors.BASIC);
            em.withDesc("[Click me for shard info](" + getGistLink(table) + ")");
            sendMessage(channel, em.build());
        } catch (Exception ex) {
            EmbedBuilder em1 = basicEmbed(user, "Error", CColors.ERROR);
            em1.withDesc(ex.getClass().getName() + " while uploading file:\n" + ex.getMessage());
            channel.sendMessage(em1.build());
        }
    }

    private String getGistLink(String table) {
        try {
            return gson.fromJson(Unirest.post(GIST_LINK).header("Content-Type", "application/json").header("Accept", "application/vnd.github.v3+json").body(new GithubRequest(table)).asString().getBody(), GithubResponse.class).html_url;
        } catch (UnirestException ex) {
            return "";
        }
    }


    private String format(Number i) {
        return NumberFormat.getNumberInstance(Locale.US).format(i);
    }

}
