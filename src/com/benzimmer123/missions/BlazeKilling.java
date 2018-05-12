package com.benzimmer123.missions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class BlazeKilling extends Mission {

	public String getMissionName() {
		return "BlazeMurderer";
	}

	public String getDescription(FPlayer fp) {
		return "Kill " + fp.getAmountNeeded(this) + " blaze!";
	}

	public Material getDisplayItem() {
		return Material.PAPER;
	}

	public String getRewardDescription(FPlayer fp) {
		return "$" + fp.getReward(this);
	}

	public int getMoneyReward(FPlayer fp) {
		return fp.getReward(this);
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		if ((event.getEntity().getKiller() != null) && ((event.getEntity().getKiller() instanceof Player))) {
			Player player = event.getEntity().getKiller();
			FPlayer fp = FPlayers.getInstance().getByPlayer(player);

			if (fp.getActiveMission() == null)
				return;

			if (!fp.getActiveMission().getMissionName().equalsIgnoreCase(getMissionName()))
				return;

			fp.getMissionData().addBlazeKill();
		}
	}
}
