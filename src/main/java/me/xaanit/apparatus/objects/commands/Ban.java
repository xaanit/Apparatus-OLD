package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.util.GuildUtil;
import me.xaanit.apparatus.util.PermissionsUtil;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jacob on 5/13/2017.
 */
public class Ban implements ICommand {
    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "banish"};
    }

    @Override
    public CmdType getType() {
        return CmdType.MODERATION;
    }

    @Override
    public EnumSet<Permissions> getNeededPermission() {
        return PermissionsUtil.makePermissions(PermissionsUtil.basicPermissions(), Permissions.BAN);
    }

    @Override
    public Permissions getUserPerm() {
        return Permissions.BAN;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{GuildUtil.getGuild(guild).getPrefix(), getName() + " <user> [reason]"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Bans a user from the server.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        List<IMessage> full = channel.getFullMessageHistory();
        List<IMessage> tatsu = full.stream().filter(m -> m.getAuthor().getLongID() == 172002275412279296L).collect(Collectors.toList());

        int lost = 0;
        int won = 0;
        int totalWinnings = 0;
        int totalLosses = 0;
        int howManyTimesWon500 = 0;
        int howManyTimesWon1500 = 0;
        int howManyTimesWon5000 = 0;
        int howManyTimesWon10000 = 0;
        String unknownWins = "";


        for (IMessage m : tatsu) {
            String content = m.getContent();
            if (m.getContent().contains("SLOTS")) {
                if (m.getContent().contains("WIN")) {
                    int wonAmount = Integer.parseInt(content.replaceAll("[^0-9]", "").substring(3));
                    if (wonAmount == 500)
                        howManyTimesWon500++;
                    else if (wonAmount == 1500)
                        howManyTimesWon1500++;
                    else if (wonAmount == 5000)
                        howManyTimesWon5000++;
                    else if (wonAmount == 10000)
                        howManyTimesWon10000++;
                    else
                        unknownWins += wonAmount + " ";
                    totalWinnings += wonAmount;
                    totalWinnings -= 500;
                    won++;
                } else {
                    lost++;
                    totalLosses += 500;
                }
            }
        }

        Util.sendMessage(channel, "Lost: " + lost + "\nWon: " + won + "\nTotal winnings: " + totalWinnings + "\nTotal Losses: " + totalLosses + "\nNet gain/loss: " + (totalWinnings - totalLosses) + "\n500 wins: " + howManyTimesWon500 + "\n1500 wins: " + howManyTimesWon1500 + "\n5000 wins: " + howManyTimesWon5000 + "\n10K wins: " + howManyTimesWon10000 + "\nUnknown: " + unknownWins);

    }
}
