package me.xaanit.apparatus.util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Util {

	/**
	 * Gets the current time in UTC
	 *
	 * @return The current time (UTC)
	 */
	public static String getCurrentTime() {
		LocalDateTime date = LocalDateTime.now();
		LocalTime time = date.toLocalTime().now(Clock.systemUTC());
		return (date.getDayOfWeek().toString().charAt(0)
				+ date.getDayOfWeek().toString().substring(1).toLowerCase())
				+ ", "
				+ (date.getMonth().toString().charAt(0)
				+ date.getMonth().toString().substring(1).toLowerCase())
				+ " " + date.getDayOfMonth() + " " + date.getYear() + " | "
				+ (time.getHour() > 12 ? time.getHour() - 12 : time) + ":" + time.getMinute() + ":"
				+ time.getSecond() + (time.getHour() > 12 ? " PM" : " AM");
	}

}
