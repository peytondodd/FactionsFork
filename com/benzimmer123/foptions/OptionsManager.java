package com.benzimmer123.foptions;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OptionsManager {

	Inventory invOptions;

	public void openOptions(Player p) {
		invOptions = Bukkit.createInventory(p, 27, "Faction Options");

		ItemStack alarmTime = new ItemStack(Material.PAPER);
		ItemMeta alarmTimeMeta = alarmTime.getItemMeta();
		alarmTimeMeta.setDisplayName(ChatColor.GREEN + "Manage Your Alarm Time");
		alarmTimeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Manage your time between faction alarms.", ChatColor.GRAY
				+ "If the time expires and nobody has", ChatColor.GRAY + "executed /f check, the alarm will sound."));
		alarmTime.setItemMeta(alarmTimeMeta);

		ItemStack wallCheck = new ItemStack(Material.PAPER);
		ItemMeta wallCheckMeta = wallCheck.getItemMeta();
		wallCheckMeta.setDisplayName(ChatColor.GREEN + "Manage Your WallCheck Time");
		wallCheckMeta.setLore(Arrays.asList(ChatColor.GRAY + "Manage your time between faction broadcasts.", ChatColor.GRAY
				+ "If the time expires and nobody has", ChatColor.GRAY + "executed /f check, it will broadcast messages.", ChatColor.GRAY
				+ "This is the time between those messages."));
		wallCheck.setItemMeta(wallCheckMeta);

		ItemStack facMotd = new ItemStack(Material.PAPER);
		ItemMeta facMotdMeta = facMotd.getItemMeta();
		facMotdMeta.setDisplayName(ChatColor.GREEN + "Change Your MOTD");
		facMotdMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change the faction MOTD.", ChatColor.GRAY + "This is the messages faction members",
				ChatColor.GRAY + "will see when logging into the server."));
		facMotd.setItemMeta(facMotdMeta);

		ItemStack open = new ItemStack(Material.PAPER);
		ItemMeta openMeta = open.getItemMeta();
		openMeta.setDisplayName(ChatColor.GREEN + "Set if The Faction is Opened or Closed");
		openMeta.setLore(Arrays.asList(ChatColor.GRAY + "Set whether the faction is open", ChatColor.GRAY
				+ "or if it requires an invitation to join."));
		open.setItemMeta(openMeta);

		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta glassmeta = glass.getItemMeta();
		glassmeta.setDisplayName(ChatColor.GOLD + " ");
		glass.setItemMeta(glassmeta);

		for (int i = 0; i < 27; i++) {
			invOptions.setItem(i, glass);
		}

		invOptions.setItem(10, alarmTime);
		invOptions.setItem(12, wallCheck);
		invOptions.setItem(14, facMotd);
		invOptions.setItem(16, open);

		p.openInventory(invOptions);
	}
}
