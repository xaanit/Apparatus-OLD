package me.xaanit.apparatus;

import java.util.HashMap;
import java.util.Map;
import me.xaanit.apparatus.objects.Logger;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

/**
 * Created by Jacob on 4/21/2017.
 */
public class GlobalVars {

	public static String VERISON = "3.0.0 STABLE";

	public static Map<String, ICommand> commands = new HashMap<>();

	public static Logger logger = new Logger();

	public static IGuild sponseredGuild;

	public static IDiscordClient client;

}
