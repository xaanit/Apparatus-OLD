package me.xaanit.apparatus.util;

import java.awt.Color;
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


	/**
	 * Returns an integer array of [R, G, B] from a provided hex
	 *
	 * @param hex The hex to convert.
	 * @return A new int[] of R G B values
	 */
	public static int[] hexToRGB(String hex) {
		hex = hex.replace("#", "");
		int r = Integer.valueOf(hex.substring(0, 2), 16);
		int g = Integer.valueOf(hex.substring(2, 4), 16);
		int b = Integer.valueOf(hex.substring(4, 6), 16);

		return new int[] {r, g, b};

	}

	/**
	 * Returns a new Color object from the provided hex.
	 *
	 * @param hex The hex to convert
	 * @return A color object.
	 */
	public static Color hexToColor(String hex) {
		int[] arr = hexToRGB(hex);
		return new Color(arr[0], arr[1], arr[2]);
	}

}
