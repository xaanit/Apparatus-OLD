package me.xaanit.apparatus.internal.json;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.internal.json.embeds.CustomEmbed;
import me.xaanit.apparatus.objects.enums.CmdType;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.handle.obj.IGuild;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Guild {

    private long id = 0;

    public boolean whitelistedGuild = false;

    private boolean devOverride = false;
    private boolean crashReports = true;

    private List<Long> barredUsers = new ArrayList<>();

    private List<JsonBuyableRole> buyableRoles = new ArrayList<>();

    private String welcomeMessage = "";
    private String goodbyeMessage = "";
    private CustomEmbed welcomeEmbed = new CustomEmbed();
    private CustomEmbed goodbyeEmbed = new CustomEmbed();

    private boolean useWelcomeEmbed = false;
    private boolean useGoodbyeEmbed = false;


    private List<JsonAutorole> autoRoles = new ArrayList<>();

    private List<Long> assignableRoles = new ArrayList<>();

    private long welcomeChannel = 0;

    private boolean welcomeOn = false;
    private boolean goodbyeOn = false;

    private String prefix = "+";

    private Stats stats = new Stats();

    private List<Command> commands = new ArrayList<>();

    private List<Modlog> modlogs = new ArrayList<>();

    public Guild() {
        this.prefix = "+";
        updateCommands();
    }

    public Guild(IGuild guild) {
        this.id = guild.getLongID();
        updateCommands();
    }


    public String getPrefix() {
        return prefix;
    }

    public void updateCommands() {
        for (String key : GlobalVars.commands.keySet()) {
            if (GlobalVars.commands.get(key).getType() != CmdType.DEV)
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

    public List<JsonAutorole> getAutoRoles() {
        return autoRoles;
    }

    private void addAutorole(long l, boolean val) {
        if (autoRoles.stream().filter(c -> c.getID() == l).count() == 0)
            this.autoRoles.add(new JsonAutorole(val, l));
    }

    public void removeAutorole(long l) {
        for (int i = 0; i < autoRoles.size(); i++) {
            if (autoRoles.get(i).getID() == l) {
                autoRoles.remove(i);
                return;
            }
        }

    }

    public List<Long> getAssignableRoles() {
        return assignableRoles;
    }


    public void addAssignableRole(long l) {
        if (assignableRoles.stream().filter(c -> c == l).count() == 0)
            this.assignableRoles.add(l);
    }

    public void removeAssignableRole(long l) {
        for (int i = 0; i < assignableRoles.size(); i++) {
            if (assignableRoles.get(i) == l) {
                assignableRoles.remove(i);
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

    public boolean isWelcomeOn() {
        return welcomeOn;
    }

    public void setWelcomeOn(boolean welcomeOn) {
        this.welcomeOn = welcomeOn;
    }

    public boolean isDevOverride() {
        return devOverride;
    }

    public void setDevOverride(boolean devOverride) {
        this.devOverride = devOverride;
    }

    public boolean isCrashReports() {
        return crashReports;
    }

    public void setCrashReports(boolean crashReports) {
        this.crashReports = crashReports;
    }

    public Stats getStats() {
        return stats;
    }

    public String getGoodbyeMessage() {
        return goodbyeMessage;
    }

    public void setGoodbyeMessage(String goodbyeMessage) {
        this.goodbyeMessage = goodbyeMessage;
    }

    public CustomEmbed getWelcomeEmbed() {
        return welcomeEmbed;
    }

    public void setWelcomeEmbed(CustomEmbed welcomeEmbed) {
        this.welcomeEmbed = welcomeEmbed;
    }

    public CustomEmbed getGoodbyeEmbed() {
        return goodbyeEmbed;
    }

    public void setGoodbyeEmbed(CustomEmbed goodbyeEmbed) {
        this.goodbyeEmbed = goodbyeEmbed;
    }

    public boolean isUseWelcomeEmbed() {
        return useWelcomeEmbed;
    }

    public void setUseWelcomeEmbed(boolean useWelcomeEmbed) {
        this.useWelcomeEmbed = useWelcomeEmbed;
    }

    public boolean isUseGoodbyeEmbed() {
        return useGoodbyeEmbed;
    }

    public void setUseGoodbyeEmbed(boolean useGoodbyeEmbed) {
        this.useGoodbyeEmbed = useGoodbyeEmbed;
    }

    public boolean isGoodbyeOn() {
        return goodbyeOn;
    }

    public void setGoodbyeOn(boolean goodbyeOn) {
        this.goodbyeOn = goodbyeOn;
    }

    public List<Long> getBarredUsers() {
        return barredUsers;
    }

    public void barUser(long id) {
        if (barredUsers.stream().filter(u -> u == id).count() == 0)
            barredUsers.add(id);
    }

    public void unbarUser(long id) {
        if (barredUsers.stream().filter(u -> u == id).count() == 1)
            barredUsers.remove(id);
    }
}
