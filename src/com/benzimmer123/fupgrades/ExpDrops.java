package com.benzimmer123.fupgrades;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

public class ExpDrops implements Listener {

	@EventHandler
	public void entityDeath(EntityDeathEvent e) {
		if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player
				&& !(e.getEntity() instanceof Player)) {
			Player player = e.getEntity().getKiller();
			FPlayer mplayer = FPlayers.getInstance().getByPlayer(player);
			Faction faction = mplayer.getFaction();

			if (faction == null || faction.getId().equalsIgnoreCase("0") || faction.getId().equalsIgnoreCase("none")
					|| faction.getId().equalsIgnoreCase("safezone") || faction.getId().equalsIgnoreCase("warzone"))
				return;

			if (!mplayer.getFaction().getCropGrowthBoost())
				return;

			Faction facLocation = Board.getInstance().getFactionAt(new FLocation(mplayer.getPlayer().getLocation()));

			if (!facLocation.getId().equals(faction.getId()))
				return;

			int half = e.getDroppedExp() / 2;
			e.setDroppedExp(e.getDroppedExp() + half);
		}
	}
}
