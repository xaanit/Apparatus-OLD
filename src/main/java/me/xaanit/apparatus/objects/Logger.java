package me.xaanit.apparatus.objects;

import java.time.LocalDateTime;
import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.objects.enums.LogType;
import me.xaanit.apparatus.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

public class Logger {

	private LocalDateTime date;

	public Logger() {
		this.date = LocalDateTime.now();
	}

	private void console(String str, Level level) {
		System.err.println("[" + Util.getCurrentTime() + "] " + level.toString() + ": " + str);
	}

	private void discord(String str, Level level) {
		EmbedBuilder em = new EmbedBuilder();
		em.withAuthorIcon(GlobalVars.sponseredGuild.getIconURL());
		em.withAuthorName("Log");
		em.withTitle(level.toString());
		em.withDesc(str);
		em.withFooterText(Util.getCurrentTime());
		IChannel log = GlobalVars.sponseredGuild.getChannelByID(Long.parseLong("290740717792526346"));


	}

	public void log(LogType type, String str, Level level) {
		if(type == LogType.DISCORD) {
			discord(str, level);
		} else {
			console(str, level);
		}
	}


}