package com.benzimmer123.foptions;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;

public class OptionsListener implements Listener {

	P plugin;

	public OptionsListener(P instance) {
		plugin = instance;
	}

	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getName() != null) {
			if (e.getCurrentItem() != null) {

				if (e.getCurrentItem().getItemMeta() == null)
					return;

				if (e.getCurrentItem().getItemMeta().getDisplayName() == null)
					return;

				if (e.getInventory().getName().equalsIgnoreCase("Faction Options")) {
					e.setCancelled(true);

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
					facMotdMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change the faction MOTD.", ChatColor.GRAY
							+ "This is the messages faction members", ChatColor.GRAY + "will see when logging into the server."));
					facMotd.setItemMeta(facMotdMeta);

					ItemStack open = new ItemStack(Material.PAPER);
					ItemMeta openMeta = open.getItemMeta();
					openMeta.setDisplayName(ChatColor.GREEN + "Set if The Faction is Opened or Closed");
					openMeta.setLore(Arrays.asList(ChatColor.GRAY + "Set whether the faction is open", ChatColor.GRAY
							+ "or if it requires an invitation to join."));
					open.setItemMeta(openMeta);

					if (e.getCurrentItem().equals(open)) {
						e.getWhoClicked().closeInventory();
						Bukkit.getServer().dispatchCommand((Player) e.getWhoClicked(), "f open");
					} else if (e.getCurrentItem().equals(facMotd)) {
						e.getWhoClicked().closeInventory();
						Bukkit.getServer().dispatchCommand((Player) e.getWhoClicked(), "f setmotd");
					} else if (e.getCurrentItem().equals(wallCheck)) {
						e.getWhoClicked().closeInventory();

						FPlayer fp = FPlayers.getInstance().getByPlayer((Player) e.getWhoClicked());

						WallCheckGUI.open(fp);
					} else if (e.getCurrentItem().equals(alarmTime)) {
						e.getWhoClicked().closeInventory();

						FPlayer fp = FPlayers.getInstance().getByPlayer((Player) e.getWhoClicked());

						AlarmGUI.open(fp);
					}
				} else if (e.getInventory().getName().endsWith("Wall Check Time")) {
					e.setCancelled(true);
					e.getWhoClicked().closeInventory();
					
					if (e.getCurrentItem().getType().equals(Material.ARROW)) {
						new OptionsManager().openOptions((Player) e.getWhoClicked());
						return;
					}

					FPlayer fp = FPlayers.getInstance().getByPlayer((Player) e.getWhoClicked());
					Faction fac = fp.getFaction();

					String[] name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).split(" ");

					int amount = Integer.parseInt(name[0]);

					if (name[1].equalsIgnoreCase("Seconds") || name[1].equalsIgnoreCase("Second")) {
						fac.setWallCheckTime(amount);

						fp.sendMessage(ChatColor.GREEN + "You have set your wall check time to " + name[0] + " seconds.");
					} else if (name[1].equalsIgnoreCase("Minutes") || name[1].equalsIgnoreCase("Minute")) {
						fac.setWallCheckTime(amount * 60);

						fp.sendMessage(ChatColor.GREEN + "You have set your wall check time to " + name[0] + " minutes.");
					} else if (name[1].equalsIgnoreCase("Hours") || name[1].equalsIgnoreCase("Hour")) {
						fac.setWallCheckTime(amount * 60 * 60);

						fp.sendMessage(ChatColor.GREEN + "You have set your wall check time to " + name[0] + " hours.");

					}
				} else if (e.getInventory().getName().endsWith("Alarm Time")) {
					e.setCancelled(true);
					e.getWhoClicked().closeInventory();

					if (e.getCurrentItem().getType().equals(Material.ARROW)) {
						new OptionsManager().openOptions((Player) e.getWhoClicked());
						return;
					}

					FPlayer fp = FPlayers.getInstance().getByPlayer((Player) e.getWhoClicked());
					Faction fac = fp.getFaction();

					String[] name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).split(" ");

					int amount = Integer.parseInt(name[0]);

					if (name[1].equalsIgnoreCase("Seconds") || name[1].equalsIgnoreCase("Second")) {
						fac.setAlarmTime(amount);

						fp.sendMessage(ChatColor.GREEN + "You have set your faction alarm time to " + name[0] + " seconds.");
					} else if (name[1].equalsIgnoreCase("Minutes") || name[1].equalsIgnoreCase("Minute")) {
						fac.setAlarmTime(amount * 60);

						fp.sendMessage(ChatColor.GREEN + "You have set your faction alarm time to " + name[0] + " minutes.");
					} else if (name[1].equalsIgnoreCase("Hours") || name[1].equalsIgnoreCase("Hour")) {
						fac.setAlarmTime(amount * 60 * 60);

						fp.sendMessage(ChatColor.GREEN + "You have set your faction alarm time to " + name[0] + " hours.");
					}
				}
			}
		}
	}
}
