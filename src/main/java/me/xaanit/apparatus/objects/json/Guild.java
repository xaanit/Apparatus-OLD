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

    private int deleteAfter;

    private String prefix;

    private List<Command> commands;

    public Guild() {
        this.deleteAfter = -1;
        this.prefix = "+";
        this.commands = new ArrayList<>();
    }

    public Guild(IGuild guild) {
        this.id = guild.getLongID();
        this.deleteAfter = -1;
        this.prefix = "+";
        this.commands = new ArrayList<>();

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

    public int getDeleteAfter() {
        return deleteAfter;
    }

    public void setDeleteAfter(int deleteAfter) {
        this.deleteAfter = deleteAfter;
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
}
