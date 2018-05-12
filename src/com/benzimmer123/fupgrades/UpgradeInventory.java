package com.benzimmer123.fupgrades;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.integration.Econ;

public class UpgradeInventory {

	Inventory invUpgrades;

	public int SPAWNERRATE_COST = Conf.mobsSpawningUpgradeCost;
	public int EXPDROPS_COST = Conf.expDropsUpgradeCost;
	public int CROPGROWTH_COST = Conf.cropsGrowthUpgradeCost;
	public int PERCENTDAMAGEINCREASE_COST = Conf.percentDamageDecreaseCost;
	public int PERCENTDAMAGEDECREASE_COST = Conf.percentDamageIncreaseCost;
	public int FCHEST2UPGRADE_COST = Conf.fChest2UpgradeCost;
	public int MOREMEMBERSUPGRADE_COST = Conf.moreMembersUpgradeCost;
	public int TNTBANKUPGRADE_COST = Conf.tntBankUpgradeCost;
	public int MOREWARPSUPGRADE_COST = Conf.moreWarpsUpgradeCost;

	public void openUpgrades(Player p) {
		invUpgrades = Bukkit.createInventory(p, 36, "Faction Upgrades");

		FPlayer fp = FPlayers.getInstance().getByPlayer(p);
		Faction fac = fp.getFaction();

		ItemStack tntBank = new ItemStack(Material.TNT);
		ItemMeta tntBankMeta = tntBank.getItemMeta();

		if (hasAmount(TNTBANKUPGRADE_COST, p) && !fac.hasTNTBankUpgrade()) {
			tntBankMeta.setDisplayName(ChatColor.GREEN + "Upgrade TNT Bank");
			tntBankMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase your TNT Bank size", ChatColor.GRAY + "This will increase it to 10 dubs.",
					ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $" + TNTBANKUPGRADE_COST + ChatColor.GRAY + "."));
		} else if (fac.hasTNTBankUpgrade()) {
			tntBankMeta.setDisplayName(ChatColor.RED + "Upgrade TNT Bank");
			tntBankMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase your TNT Bank size", ChatColor.GRAY + "This will increase it to 10 dubs.",
					ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $" + TNTBANKUPGRADE_COST + ChatColor.GRAY + ".", "", ChatColor.GREEN
							+ "Obtained by your faction."));
		} else {
			tntBankMeta.setDisplayName(ChatColor.RED + "Upgrade TNT Bank");
			tntBankMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase your TNT Bank size", ChatColor.GRAY + "This will increase it to 10 dubs.",
					ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $" + TNTBANKUPGRADE_COST + ChatColor.GRAY + "."));
		}

		tntBank.setItemMeta(tntBankMeta);
		
		ItemStack warps = new ItemStack(Material.ENDER_PEARL);
		ItemMeta warpsMeta = warps.getItemMeta();

		if (hasAmount(MOREWARPSUPGRADE_COST, p) && !fac.hasWarpsUpgrade()) {
			warpsMeta.setDisplayName(ChatColor.GREEN + "Upgrade Warps");
			warpsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase your faction warps", ChatColor.GRAY + "This will increase it to 6 warps.",
					ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $" + MOREWARPSUPGRADE_COST + ChatColor.GRAY + "."));
		} else if (fac.hasWarpsUpgrade()) {
			warpsMeta.setDisplayName(ChatColor.RED + "Upgrade Warps");
			warpsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase your faction warps", ChatColor.GRAY + "This will increase it to 6 warps.",
					ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $" + MOREWARPSUPGRADE_COST + ChatColor.GRAY + ".", "", ChatColor.GREEN
							+ "Obtained by your faction."));
		} else {
			warpsMeta.setDisplayName(ChatColor.RED + "Upgrade Warps");
			warpsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase your faction warps", ChatColor.GRAY + "This will increase it to 6 warps.",
					ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $" + MOREWARPSUPGRADE_COST + ChatColor.GRAY + "."));
		}

		warps.setItemMeta(warpsMeta);

		ItemStack moreMembers = new ItemStack(Material.MONSTER_EGG);
		ItemMeta moreMembersMeta = moreMembers.getItemMeta();

		if (hasAmount(MOREMEMBERSUPGRADE_COST, p) && !fac.hasMoreMembers()) {
			moreMembersMeta.setDisplayName(ChatColor.GREEN + "Upgrade Faction Members");
			moreMembersMeta.setLore(Arrays.asList(ChatColor.GRAY + "This will give you the ability", ChatColor.GRAY
					+ "For your faction to hold up to 60", ChatColor.GRAY + "Faction members at one time.", ChatColor.GRAY + "This will cost you"
					+ ChatColor.GREEN + " $" + MOREMEMBERSUPGRADE_COST + ChatColor.GRAY + "."));
		} else if (fac.hasMoreMembers()) {
			moreMembersMeta.setDisplayName(ChatColor.RED + "Upgrade Faction Members");
			moreMembersMeta.setLore(Arrays.asList(ChatColor.GRAY + "This will give you the ability", ChatColor.GRAY
					+ "For your faction to hold up to 60", ChatColor.GRAY + "Faction members at one time.", ChatColor.GRAY + "This will cost you"
					+ ChatColor.RED + " $" + MOREMEMBERSUPGRADE_COST + ChatColor.GRAY + ".", "", ChatColor.GREEN + "Obtained by your faction."));
		} else {
			moreMembersMeta.setDisplayName(ChatColor.RED + "Upgrade Faction Members");
			moreMembersMeta.setLore(Arrays.asList(ChatColor.GRAY + "This will give you the ability", ChatColor.GRAY
					+ "For your faction to hold up to 60", ChatColor.GRAY + "Faction members at one time.", ChatColor.GRAY + "This will cost you"
					+ ChatColor.RED + " $" + MOREMEMBERSUPGRADE_COST + ChatColor.GRAY + "."));
		}

		moreMembers.setItemMeta(moreMembersMeta);

		ItemStack spawnerRate = new ItemStack(Material.MOB_SPAWNER);
		ItemMeta spawnerRateMeta = spawnerRate.getItemMeta();

		if (hasAmount(SPAWNERRATE_COST, p) && !fac.getMobSpawningBoost()) {
			spawnerRateMeta.setDisplayName(ChatColor.GREEN + "Increased Spawn Rate");
			spawnerRateMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase spawn rate by 50%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $" + SPAWNERRATE_COST
					+ ChatColor.GRAY + "."));
		} else if (fac.getMobSpawningBoost()) {
			spawnerRateMeta.setDisplayName(ChatColor.RED + "Increased Spawn Rate");
			spawnerRateMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase spawn rate by 50%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $" + SPAWNERRATE_COST
					+ ChatColor.GRAY + ".", "", ChatColor.GREEN + "Obtained by your faction."));
		} else {
			spawnerRateMeta.setDisplayName(ChatColor.RED + "Increased Spawn Rate");
			spawnerRateMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase spawn rate by 50%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $" + SPAWNERRATE_COST
					+ ChatColor.GRAY + "."));
		}

		spawnerRate.setItemMeta(spawnerRateMeta);

		ItemStack fChest = new ItemStack(Material.CHEST);
		ItemMeta fChestMeta = fChest.getItemMeta();

		if (hasAmount(FCHEST2UPGRADE_COST, p) && !fac.hasSecondChest()) {
			fChestMeta.setDisplayName(ChatColor.GREEN + "Access to /f chest 2");
			fChestMeta.setLore(Arrays.asList(ChatColor.GRAY + "Gives you access to /f chest 2", ChatColor.GRAY + "You will still have access to",
					ChatColor.GRAY + "The previous one with /f chest 1", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $"
							+ FCHEST2UPGRADE_COST + ChatColor.GRAY + "."));
		} else if (fac.hasSecondChest()) {
			fChestMeta.setDisplayName(ChatColor.RED + "Access to /f chest 2");
			fChestMeta.setLore(Arrays.asList(ChatColor.GRAY + "Gives you access to /f chest 2", ChatColor.GRAY + "You will still have access to",
					ChatColor.GRAY + "The previous one with /f chest 1", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $"
							+ FCHEST2UPGRADE_COST + ChatColor.GRAY + ".", "", ChatColor.GREEN + "Obtained by your faction."));
		} else {
			fChestMeta.setDisplayName(ChatColor.RED + "Access to /f chest 2");
			fChestMeta.setLore(Arrays.asList(ChatColor.GRAY + "Gives you access to /f chest 2", ChatColor.GRAY + "You will still have access to",
					ChatColor.GRAY + "The previous one with /f chest 1", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $"
							+ FCHEST2UPGRADE_COST + ChatColor.GRAY + "."));
		}

		fChest.setItemMeta(fChestMeta);

		ItemStack expDrops = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta expDropsMeta = expDrops.getItemMeta();

		if (hasAmount(EXPDROPS_COST, p) && !fac.getExpBoost()) {
			expDropsMeta.setDisplayName(ChatColor.GREEN + "Increased XP Drops");
			expDropsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase exp drops by 50%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $" + EXPDROPS_COST
					+ ChatColor.GRAY + "."));
		} else if (fac.getExpBoost()) {
			expDropsMeta.setDisplayName(ChatColor.RED + "Increased XP Drops");
			expDropsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase exp drops by 50%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $" + EXPDROPS_COST
					+ ChatColor.GRAY + ".", "", ChatColor.GREEN + "Obtained by your faction."));
		} else {
			expDropsMeta.setDisplayName(ChatColor.RED + "Increased XP Drops");
			expDropsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase exp drops by 50%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $" + EXPDROPS_COST
					+ ChatColor.GRAY + "."));
		}

		expDrops.setItemMeta(expDropsMeta);

		ItemStack cropGrowth = new ItemStack(Material.WHEAT);
		ItemMeta cropGrowthMeta = cropGrowth.getItemMeta();

		if (hasAmount(CROPGROWTH_COST, p) && !fac.getCropGrowthBoost()) {
			cropGrowthMeta.setDisplayName(ChatColor.GREEN + "Increased Crop Growth");
			cropGrowthMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase crops growth by 50%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $" + CROPGROWTH_COST
					+ ChatColor.GRAY + "."));
		} else if (fac.getCropGrowthBoost()) {
			cropGrowthMeta.setDisplayName(ChatColor.RED + "Increased Crop Growth");
			cropGrowthMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase crops growth by 50%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $" + CROPGROWTH_COST
					+ ChatColor.GRAY + ".", "", ChatColor.GREEN + "Obtained by your faction."));
		} else {
			cropGrowthMeta.setDisplayName(ChatColor.RED + "Increased Crop Growth");
			cropGrowthMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase crops growth by 50%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $" + CROPGROWTH_COST
					+ ChatColor.GRAY + "."));
		}

		cropGrowth.setItemMeta(cropGrowthMeta);

		ItemStack damageIncrease = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta damageIncreaseMeta = damageIncrease.getItemMeta();

		if (hasAmount(PERCENTDAMAGEINCREASE_COST, p) && !fac.getDamageIncrease()) {
			damageIncreaseMeta.setDisplayName(ChatColor.GREEN + "5% Damage Increase");
			damageIncreaseMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase damage by 5%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $"
					+ PERCENTDAMAGEINCREASE_COST + ChatColor.GRAY + "."));
		} else if (fac.getDamageIncrease()) {
			damageIncreaseMeta.setDisplayName(ChatColor.RED + "5% Damage Increase");
			damageIncreaseMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase damage by 5%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $"
					+ PERCENTDAMAGEINCREASE_COST + ChatColor.GRAY + ".", "", ChatColor.GREEN + "Obtained by your faction."));
		} else {
			damageIncreaseMeta.setDisplayName(ChatColor.RED + "5% Damage Increase");
			damageIncreaseMeta.setLore(Arrays.asList(ChatColor.GRAY + "Increase damage by 5%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $"
					+ PERCENTDAMAGEINCREASE_COST + ChatColor.GRAY + "."));
		}

		damageIncrease.setItemMeta(damageIncreaseMeta);

		ItemStack damageDecrease = new ItemStack(Material.WOOD_SWORD);
		ItemMeta damageDecreaseMeta = damageDecrease.getItemMeta();

		if (hasAmount(PERCENTDAMAGEDECREASE_COST, p) && !fac.getDamageDecrease()) {
			damageDecreaseMeta.setDisplayName(ChatColor.GREEN + "5% Damage Decrease");
			damageDecreaseMeta.setLore(Arrays.asList(ChatColor.GRAY + "Decrease damage by 5%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.GREEN + " $"
					+ PERCENTDAMAGEDECREASE_COST + ChatColor.GRAY + "."));
		} else if (fac.getDamageDecrease()) {
			damageDecreaseMeta.setDisplayName(ChatColor.RED + "5% Damage Decrease");
			damageDecreaseMeta.setLore(Arrays.asList(ChatColor.GRAY + "Decrease damage by 5%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $"
					+ PERCENTDAMAGEDECREASE_COST + ChatColor.GRAY + ".", "", ChatColor.GREEN + "Obtained by your faction."));
		} else {
			damageDecreaseMeta.setDisplayName(ChatColor.RED + "5% Damage Decrease");
			damageDecreaseMeta.setLore(Arrays.asList(ChatColor.GRAY + "Decrease damage by 5%", ChatColor.GRAY
					+ "This only applies in faction territory.", ChatColor.GRAY + "This will cost you" + ChatColor.RED + " $"
					+ PERCENTDAMAGEDECREASE_COST + ChatColor.GRAY + "."));
		}

		damageDecrease.setItemMeta(damageDecreaseMeta);

		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta glassmeta = glass.getItemMeta();
		glassmeta.setDisplayName(ChatColor.GOLD + " ");
		glass.setItemMeta(glassmeta);

		for (int i = 0; i < 36; i++) {
			invUpgrades.setItem(i, glass);
		}

		invUpgrades.setItem(10, spawnerRate);
		invUpgrades.setItem(12, expDrops);
		invUpgrades.setItem(14, damageDecrease);
		invUpgrades.setItem(16, damageIncrease);
		invUpgrades.setItem(18, tntBank);
		invUpgrades.setItem(20, cropGrowth);
		invUpgrades.setItem(22, fChest);
		invUpgrades.setItem(24, moreMembers);
		invUpgrades.setItem(26, warps);

		p.openInventory(invUpgrades);
	}

	@SuppressWarnings("deprecation")
	public boolean hasAmount(double amount, Player player) {
		if (Econ.econ.getBalance(player.getName()) >= amount)
			return true;

		return false;
	}
}
