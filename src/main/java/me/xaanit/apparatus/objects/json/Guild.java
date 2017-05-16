package me.xaanit.apparatus.objects.json;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.handle.obj.IGuild;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jacob on 4/21/2017.
 */
public class Guild {

    private long id;

    private String welcomeMessage;

    private long welcomeChannel;

    private String prefix;

    private List<Command> commands;

    private List<Modlog> modlogs;

    public Guild() {
        this.prefix = "+";
        this.commands = new ArrayList<>();
        this.modlogs = new ArrayList<>();

    }

    public Guild(IGuild guild) {
        this.id = guild.getLongID();
        this.prefix = "+";
        this.commands = new ArrayList<>();
        this.modlogs = new ArrayList<>();

    }


    public String getPrefix() {
        return prefix;
    }

    public void updateCommands() {
        for (String key : GlobalVars.commands.keySet()) {
            addCommand(GlobalVars.commands.get(key));
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public long getId() {
        return id;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void addCommand(ICommand command) {
        if (commands.stream().filter(c -> c.getName().equalsIgnoreCase(command.getName())).count() == 0)
            this.commands.add(new Command(command.getName()));
    }

    private void addCommand(Command command) {
        if (commands.stream().filter(c -> c.getName().equalsIgnoreCase(command.getName())).count() == 0)
            this.commands.add(command);
    }

    public void removeCommand(ICommand command) {
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).getName().equalsIgnoreCase(command.getName())) {
                commands.remove(i);
                return;
            }
        }

    }

    public List<Modlog> getModlogs() {
        return modlogs;
    }

    public void addModlog(String name) {
        if (modlogs.stream().filter(c -> c.getName().equalsIgnoreCase(name)).count() == 0)
            this.modlogs.add(new Modlog(name));
    }

    private void addModlog(Modlog m) {
        if (modlogs.stream().filter(c -> c.getName().equalsIgnoreCase(m.getName())).count() == 0)
            this.modlogs.add(m);
    }

    public Modlog getModlog(String name) {
        Modlog command = null;
        List<Modlog> cs = modlogs.stream().filter(c -> c.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (cs.isEmpty()) {
            command = new Modlog(name);
            addModlog(command);
        } else {
            command = cs.get(0);
        }
        return command;
    }


    public Command getCommand(String name) {
        Command command = null;
        List<Command> cs = commands.stream().filter(c -> c.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (cs.isEmpty()) {
            command = new Command(name);
            addCommand(command);
        } else {
            command = cs.get(0);
        }
        return command;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public long getWelcomeChannel() {
        return welcomeChannel;
    }

    public void setWelcomeChannel(long welcomeChannel) {
        this.welcomeChannel = welcomeChannel;
    }
}
