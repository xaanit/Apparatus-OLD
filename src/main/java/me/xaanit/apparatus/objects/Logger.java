package me.xaanit.apparatus.objects;

import me.xaanit.apparatus.objects.enums.Level;
import me.xaanit.apparatus.util.Util;

import java.time.LocalDateTime;

public class Logger {

	private LocalDateTime date;

	public Logger() {
		this.date = LocalDateTime.now();
	}

	public void log(String str, Level level) {
		System.err.println("[" + Util.getCurrentTime() + "] " + level.toString() + ": " + str);
	}



}