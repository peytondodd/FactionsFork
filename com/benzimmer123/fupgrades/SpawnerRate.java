package com.benzimmer123.fupgrades;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

public class SpawnerRate implements Listener {

	public void upgraded(Faction faction) {
		for (FLocation fac : faction.getAllClaims()) {
			int x = (int) fac.getX();
			int z = (int) fac.getZ();

			for (BlockState state : Bukkit.getWorld(fac.getWorldName()).getChunkAt(x, z).getTileEntities()) {
				if ((state instanceof CreatureSpawner)) {
					CreatureSpawner spawner = (CreatureSpawner) state;
					spawner.setDelay(spawner.getDelay() / 2);
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		FPlayer mplayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
		Faction faction = mplayer.getFaction();
		Faction factionAt = Board.getInstance().getFactionAt(new FLocation(mplayer.getPlayer().getLocation()));

		if (event.getBlock().getType() != Material.MOB_SPAWNER)
			return;

		if (faction == null || faction.getId().equalsIgnoreCase("0") || faction.getId().equalsIgnoreCase("none")
				|| faction.getId().equalsIgnoreCase("safezone") || faction.getId().equalsIgnoreCase("warzone"))
			return;

		if (factionAt == null || factionAt.getId().equalsIgnoreCase("0") || factionAt.getId().equalsIgnoreCase("none")
				|| factionAt.getId().equalsIgnoreCase("safezone") || factionAt.getId().equalsIgnoreCase("warzone"))
			return;

		if ((mplayer.getFaction().getId().equalsIgnoreCase(factionAt.getId())) && (factionAt.getMobSpawningBoost())) {
			CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();
			creatureSpawner.setDelay(creatureSpawner.getDelay() / 2);
		}
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Faction factionAt = Board.getInstance().getFactionAt(new FLocation(event.getBlock().getLocation()));

		if ((event.getBlock().getType() == Material.MOB_SPAWNER) && (factionAt.getMobSpawningBoost())) {
			CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();
			creatureSpawner.setDelay(creatureSpawner.getDelay() * 2);
		}
	}
}
