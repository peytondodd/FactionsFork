package com.benzimmer123.fupgrades;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

public class DamageEffects implements Listener {

	@EventHandler
	public void onDamageDecrease(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getDamager() instanceof Player) {
				Player player = (Player) e.getEntity();

				FPlayer mplayer = FPlayers.getInstance().getByPlayer(player);
				Faction faction = mplayer.getFaction();

				if (faction == null || faction.getId().equalsIgnoreCase("-1") || faction.getId().equalsIgnoreCase("-2")
						|| faction.getId().equalsIgnoreCase("0"))
					return;

				if (!mplayer.getFaction().getDamageIncrease())
					return;

				Faction facLocation = Board.getInstance().getFactionAt(new FLocation(mplayer.getPlayer().getLocation()));

				if (!facLocation.getId().equals(faction.getId()))
					return;
				
				double percent = 5 * e.getDamage();
				int total = (int) (percent / 100);

				e.setDamage(e.getDamage() - total);
			}
		}
	}

	@EventHandler
	public void onDamageIncrease(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getDamager() instanceof Player) {
				Player damager = (Player) e.getDamager();

				FPlayer mplayer = FPlayers.getInstance().getByPlayer(damager);
				Faction faction = mplayer.getFaction();

				if (faction == null || faction.getId().equalsIgnoreCase("-1") || faction.getId().equalsIgnoreCase("-2")
						|| faction.getId().equalsIgnoreCase("0"))
					return;

				if (!mplayer.getFaction().getDamageIncrease())
					return;

				Faction facLocation = Board.getInstance().getFactionAt(new FLocation(mplayer.getPlayer().getLocation()));

				if (!facLocation.getId().equals(faction.getId()))
					return;
				
				double percent = 5 * e.getDamage();
				int total = (int) (percent / 100);

				e.setDamage(e.getDamage() + total);
			}
		}
	}
}
