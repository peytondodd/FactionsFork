package com.benzimmer123.missions;

import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class MissionData {
	private final String activeMission;
	private final Player player;
	private final long startTime;
	private int totalBlazeKilled;
	private int blocksMined;
	private int glassBroke;
	private int sugarCaneDestroyed;

	public MissionData(String activeMission, Player player, int sugarCaneDestroyed, int glassBroke, int totalBlazeKilled,  int blocksMined) {
		this.activeMission = activeMission;
		this.player = player;
		this.totalBlazeKilled = totalBlazeKilled;
		this.blocksMined = blocksMined;
		this.sugarCaneDestroyed = sugarCaneDestroyed;
		this.glassBroke = glassBroke;

		this.startTime = System.currentTimeMillis();
	}

	public String getActiveMission() {
		return this.activeMission;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public int getBlocksMined() {
		return this.blocksMined;
	}

	public int getBlazeMobsKilled() {
		return this.totalBlazeKilled;
	}

	public int getGlassBroke() {
		return this.glassBroke;
	}
	
	public int getSugarCaneDestroyed() {
		return this.sugarCaneDestroyed;
	}

	public void addBlazeKill() {
		this.totalBlazeKilled += 1;

		FPlayer fp = FPlayers.getInstance().getByPlayer(player);

		if (this.totalBlazeKilled >= fp.getAmountNeeded(MissionManager.get().getMissionByName("BlazeMurderer"))) {
			fp.endMission(EndReason.COMPLETE);
		}
	}

	public void addGlassBroke() {
		this.glassBroke += 1;

		FPlayer fp = FPlayers.getInstance().getByPlayer(player);

		if (this.glassBroke >= fp.getAmountNeeded(MissionManager.get().getMissionByName("GlassMiner"))) {
			fp.endMission(EndReason.COMPLETE);
		}
	}
	
	public void addBlockMined() {
		this.blocksMined += 1;

		FPlayer fp = FPlayers.getInstance().getByPlayer(player);

		if (this.blocksMined >= fp.getAmountNeeded(MissionManager.get().getMissionByName("Miner"))) {
			fp.endMission(EndReason.COMPLETE);
		}
	}
	
	public void addSugarCaneDestroyed() {
		this.sugarCaneDestroyed += 1;

		FPlayer fp = FPlayers.getInstance().getByPlayer(player);

		if (this.sugarCaneDestroyed >= fp.getAmountNeeded(MissionManager.get().getMissionByName("Harvester"))) {
			fp.endMission(EndReason.COMPLETE);
		}
	}
}
