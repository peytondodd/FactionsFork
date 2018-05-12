package com.benzimmer123.inspect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import net.coreprotect.database.Database;
import net.coreprotect.database.Lookup;
import net.coreprotect.model.Config;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.TL;

public class Inspect implements Listener {

	private List<Material> interact_blocks = Arrays.asList(new Material[] { Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR,
			Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.SPRUCE_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.JUNGLE_FENCE_GATE,
			Material.DARK_OAK_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.DISPENSER, Material.NOTE_BLOCK, Material.CHEST, Material.FURNACE,
			Material.BURNING_FURNACE, Material.WOODEN_DOOR, Material.LEVER, Material.STONE_BUTTON, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON,
			Material.TRAP_DOOR, Material.FENCE_GATE, Material.BREWING_STAND, Material.WOOD_BUTTON, Material.ANVIL, Material.TRAPPED_CHEST,
			Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.HOPPER, Material.DROPPER });

	private static Inspect i = new Inspect();

	public static Inspect get() {
		return i;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		FPlayer mPlayer = (FPlayer) FPlayers.getInstance().getByPlayer(event.getPlayer());

		if (mPlayer.isInspecting())
			mPlayer.setInspecting(false);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();

		if ((block == null) || (block.getType() == Material.AIR))
			return;

		FPlayer p = (FPlayer) FPlayers.getInstance().getByPlayer(player);

		if (p.isInspecting()) {
			event.setCancelled(true);

			if ((p.getFaction() == null || p.getFaction().getId().equalsIgnoreCase("0") || p.getFaction().getId().equalsIgnoreCase("none")
					|| p.getFaction().getId().equalsIgnoreCase("safezone") || p.getFaction().getId().equalsIgnoreCase("warzone"))) {
				p.setInspecting(false);
				p.msg(TL.COMMAND_INSPECT_DISABLE);
				return;
			}

			if (System.currentTimeMillis() - p.getLastInspectionTime() <= 3000L) {
				p.sendMessage(ChatColor.RED + "Please wait in-between inspections!");
				return;
			}

			p.setLastInspectionTime(System.currentTimeMillis());

			FLocation loc = new FLocation(block.getLocation());
			Faction faction = Board.getInstance().getFactionAt(loc);

			if (faction == null || faction.getId().equalsIgnoreCase("0") || faction.getId().equalsIgnoreCase("none")
					|| faction.getId().equalsIgnoreCase("safezone") || faction.getId().equalsIgnoreCase("warzone")) {
				p.sendMessage(ChatColor.RED + "You can only inspect land in your own faction!");
				return;
			}

			if (!faction.getId().equalsIgnoreCase(p.getFaction().getId())) {
				p.sendMessage(ChatColor.RED + "You can only inspect land in your own faction!");
				return;
			}

			handleInspection(p, block, block.getLocation());
		}
	}

	public boolean isReadyToInspect() {
		if (IntegrationCoreProtect.getCoreProtect() == null)
			return false;
		if (Config.converter_running)
			return false;
		if (Config.purge_running)
			return false;
		if (Database.getConnection(false) == null)
			return false;

		return true;
	}

	public void handleInspection(FPlayer player, Block b, Location clickedBlock) {
		if (!player.isInspecting())
			return;

		if (IntegrationCoreProtect.getCoreProtect() == null) {
			player.sendMessage(ChatColor.RED
					+ "This feature is currently disabled. Please contact an administrator if you believe this is a mistake.");
			return;
		}

		Role rel = player.getRole();

		if (!rel.isAtLeast(Role.MODERATOR)) {
			player.setInspecting(false);
			player.msg(TL.COMMAND_INSPECT_DISABLE);
			return;
		}

		if ((clickedBlock == null) || (clickedBlock.getBlock().getType() == Material.AIR)) {
			player.sendMessage(ChatColor.RED + "You can not inspect that block.");
			return;
		}

		if (!isReadyToInspect()) {
			player.setInspecting(false);
			player.sendMessage(ChatColor.RED + "You may not use /f inspect at this time. Please try again later!");
			return;
		}

		Connection connection = Database.getConnection(false);

		if (connection == null) {
			player.setInspecting(false);
			player.sendMessage(ChatColor.RED + "You may not use /f inspect at this time. Please try again later!");
			return;
		}

		try {
			Statement statement = connection.createStatement();
			Block block = clickedBlock.getBlock();
			String blockdata;

			if (interact_blocks.contains(block.getType())) {
				blockdata = Lookup.chest_transactions(statement, clickedBlock, player.getPlayer().getName(), 1, 10);
			} else {
				blockdata = Lookup.block_lookup(statement, block, player.getPlayer().getName(), 0, 1, 5);
			}

			if (blockdata.contains("\n")) {
				String[] arrayOfString;
				int j = (arrayOfString = blockdata.split("\n")).length;
				int index = 0;
				for (int i = 0; i < j; i++) {
					if (index == 0) {
						index++;
					} else {
						index++;

						String s = arrayOfString[i].replace("CoreProtect", "")
								.replace(ChatColor.translateAlternateColorCodes('&', "&3"), ChatColor.GOLD + "")
								.replace(ChatColor.translateAlternateColorCodes('&', "&f"), ChatColor.WHITE + "");

						if ((!s.contains("older data")) && (!s.contains("page")) && (!s.contains(ChatColor.WHITE + "-----"))) {
							String[] split = s.split("-");

							s = ChatColor.RED + "Information " + ChatColor.DARK_GRAY + "»" + split[1].replace(".", "") + ChatColor.DARK_GRAY
									+ " - " + split[0];

							player.getPlayer().sendMessage(s);
						}
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "No data was found for that block.");
			}
		} catch (SQLException e) {
			e.printStackTrace();

			player.setInspecting(false);
			player.sendMessage(ChatColor.RED + "You may not use /f inspect at this time. Please try again later. Please contact an administrator.");
		}
	}
}
