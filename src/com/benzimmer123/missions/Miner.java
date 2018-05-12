package com.benzimmer123.missions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class Miner extends Mission {

	public String getMissionName() {
		return "Miner";
	}

	public String getDescription(FPlayer fp) {
		return "Mine " + fp.getAmountNeeded(this) + " blocks!";
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
	public void onPlace(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();
		FPlayer fp = FPlayers.getInstance().getByPlayer(player);

		if (fp.getActiveMission() == null)
			return;

		if (!fp.getActiveMission().getMissionName().equalsIgnoreCase(getMissionName()))
			return;

		fp.getMissionData().addBlockMined();
	}
}
