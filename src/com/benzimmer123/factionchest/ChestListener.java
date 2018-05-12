package com.benzimmer123.factionchest;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;

public class ChestListener implements Listener {

	P p;

	public ChestListener(P instance) {
		p = instance;
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		Inventory inv = e.getInventory();

		if (!inv.getName().equalsIgnoreCase("Faction Chest 1") && !inv.getName().equalsIgnoreCase("Faction Chest 2"))
			return;

		FPlayer fp = FPlayers.getInstance().getByPlayer(p);
		Faction fac = fp.getFaction();

		String[] invName = inv.getName().split(" ");

		int i = Integer.parseInt(invName[2]);

		ArrayList<ItemStack> chestContents = (ArrayList<ItemStack>) new ChestManager(this.p).getChest(fac, i);
		ArrayList<ItemStack> newChestContents = new ArrayList<ItemStack>();

		for (ItemStack newChest : inv.getContents()) {
			newChestContents.add(newChest);
		}

		if (chestContents.equals(newChestContents))
			return;

		ArrayList<ItemStack> removed = new ArrayList<ItemStack>();
		ArrayList<ItemStack> added = new ArrayList<ItemStack>();

		for (ItemStack item : newChestContents) {
			if (item != null)
				if (!chestContents.contains(item)) {
					added.add(item);
				}
		}

		for (ItemStack item : chestContents) {
			if (item != null)
				if (!newChestContents.contains(item)) {
					removed.add(item);
				}
		}

		for (ItemStack allRemoved : removed) {
			if (allRemoved != null)
				new ChestManager(this.p).logData(fac,
						"&9" + p.getName() + " &7removed " + "&9x" + allRemoved.getAmount() + " &7of &9" + allRemoved.getType() + " &7", i);
		}

		for (ItemStack allAdded : added) {
			if (allAdded != null)
				new ChestManager(this.p).logData(fac,
						"&9" + p.getName() + " &7added " + "&9x" + allAdded.getAmount() + " &7of &9" + allAdded.getType() + " &7", i);
		}

		 new ChestManager(this.p).saveChest(fac, i, newChestContents);
	}
}
