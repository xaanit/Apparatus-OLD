package me.xaanit.apparatus.internal.json;


public class Stats {

    private int commandExecuted = 0;

    public int getCommandExecuted() {
        return commandExecuted;
    }

    public void increaseCommandsExecuted() {
        this.commandExecuted++;
    }

    public void decreaseCommandsExecuted() {
        this.commandExecuted--;
    }
}
