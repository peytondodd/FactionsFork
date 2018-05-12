package com.benzimmer123.missions;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.util.TimeUtil;

public class MissionManager {

	private static MissionManager i = new MissionManager();

	private ArrayList<Mission> mission = new ArrayList<Mission>();

	public static MissionManager get() {
		return i;
	}

	public void load() {
		this.mission.add(new GlassBreak());
		this.mission.add(new Miner());
		this.mission.add(new BlazeKilling());
		this.mission.add(new SugarCane());
	}

	public ArrayList<Mission> getMission() {
		return this.mission;
	}

	public Mission getMissionByName(String string) {
		for (Mission mission1 : this.mission) {
			if (mission1.getMissionName().equalsIgnoreCase(string)) {
				return mission1;
			}
		}
		return null;
	}

	public void openInventory(Player player) {
		FPlayer fp = FPlayers.getInstance().getByPlayer(player);

		if (fp.getActiveMission() != null) {
			if (fp.getActiveMission() == null) {
				player.sendMessage("&cYou do not have any active missions!");
				return;
			}

			MissionData missionData = fp.getMissionData();
			Mission mission = getMissionByName(missionData.getActiveMission());

			int endTime = (int) (86400L - (System.currentTimeMillis() - missionData.getStartTime()) / 1000L);
			String endTimeString = TimeUtil.formatTime(endTime);

			if (endTime <= 1) {
				fp.endMission(EndReason.TIME);
				return;
			}

			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m-------------------------------"));

			player.sendMessage(" ");

			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					new StringBuilder().append("&cMission &8» &7").append(mission.getMissionName()).toString()));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					new StringBuilder().append("&cMission Description &8» &7").append(mission.getDescription(fp)).toString()));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					new StringBuilder().append("&cReward &8» &7").append(mission.getRewardDescription(fp)).toString()));
			player.sendMessage(ChatColor.translateAlternateColorCodes(
					'&',
					new StringBuilder().append("&cStarted &8» &7")
							.append(TimeUtil.formatTime(System.currentTimeMillis() - missionData.getStartTime())).toString()));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', new StringBuilder().append("&cEnd Time &8» &7 ").append(endTimeString)
					.toString()));

			player.sendMessage(" ");

			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					new StringBuilder().append("&cTo end this mission type &8» &7/f mission end").toString()));

			player.sendMessage(" ");

			switch (mission.getMissionName()) {
			case "GlassMiner":
				player.sendMessage(ChatColor.translateAlternateColorCodes(
						'&',
						new StringBuilder().append("&cProgression &8» &7").append(missionData.getGlassBroke()).append("&8/&7")
								.append(fp.getAmountNeeded(MissionManager.get().getMissionByName("GlassMiner"))).toString()));

				break;
			case "Miner":
				player.sendMessage(ChatColor.translateAlternateColorCodes(
						'&',
						new StringBuilder().append("&cProgression &8» &7").append(missionData.getBlocksMined()).append("&8/&7")
								.append(fp.getAmountNeeded(MissionManager.get().getMissionByName("Miner"))).toString()));

				break;
			case "BlazeMurderer":
				player.sendMessage(ChatColor.translateAlternateColorCodes(
						'&',
						new StringBuilder().append("&cProgression &8» &7").append(missionData.getBlazeMobsKilled()).append("&8/&7")
								.append(fp.getAmountNeeded(MissionManager.get().getMissionByName("BlazeMurderer"))).toString()));

				break;
			case "Harvester":
				player.sendMessage(ChatColor.translateAlternateColorCodes(
						'&',
						new StringBuilder().append("&cProgression &8» &7").append(missionData.getSugarCaneDestroyed()).append("&8/&7")
								.append(fp.getAmountNeeded(MissionManager.get().getMissionByName("Harvester"))).toString()));

				break;
			default:
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo Data found!"));
			}

			player.sendMessage(" ");

			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m-------------------------------"));

			return;
		}

		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&cPlayer Missions"));

		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta Gim = glass.getItemMeta();
		Gim.setDisplayName(" ");
		glass.setItemMeta(Gim);

		glass.setDurability((short) 7);

		int current = 10;

		for (int i = 0; i < 27; i++) {
			inv.setItem(i, glass);
		}

		for (Mission mission : this.mission) {
			long endTime = fp.getEndTime(mission.getMissionName()).longValue();
			ItemStack item;

			if (endTime == 0L)
				item = new ItemStack(mission.getDisplayItem(), 1);
			else {
				item = new ItemStack(Material.BARRIER, 1);
			}

			ItemMeta im = item.getItemMeta();

			im.setDisplayName(ChatColor.translateAlternateColorCodes('&',
					new StringBuilder().append(endTime == 0L ? "&c" : "&4").append(mission.getMissionName()).toString()));

			ArrayList<String> lore = new ArrayList<String>();

			lore.add("  ");
			lore.add(ChatColor.translateAlternateColorCodes('&', new StringBuilder().append("&cDescription &8» &7")
					.append(mission.getDescription(fp)).toString()));
			lore.add(ChatColor.translateAlternateColorCodes('&',
					new StringBuilder().append("&cReward &8» &7").append(mission.getRewardDescription(fp)).toString()));
			lore.add(" ");

			if (endTime == 0L) {
				lore.add(ChatColor.translateAlternateColorCodes('&', "&cStatus &8» &7Available"));
			} else {
				long time = 604800L - (System.currentTimeMillis() - endTime) / 1000L;

				String avaliableIn = TimeUtil.formatTime((int) time);

				lore.add(ChatColor.translateAlternateColorCodes('&', new StringBuilder().append("&cStatus &8» &7Available in ").append(avaliableIn)
						.toString()));
			}

			im.setLore(lore);
			item.setItemMeta(im);

			inv.setItem(current, item);
			current++;

			if (current < 9) {
				inv.setItem(current, glass);
			}

			current++;
		}

		player.openInventory(inv);
	}
}
