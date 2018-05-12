package com.benzimmer123.fupgrades;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;

public class CropGrowth implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void blockGrow(BlockGrowEvent e) {
		Faction fac = Board.getInstance().getFactionAt(new FLocation(e.getBlock().getLocation()));

		if (fac == null || fac.getId().equalsIgnoreCase("0") || fac.getId().equalsIgnoreCase("none")
				|| fac.getId().equalsIgnoreCase("safezone") || fac.getId().equalsIgnoreCase("warzone"))
			return;
		
		if (!fac.getCropGrowthBoost())
			return;

		e.getBlock().setData((byte) (e.getBlock().getData() + 1));
	}
}
