package com.benzimmer123.fupgrades;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.integration.Econ;

public class UpgradeListener implements Listener {

	public int SPAWNERRATE_COST = Conf.mobsSpawningUpgradeCost;
	public int EXPDROPS_COST = Conf.expDropsUpgradeCost;
	public int CROPGROWTH_COST = Conf.cropsGrowthUpgradeCost;
	public int PERCENTDAMAGEINCREASE_COST = Conf.percentDamageDecreaseCost;
	public int PERCENTDAMAGEDECREASE_COST = Conf.percentDamageIncreaseCost;
	public int FCHEST2UPGRADE_COST = Conf.fChest2UpgradeCost;
	public int MOREMEMBERSUPGRADE_COST = Conf.moreMembersUpgradeCost;
	public int TNTBANKUPGRADE_COST = Conf.tntBankUpgradeCost;
	public int MOREWARPSUPGRADE_COST = Conf.moreWarpsUpgradeCost;

	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getName() != null) {
			if (e.getCurrentItem() != null) {

				if (e.getCurrentItem().getItemMeta() == null)
					return;

				if (e.getCurrentItem().getItemMeta().getDisplayName() == null)
					return;

				if (e.getInventory().getName().equalsIgnoreCase("Faction Upgrades")) {
					e.setCancelled(true);

					ItemStack warps = new ItemStack(Material.ENDER_PEARL);
					ItemMeta warpsMeta = warps.getItemMeta();
					warpsMeta.setDisplayName(ChatColor.GREEN + "Upgrade Warps");
					warpsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase your faction warps", ChatColor.GRAY
							+ "This will increase it to 6 warps.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $"
							+ MOREWARPSUPGRADE_COST + ChatColor.GRAY + "."));
					warps.setItemMeta(warpsMeta);

					ItemStack spawnerRate = new ItemStack(Material.MOB_SPAWNER);
					ItemMeta spawnerRateMeta = spawnerRate.getItemMeta();
					spawnerRateMeta.setDisplayName(ChatColor.GREEN + "Increased Spawn Rate");
					spawnerRateMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase spawn rate by 50%", ChatColor.GRAY
							+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $"
							+ SPAWNERRATE_COST + ChatColor.GRAY + "."));
					spawnerRate.setItemMeta(spawnerRateMeta);

					ItemStack expDrops = new ItemStack(Material.EXP_BOTTLE);
					ItemMeta expDropsMeta = expDrops.getItemMeta();
					expDropsMeta.setDisplayName(ChatColor.GREEN + "Increased XP Drops");
					expDropsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase exp drops by 50%", ChatColor.GRAY
							+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $"
							+ EXPDROPS_COST + ChatColor.GRAY + "."));
					expDrops.setItemMeta(expDropsMeta);

					ItemStack tntBank = new ItemStack(Material.TNT);
					ItemMeta tntBankMeta = tntBank.getItemMeta();
					tntBankMeta.setDisplayName(ChatColor.GREEN + "Upgrade TNT Bank");
					tntBankMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase your TNT Bank size", ChatColor.GRAY
							+ "This will increase it to 10 dubs.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $"
							+ TNTBANKUPGRADE_COST + ChatColor.GRAY + "."));
					tntBank.setItemMeta(tntBankMeta);

					ItemStack cropGrowth = new ItemStack(Material.WHEAT);
					ItemMeta cropGrowthMeta = cropGrowth.getItemMeta();
					cropGrowthMeta.setDisplayName(ChatColor.GREEN + "Increased Crop Growth");
					cropGrowthMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase crops growth by 50%", ChatColor.GRAY
							+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $"
							+ CROPGROWTH_COST + ChatColor.GRAY + "."));
					cropGrowth.setItemMeta(cropGrowthMeta);

					ItemStack damageIncrease = new ItemStack(Material.DIAMOND_SWORD);
					ItemMeta damageIncreaseMeta = damageIncrease.getItemMeta();
					damageIncreaseMeta.setDisplayName(ChatColor.GREEN + "5% Damage Increase");
					damageIncreaseMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase damage by 5%", ChatColor.GRAY
							+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $"
							+ PERCENTDAMAGEINCREASE_COST + ChatColor.GRAY + "."));
					damageIncrease.setItemMeta(damageIncreaseMeta);

					ItemStack damageDecrease = new ItemStack(Material.WOOD_SWORD);
					ItemMeta damageDecreaseMeta = damageDecrease.getItemMeta();
					damageDecreaseMeta.setDisplayName(ChatColor.GREEN + "5% Damage Decrease");
					damageDecreaseMeta.setLore(Arrays.asList(ChatColor.GRAY + "Decrease damage by 5%", ChatColor.GRAY
							+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $"
							+ PERCENTDAMAGEDECREASE_COST + ChatColor.GRAY + "."));
					damageDecrease.setItemMeta(damageDecreaseMeta);

					ItemStack fChest = new ItemStack(Material.CHEST);
					ItemMeta fChestMeta = fChest.getItemMeta();
					fChestMeta.setDisplayName(ChatColor.GREEN + "Access to /f chest 2");
					fChestMeta.setLore(Arrays.asList(ChatColor.GRAY + "Gives you access to /f chest 2", ChatColor.GRAY
							+ "You will still have access to", ChatColor.GRAY + "The previous one with /f chest 1", ChatColor.GRAY
							+ "This will cost you" + ChatColor.GREEN + " $" + FCHEST2UPGRADE_COST + ChatColor.GRAY + "."));
					fChest.setItemMeta(fChestMeta);

					ItemStack moreMembers = new ItemStack(Material.MONSTER_EGG);
					ItemMeta moreMembersMeta = moreMembers.getItemMeta();
					moreMembersMeta.setDisplayName(ChatColor.GREEN + "Upgrade Faction Members");
					moreMembersMeta.setLore(Arrays.asList(ChatColor.GRAY + "This will give you the ability", ChatColor.GRAY
							+ "For your faction to hold up to 60", ChatColor.GRAY + "Faction members at one time.", ChatColor.GRAY
							+ "This will cost you" + ChatColor.GREEN + " $" + MOREMEMBERSUPGRADE_COST + ChatColor.GRAY + "."));
					moreMembers.setItemMeta(moreMembersMeta);

					Player p = (Player) e.getWhoClicked();
					FPlayer mplayer = FPlayers.getInstance().getByPlayer(p);
					Faction fac = mplayer.getFaction();

					if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.RED + "")) {
						p.closeInventory();
						p.sendMessage(ChatColor.RED
								+ "You either do not have enough money in your account to buy this or your faction has already purchased it.");
					} else if (e.getCurrentItem().equals(tntBank)) {
						chargePlayer(TNTBANKUPGRADE_COST, p);
						e.getWhoClicked().closeInventory();
						p.sendMessage(ChatColor.GREEN + "You have been charged $" + TNTBANKUPGRADE_COST
								+ " for buying your faction an extra 5 tnt bank dubs.");
						fac.setTNTBankUpgrade(true);
					} else if (e.getCurrentItem().equals(spawnerRate)) {
						chargePlayer(SPAWNERRATE_COST, p);
						e.getWhoClicked().closeInventory();
						p.sendMessage(ChatColor.GREEN + "You have been charged $" + SPAWNERRATE_COST
								+ " for buying your faction an upgrade which increases 50% of mob spawning rate.");
						fac.setMobSpawningBoost(true);
						new SpawnerRate().upgraded(fac);
					} else if (e.getCurrentItem().equals(warps)) {
						chargePlayer(MOREWARPSUPGRADE_COST, p);
						e.getWhoClicked().closeInventory();
						p.sendMessage(ChatColor.GREEN + "You have been charged $" + MOREWARPSUPGRADE_COST
								+ " for buying your faction an extra 3 warps.");
						fac.setWarpsUpgrade(true);
					}else if (e.getCurrentItem().equals(expDrops)) {
						chargePlayer(EXPDROPS_COST, p);
						e.getWhoClicked().closeInventory();
						p.sendMessage(ChatColor.GREEN + "You have been charged $" + EXPDROPS_COST
								+ " for buying your faction an upgrade which increases 50% of exp drops.");
						fac.setExpBoost(true);
					} else if (e.getCurrentItem().equals(cropGrowth)) {
						chargePlayer(CROPGROWTH_COST, p);
						p.sendMessage(ChatColor.GREEN + "You have been charged $" + CROPGROWTH_COST
								+ " for buying your faction an upgrade which increases 50% crop growth speed.");
						fac.setCropGrowthBoost(true);
						e.getWhoClicked().closeInventory();
					} else if (e.getCurrentItem().equals(damageIncrease)) {
						chargePlayer(PERCENTDAMAGEINCREASE_COST, p);
						p.sendMessage(ChatColor.GREEN + "You have been charged $" + PERCENTDAMAGEINCREASE_COST
								+ " for buying your faction an upgrade which increases 5% of damage.");
						fac.setDamageIncrease(true);
						e.getWhoClicked().closeInventory();
					} else if (e.getCurrentItem().equals(damageDecrease)) {
						chargePlayer(PERCENTDAMAGEDECREASE_COST, p);
						p.sendMessage(ChatColor.GREEN + "You have been charged $" + PERCENTDAMAGEDECREASE_COST
								+ " for buying your faction an upgrade which decreases 5% of damage.");
						fac.setDamageDecrease(true);
						e.getWhoClicked().closeInventory();
					} else if (e.getCurrentItem().equals(fChest)) {
						chargePlayer(FCHEST2UPGRADE_COST, p);
						p.sendMessage(ChatColor.GREEN + "You have been charged $" + FCHEST2UPGRADE_COST
								+ " for buying your faction a second /f chest.");
						fac.setSecondChest(true);
						e.getWhoClicked().closeInventory();
					} else if (e.getCurrentItem().equals(moreMembers)) {
						chargePlayer(MOREMEMBERSUPGRADE_COST, p);
						p.sendMessage(ChatColor.GREEN + "You have been charged $" + MOREMEMBERSUPGRADE_COST
								+ " for upgrading your member limit to 60 instead of 35.");
						fac.setMoreMembers(true);
						e.getWhoClicked().closeInventory();
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void chargePlayer(double amount, Player player) {
		Econ.econ.withdrawPlayer(player.getName(), amount);
	}
}
