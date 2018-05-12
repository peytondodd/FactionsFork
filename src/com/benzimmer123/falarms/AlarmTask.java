package com.benzimmer123.falarms;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.P;

public class AlarmTask {

	private P p;

	public AlarmTask(P p) {
		this.p = p;
	}

	public static HashMap<Faction, Integer> alarmTimes = new HashMap<Faction, Integer>();
	public static HashMap<Faction, Integer> wallCheckTimes = new HashMap<Faction, Integer>();

	public void call() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {
			public void run() {
				for (Faction fac : Factions.getInstance().getAllFactions()) {
					if (fac == null || fac.getId().equalsIgnoreCase("0") || fac.getId().equalsIgnoreCase("-1") || fac.getTag().equalsIgnoreCase("-2"))
						continue;

					if (fac.getAlarmTime() == 0 || fac.getWallCheckTime() == 0)
						continue;

					if (alarmTimes.containsKey(fac)) {
						alarmTimes.put(fac, alarmTimes.get(fac) + 1);

						if (alarmTimes.get(fac) >= fac.getAlarmTime()) {
							alarmTimes.put(fac, 0);

							fac.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "[FACTION ALARM]" + ChatColor.RESET
									+ ChatColor.RED + " Your walls have not been checked! Do /f check to mark them as checked!");
						}
					} else {
						if (wallCheckTimes.containsKey(fac)) {
							wallCheckTimes.put(fac, wallCheckTimes.get(fac) + 1);
							
							if (wallCheckTimes.get(fac) >= fac.getWallCheckTime()) {
								alarmTimes.put(fac, 1);
								wallCheckTimes.remove(fac);

								fac.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "[FACTION ALARM]" + ChatColor.RESET
										+ ChatColor.RED + " Your walls have not been checked! Do /f check to mark them as checked!");
							}
						} else {
							wallCheckTimes.put(fac, 1);
						}
					}
				}
			}
		}, 0, 20);
	}
}
