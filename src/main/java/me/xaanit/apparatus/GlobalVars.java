package me.xaanit.apparatus;

import com.google.gson.Gson;
import me.xaanit.apparatus.objects.Logger;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import me.xaanit.apparatus.objects.json.Config;
import me.xaanit.apparatus.objects.json.Guild;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jacob on 4/21/2017.
 */
public class GlobalVars {

    public static final String VERISON = "3.0.0 STABLE";

    public static final String PATH = "C:\\Users\\Jacob\\Desktop\\ApparatusInfo\\";

    public static Map<String, ICommand> commands = new HashMap<>();

    public static Map<Long, Guild> guilds = new HashMap<>();

    public static Logger logger = new Logger();

    public static IGuild sponseredGuild;

    public static IDiscordClient client;

    public static Gson gson;

    public static Config config;

}
