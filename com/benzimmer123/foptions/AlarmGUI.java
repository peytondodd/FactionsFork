package com.benzimmer123.foptions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.FPlayer;

public class AlarmGUI {

	public static void open(FPlayer p) {
		Inventory inv = Bukkit.createInventory(null, 36, p.getFaction().getTag() + "'s Alarm Time");

		ItemStack seconds10 = new ItemStack(Material.PAPER);
		ItemMeta seconds10meta = seconds10.getItemMeta();
		seconds10meta.setDisplayName(ChatColor.GREEN + "10 Seconds");
		seconds10.setItemMeta(seconds10meta);

		ItemStack seconds20 = new ItemStack(Material.PAPER);
		ItemMeta seconds20meta = seconds20.getItemMeta();
		seconds20meta.setDisplayName(ChatColor.GREEN + "20 Seconds");
		seconds20.setItemMeta(seconds20meta);

		ItemStack seconds30 = new ItemStack(Material.PAPER);
		ItemMeta seconds30meta = seconds30.getItemMeta();
		seconds30meta.setDisplayName(ChatColor.GREEN + "30 Seconds");
		seconds30.setItemMeta(seconds30meta);
		
		ItemStack seconds45 = new ItemStack(Material.PAPER);
		ItemMeta seconds45meta = seconds45.getItemMeta();
		seconds45meta.setDisplayName(ChatColor.GREEN + "45 Seconds");
		seconds45.setItemMeta(seconds45meta);
		
		ItemStack minute1 = new ItemStack(Material.PAPER);
		ItemMeta minute1meta = minute1.getItemMeta();
		minute1meta.setDisplayName(ChatColor.GREEN + "1 Minute");
		minute1.setItemMeta(minute1meta);
		
		ItemStack minute2 = new ItemStack(Material.PAPER);
		ItemMeta minute2meta = minute2.getItemMeta();
		minute2meta.setDisplayName(ChatColor.GREEN + "2 Minutes");
		minute2.setItemMeta(minute1meta);

		ItemStack minute3 = new ItemStack(Material.PAPER);
		ItemMeta minute3meta = minute3.getItemMeta();
		minute3meta.setDisplayName(ChatColor.GREEN + "3 Minutes");
		minute3.setItemMeta(minute3meta);

		ItemStack minute4 = new ItemStack(Material.PAPER);
		ItemMeta minute4meta = minute4.getItemMeta();
		minute4meta.setDisplayName(ChatColor.GREEN + "4 Minutes");
		minute4.setItemMeta(minute4meta);

		ItemStack minute5 = new ItemStack(Material.PAPER);
		ItemMeta minute5meta = minute5.getItemMeta();
		minute5meta.setDisplayName(ChatColor.GREEN + "5 Minutes");
		minute5.setItemMeta(minute5meta);

		ItemStack minute6 = new ItemStack(Material.PAPER);
		ItemMeta minute6meta = minute6.getItemMeta();
		minute6meta.setDisplayName(ChatColor.GREEN + "6 Minutes");
		minute6.setItemMeta(minute6meta);

		ItemStack minute7 = new ItemStack(Material.PAPER);
		ItemMeta minute7meta = minute7.getItemMeta();
		minute7meta.setDisplayName(ChatColor.GREEN + "7 Minutes");
		minute7.setItemMeta(minute7meta);

		ItemStack minute8 = new ItemStack(Material.PAPER);
		ItemMeta minute8meta = minute8.getItemMeta();
		minute8meta.setDisplayName(ChatColor.GREEN + "8 Minutes");
		minute8.setItemMeta(minute8meta);

		ItemStack minute9 = new ItemStack(Material.PAPER);
		ItemMeta minute9meta = minute9.getItemMeta();
		minute9meta.setDisplayName(ChatColor.GREEN + "9 Minutes");
		minute9.setItemMeta(minute9meta);

		ItemStack minute10 = new ItemStack(Material.PAPER);
		ItemMeta minute10meta = minute10.getItemMeta();
		minute10meta.setDisplayName(ChatColor.GREEN + "10 Minutes");
		minute10.setItemMeta(minute10meta);

		ItemStack minute15 = new ItemStack(Material.PAPER);
		ItemMeta minute15meta = minute15.getItemMeta();
		minute15meta.setDisplayName(ChatColor.GREEN + "15 Minutes");
		minute15.setItemMeta(minute15meta);

		ItemStack minute20 = new ItemStack(Material.PAPER);
		ItemMeta minute20meta = minute20.getItemMeta();
		minute20meta.setDisplayName(ChatColor.GREEN + "20 Minutes");
		minute20.setItemMeta(minute20meta);

		ItemStack minute25 = new ItemStack(Material.PAPER);
		ItemMeta minute25meta = minute25.getItemMeta();
		minute25meta.setDisplayName(ChatColor.GREEN + "25 Minutes");
		minute25.setItemMeta(minute25meta);

		ItemStack minute30 = new ItemStack(Material.PAPER);
		ItemMeta minute30meta = minute30.getItemMeta();
		minute30meta.setDisplayName(ChatColor.GREEN + "30 Minutes");
		minute30.setItemMeta(minute30meta);

		ItemStack minute45 = new ItemStack(Material.PAPER);
		ItemMeta minute45meta = minute45.getItemMeta();
		minute45meta.setDisplayName(ChatColor.GREEN + "45 Minutes");
		minute45.setItemMeta(minute45meta);

		ItemStack hour1 = new ItemStack(Material.PAPER);
		ItemMeta hour1meta = hour1.getItemMeta();
		hour1meta.setDisplayName(ChatColor.GREEN + "1 Hour");
		hour1.setItemMeta(hour1meta);

		ItemStack hour2 = new ItemStack(Material.PAPER);
		ItemMeta hour2meta = hour2.getItemMeta();
		hour2meta.setDisplayName(ChatColor.GREEN + "2 Hours");
		hour2.setItemMeta(hour2meta);
		
		ItemStack hour3 = new ItemStack(Material.PAPER);
		ItemMeta hour3meta = hour3.getItemMeta();
		hour3meta.setDisplayName(ChatColor.GREEN + "3 Hours");
		hour3.setItemMeta(hour3meta);

		ItemStack goBack = new ItemStack(Material.ARROW);
		ItemMeta goBackmeta = goBack.getItemMeta();
		goBackmeta.setDisplayName(ChatColor.GREEN + "Go Back");
		goBack.setItemMeta(goBackmeta);

		inv.setItem(0, seconds10);
		inv.setItem(1, seconds20);
		inv.setItem(2, seconds30);
		inv.setItem(3, seconds45);
		inv.setItem(4, minute1);
		inv.setItem(5, minute2);
		inv.setItem(6, minute3);
		inv.setItem(7, minute4);
		inv.setItem(8, minute5);
		inv.setItem(9, minute6);
		inv.setItem(10, minute7);
		inv.setItem(11, minute8);
		inv.setItem(12, minute9);
		inv.setItem(13, minute10);
		inv.setItem(14, minute15);
		inv.setItem(15, minute20);
		inv.setItem(16, minute25);
		inv.setItem(17, minute30);
		inv.setItem(18, minute45);
		inv.setItem(19, hour1);
		inv.setItem(20, hour2);
		inv.setItem(20, hour3);
		inv.setItem(31, goBack);

		p.getPlayer().openInventory(inv);
	}
	
}
