package com.benzimmer123.missions;

import org.bukkit.Material;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;

public abstract class Mission implements Listener {

	public abstract String getMissionName();

	public abstract String getDescription(FPlayer fp);

	public abstract Material getDisplayItem();

	public abstract String getRewardDescription(FPlayer fp);
	
	public abstract int getMoneyReward(FPlayer fp);

}
