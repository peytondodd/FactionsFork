package com.benzimmer123.factionchest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;

public class ChestManager {

	P plugin;

	public ChestManager(P instance) {
		plugin = instance;
	}

	public void openChest(Player p, Faction fac, int chestNumber) {
		ArrayList<ItemStack> chest = (ArrayList<ItemStack>) getChest(fac, chestNumber);
		Inventory inv = Bukkit.createInventory(new ChestHolder(fac), 27, "Faction Chest " + chestNumber);

		for (ItemStack item : chest) {
			if (item != null)
				inv.addItem(item);
		}

		p.openInventory(inv);
	}
	
	public void deleteFactionFile(Faction fac){
		File factionLogs = new File(plugin.getDataFolder() + File.separator + "chests");

		if (!factionLogs.exists()) {
			createFiles();
		}

		File factionFile = new File(plugin.getDataFolder() + File.separator + "chests" + File.separator + fac.getId() + ".yml");

		if (factionFile.exists()) {
			factionFile.delete();
		}
	}

	public void createFiles() {
		File factionLogs = new File(plugin.getDataFolder() + File.separator + "chests");

		if (!factionLogs.exists()) {
			factionLogs.mkdir();
		}
	}

	public void createFactionFiles(Faction fac) {
		File factionLogs = new File(plugin.getDataFolder() + File.separator + "chests");

		if (!factionLogs.exists()) {
			createFiles();
		}

		File factionFile = new File(plugin.getDataFolder() + File.separator + "chests" + File.separator + fac.getId() + ".yml");

		if (!factionFile.exists()) {
			try {
				factionFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void logData(Faction fac, String data, int i) {
		File factionLogs = new File(plugin.getDataFolder() + File.separator + "chests");

		if (!factionLogs.exists()) {
			createFiles();
		}

		File factionFile = new File(plugin.getDataFolder() + File.separator + "chests" + File.separator + fac.getId() + ".yml");

		if (!factionFile.exists()) {
			try {
				factionFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		FileConfiguration folder = YamlConfiguration.loadConfiguration(factionFile);

		ArrayList<String> lines = new ArrayList<String>();

		lines = readFileAsList(fac, i);
		lines.add(data + "on " + dtf.format(now));
		folder.set("CHEST_LOGS." + i, lines);

		try {
			folder.save(factionFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> readFileAsList(Faction fac, int i) {
		File file = new File(plugin.getDataFolder() + File.separator + "chests" + File.separator + fac.getId() + ".yml");

		if (!file.exists())
			createFactionFiles(fac);

		FileConfiguration folder = YamlConfiguration.loadConfiguration(file);

		ArrayList<String> list = new ArrayList<String>();

		if (folder.isSet("CHEST_LOGS." + i)) {
			list = (ArrayList<String>) folder.getList("CHEST_LOGS." + i);
		}

		return (ArrayList<String>) list;
	}

	public void saveChest(Faction fac, int i, ArrayList<ItemStack> newChest) {
		File file = new File(plugin.getDataFolder() + File.separator + "chests" + File.separator + fac.getId() + ".yml");

		if (!file.exists())
			createFactionFiles(fac);

		FileConfiguration folder = YamlConfiguration.loadConfiguration(file);

		folder.set("CHEST." + i, newChest);

		try {
			folder.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ItemStack> getChest(Faction fac, int i) {
		File file = new File(plugin.getDataFolder() + File.separator + "chests" + File.separator + fac.getId() + ".yml");

		if (!file.exists())
			createFactionFiles(fac);

		FileConfiguration folder = YamlConfiguration.loadConfiguration(file);

		ArrayList<ItemStack> chest = new ArrayList<ItemStack>();
		
		if(folder.isSet("CHEST") && folder.isSet("CHEST." + i)){
			chest = (ArrayList<ItemStack>) folder.getList("CHEST." + i);
		}

		return chest;
	}
}
