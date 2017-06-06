package me.xaanit.apparatus.objects.commands.fun;

import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

import static me.xaanit.apparatus.util.EmbedUtil.basicEmbed;
import static me.xaanit.apparatus.util.MessageUtil.combineArgs;
import static me.xaanit.apparatus.util.MessageUtil.sendMessage;
import static me.xaanit.apparatus.util.PermissionsUtil.allChecks;
import static me.xaanit.apparatus.util.UserUtil.getNameAndDescrim;
import static me.xaanit.apparatus.util.Util.addToHelpEmbed;
import static me.xaanit.apparatus.util.Util.getGuild;

public class Say implements ICommand {
    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName()};
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = addToHelpEmbed(this, user, new String[]{getGuild(guild).getPrefix(), getName() + " <text>"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public CmdType getType() {
        return CmdType.FUN;
    }

    @Override
    public String getInfo() {
        return "Say something!";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args, IDiscordClient client) {
        allChecks(user, guild, this, channel);

        if (args.length == 1) {
            EmbedBuilder em = basicEmbed(user, "Error", CColors.ERROR);
            em.withDesc("Please provide me some text to say!");
            sendMessage(channel, em.build());
            return;
        }
        sendMessage(channel, combineArgs(args, 1, args.length) + "\n\n*~" + getNameAndDescrim(user) + "*");
    }
}
