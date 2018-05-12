package com.benzimmer123.missions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class MissionClickListener implements Listener {

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if ((event.getMessage().equalsIgnoreCase("/f missions claim")) || (event.getMessage().equalsIgnoreCase("/f mission claim"))) {
			event.setCancelled(true);

			Player player = event.getPlayer();
			FPlayer fp = FPlayers.getInstance().getByPlayer(player);

			if (!fp.hasRedeemableMissions()) {
				player.sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.RED + "You have no rewards to claim!");
				return;
			}

			fp.claimRewards();
		}

		if ((event.getMessage().equalsIgnoreCase("/f missions end")) || (event.getMessage().equalsIgnoreCase("/f mission end"))) {
			event.setCancelled(true);

			Player player = event.getPlayer();
			FPlayer fp = FPlayers.getInstance().getByPlayer(player);

			if (fp.getActiveMission() == null) {
				player.sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.RED + "You currently have no active missions!");
				return;
			}

			fp.endMission(EndReason.PLAYER);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;

		if (event.getCurrentItem() == null)
			return;

		if (event.getInventory() == null)
			return;

		if (!event.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "Player Missions"))
			return;

		event.setCancelled(true);

		Player player = (Player) event.getWhoClicked();
		FPlayer fp = FPlayers.getInstance().getByPlayer(player);

		ItemStack item = event.getCurrentItem();

		if (item.getType() == Material.STAINED_GLASS_PANE)
			return;

		if (!item.hasItemMeta())
			return;
		if (!item.getItemMeta().hasDisplayName())
			return;
		if (!item.getItemMeta().hasLore())
			return;

		String SCitemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

		player.closeInventory();

		if (fp.getActiveMission() != null) {
			player.sendMessage(ChatColor.RED + "You already have an active mission!");
			return;
		}

		Mission mission = MissionManager.get().getMissionByName(SCitemName);

		if (mission == null) {
			player.sendMessage(ChatColor.RED + "That mission is not a mission.");
			return;
		}

		long endTime = fp.getEndTime(SCitemName).longValue();

		if (endTime > 0L) {
			player.sendMessage(ChatColor.RED + "That mission is currently not available.");
			return;
		}

		fp.startMission(mission);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8» &cYou &7have started the &c" + mission.getMissionName() + " &7mission!"));
	}
}
