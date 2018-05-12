package com.massivecraft.factions.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;

public class FlightDisableUtil extends BukkitRunnable {

	ArrayList<FPlayer> flyingPlayers = new ArrayList<FPlayer>();

	public ArrayList<FPlayer> getFlying(){
		return flyingPlayers;
	}
	
	@Override
	public void run() {
		for (FPlayer player : flyingPlayers) {
			if (player.isFlying() && !player.isAdminBypassing()) {
				if (enemiesNearby(player, 30)) {
					player.setFFlying(false, false, true);
				}
			}
		}
	}

	public static boolean enemiesNearby(FPlayer target, int radius) {
		List<Entity> nearbyEntities = target.getPlayer().getNearbyEntities(radius, radius, radius);
		for (Entity entity : nearbyEntities) {
			if (entity instanceof Player) {
				FPlayer playerNearby = FPlayers.getInstance().getByPlayer((Player) entity);

				if (playerNearby.isAdminBypassing()) {
					continue;
				}

				if (!playerNearby.getFaction().equals(target.getFaction())
						&& !playerNearby.getFaction().getRelationTo(target.getFaction()).equals(Relation.TRUCE)
						&& !playerNearby.getFaction().getRelationTo(target.getFaction()).equals(Relation.ALLY) && !playerNearby.isStealth()) {
					return true;
				}
			}
		}
		return false;
	}
}
