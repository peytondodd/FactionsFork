package com.massivecraft.factions.cmd;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdTNTFill extends FCommand {

	public CmdTNTFill() {
		this.aliases.add("tntfill");

		this.requiredArgs.add("amount");

		this.permission = Permission.TNT_FILL.node;
		this.senderMustBeMember = true;
		this.senderMustBeModerator = true;
	}

	@SuppressWarnings("unused")
	@Override
	public void perform() {
		Access access = myFaction.getAccess(fme, PermissableAction.TNTFILL);
		// This statement allows us to check if they've specifically denied it,
		// or default to
		// the old setting of allowing moderators to set warps.
		if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
			fme.msg(TL.GENERIC_NOPERMISSION, "fill nearby dispensers");
			return;
		}

		msg(TL.COMMAND_TNTFILL_HEADER);
		int amount = argAsInt(0, 16);

		int testNumber = -1;
		try {
			testNumber = Integer.parseInt(args.get(0));
		} catch (NumberFormatException e) {
			fme.msg(TL.COMMAND_TNT_INVALID_NUM);
			return;
		}
		if (amount < 0) {
			fme.msg(TL.COMMAND_TNT_POSITIVE);
			return;
		}

		Location start = me.getLocation();
		int radius = 16;

		for (double x = start.getX() - radius; x <= start.getX() + radius; x++) {
			for (double y = start.getY() - radius; y <= start.getY() + radius; y++) {
				for (double z = start.getZ() - radius; z <= start.getZ() + radius; z++) {
					Location blockLoc = new Location(start.getWorld(), x, y, z);
					if (blockLoc.getBlock().getState() instanceof Dispenser) {
						Dispenser disp = (Dispenser) blockLoc.getBlock().getState();
						Inventory dispenser = disp.getInventory();
						if (canHold(dispenser, amount)) {
							int fullStacks = amount / 64;
							int remainderAmt = amount % 64;

							if (!getBankTNT(amount)) {
								msg(ChatColor.RED.toString() + ChatColor.BOLD + "[!]" + ChatColor.RESET.toString() + ChatColor.GRAY
										+ " Not enough TNT in your faction bank to continue filling dispensers.");
								return;
							}

							ItemStack tnt64 = new ItemStack(Material.TNT, 64);
							for (int i = 0; i <= fullStacks - 1; i++) {
								dispenser.addItem(tnt64);
							}

							if (remainderAmt != 0) {
								ItemStack tnt = new ItemStack(Material.TNT, remainderAmt);
								dispenser.addItem(tnt);
							}

							sendMessage(TL.COMMAND_TNTFILL_SUCCESS.toString().replace("{amount}", amount + "").replace("{x}", (int) x + "")
									.replace("{y}", (int) y + "").replace("{z}", (int) z + ""));
						}
					}
				}
			}
		}
	}

	public boolean getBankTNT(int amount) {
		try {
		} catch (NumberFormatException e) {
			return false;
		}

		if (fme.getFaction().getTnt() < amount) {
			return false;
		}

		fme.getFaction().takeTnt(amount);
		return true;
	}

	public boolean canHold(Inventory inventory, int amount) {
		int fullStacks = amount / 64;
		int remainderAmt = amount % 64;
		if ((remainderAmt == 0 && getEmptySlots(inventory) <= fullStacks)) {
			return false;
		}
		if (getEmptySlots(inventory) + 1 <= fullStacks) {
			fme.msg(ChatColor.RED.toString() + ChatColor.BOLD + "[!]" + ChatColor.RESET.toString() + ChatColor.GRAY
					+ " Nearby dispenser is full, continuing...");
			return false;
		}
		return true;
	}

	public boolean inventoryContains(Inventory inventory, ItemStack item) {
		int count = 0;
		ItemStack[] items = inventory.getContents();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getType() == item.getType() && items[i].getDurability() == item.getDurability()) {
				count += items[i].getAmount();
			}
			if (count >= item.getAmount()) {
				return true;
			}
		}
		return false;
	}

	public void removeFromInventory(Inventory inventory, ItemStack item) {
		int amt = item.getAmount();
		ItemStack[] items = inventory.getContents();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getType() == item.getType() && items[i].getDurability() == item.getDurability()) {
				if (items[i].getAmount() > amt) {
					items[i].setAmount(items[i].getAmount() - amt);
					break;
				} else if (items[i].getAmount() == amt) {
					items[i] = null;
					break;
				} else {
					amt -= items[i].getAmount();
					items[i] = null;
				}
			}
		}
		inventory.setContents(items);
	}

	public int getEmptySlots(Inventory inventory) {
		ItemStack[] cont = inventory.getContents();
		int i = 0;
		for (ItemStack item : cont)
			if (item != null && item.getType() != Material.AIR) {
				i++;
			}
		return 9 - i;
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_TNTFILL_DESCRIPTION;
	}
}
