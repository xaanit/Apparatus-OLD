package me.xaanit.apparatus.objects.commands;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.database.Database;
import me.xaanit.apparatus.objects.enums.CColors;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.objects.json.User;
import me.xaanit.apparatus.util.GuildUtil;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;

/**
 * Created by Jacob on 5/22/2017.
 */
public class CreateUser implements ICommand {
    @Override
    public String getName() {
        return "createuser";
    }

    @Override
    public String[] getAliases() {
        return new String[]{getName(), "user"};
    }

    @Override
    public CmdType getType() {
        return CmdType.DEV;
    }

    @Override
    public EmbedObject getHelp(IUser user, IGuild guild) {
        EmbedBuilder em = Util.addToHelpEmbed(this, user, new String[]{GuildUtil.getGuild(guild).getPrefix(), getName() + " <args>"}, new String[]{Arrays.toString(getAliases())
                .replaceAll(getName() + ",\\s", "")});
        return em.build();
    }

    @Override
    public String getInfo() {
        return "Makes a user json.";
    }

    @Override
    public void runCommand(IUser user, IChannel channel, IGuild guild, IMessage message, String[] args) {
        Util.allChecks(user, guild, this, channel);

        if (args.length == 1) {
            Util.sendMessage(channel, Util.argumentsError(user));
            return;
        }

        if (args[1].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                Util.sendMessage(channel, Util.argumentsError(user));
                return;
            }
            IUser u = Util.getUser(args[2], message);
            if(u == null) {
                EmbedBuilder em = new EmbedBuilder();
                em.withAuthorIcon(Util.botAva());
                em.withAuthorName("Error");
                em.withColor(Util.hexToColor(CColors.ERROR));
                em.withDesc("Could not find user based on input.");
                em.withFooterText("Requested by: " + Util.getNameAndDescrim(user));
                Util.sendMessage(channel, em.build());
                return;
            }

            User createdUser = new User(u);
            Database.saveUser(createdUser);
            GlobalVars.users.put(u.getLongID(), createdUser);
            EmbedBuilder em = new EmbedBuilder();
            em.withAuthorIcon(Util.botAva());
            em.withAuthorName("User create!");
            em.withColor(Util.hexToColor(CColors.BASIC));
            em.withDesc("User created with info: " + createdUser.toString());
            em.withFooterText("Requested by: " + Util.getNameAndDescrim(user));
            Util.sendMessage(channel, em.build());
            return;
        }
    }
}
