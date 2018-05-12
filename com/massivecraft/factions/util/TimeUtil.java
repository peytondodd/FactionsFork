package com.massivecraft.factions.util;

import java.util.concurrent.TimeUnit;

public class TimeUtil {
	public static String formatTime(long timePeriod) {
		long millis = timePeriod;

		String output = "";

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		if (days > 1L)
			output = new StringBuilder().append(output).append(days).append(" days ").toString();
		else if (days == 1L)
			output = new StringBuilder().append(output).append(days).append(" day ").toString();

		if (hours > 1L)
			output = new StringBuilder().append(output).append(hours).append(" hours ").toString();
		else if (hours == 1L)
			output = new StringBuilder().append(output).append(hours).append(" hour ").toString();

		if (minutes > 1L)
			output = new StringBuilder().append(output).append(minutes).append(" minutes ").toString();
		else if (minutes == 1L)
			output = new StringBuilder().append(output).append(minutes).append(" minute ").toString();

		if (seconds > 1L)
			output = new StringBuilder().append(output).append(seconds).append(" seconds").toString();
		else if (seconds == 1L)
			output = new StringBuilder().append(output).append(seconds).append(" second").toString();

		if (output.isEmpty())
			return "just now";

		return output;
	}

	public static String formatTime(int seconds) {
		int days = seconds / 86400;
		int hours = seconds % 86400 / 3600;
		int minutes = seconds % 86400 % 3600 / 60;

		StringBuilder sb = new StringBuilder();

		if (days != 0) {
			if (days > 1)
				sb.append(new StringBuilder().append(days).append(" days ").toString());
			else if (days == 1)
				sb.append("1 day ");
		}

		if (hours != 0) {
			if (hours > 1)
				sb.append(new StringBuilder().append(hours).append(" hours ").toString());
			else if (hours == 1)
				sb.append("1 hour ");
		}

		if (minutes != 0) {
			if (minutes > 1)
				sb.append(new StringBuilder().append(minutes).append(" minutes").toString());
			else if (minutes == 1)
				sb.append("1 minute ");
		}

		if (sb.toString().isEmpty())
			return "just now";

		return sb.toString();
	}
}