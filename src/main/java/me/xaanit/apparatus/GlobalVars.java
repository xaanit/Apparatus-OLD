package me.xaanit.apparatus;

import com.google.gson.Gson;
import me.xaanit.apparatus.internal.json.JsonConfig;
import me.xaanit.apparatus.internal.json.JsonGuild;
import me.xaanit.apparatus.objects.Logger;
import me.xaanit.apparatus.objects.interfaces.ICommand;
import sx.blah.discord.api.IDiscordClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jacob on 4/21/2017.
 */
public class GlobalVars {

    public static final String VERISON = "3.1.0 STABLE";

    public static final String PATH = "C:\\Users\\Jacob\\Desktop\\ApparatusInfo\\";

    public static Map<String, ICommand> commands = new HashMap<>();

    public static List<String> commandNames = new ArrayList<>();

    public static Map<Long, JsonGuild> guilds = new HashMap<>();

    public static Logger logger = new Logger();

    public static IDiscordClient client;

    public static Gson gson;

    public static JsonConfig config;

}
