package com.massivecraft.factions.cmd;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdTNTBankDeposit extends FCommand {

	public CmdTNTBankDeposit() {
		super();
		this.aliases.add("tntdeposit");

		this.requiredArgs.add("amount");

		this.permission = Permission.DEPOSIT_TNT.node;
		this.disableOnLock = true;

		senderMustBePlayer = true;
		senderMustBeMember = false;
		senderMustBeModerator = true;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}

	@Override
	public void perform() {
		Access access = myFaction.getAccess(fme, PermissableAction.DEPOSITTNT);
		// This statement allows us to check if they've specifically denied it,
		// or default to
		// the old setting of allowing moderators to set warps.
		if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MEMBER))) {
			fme.msg(TL.GENERIC_NOPERMISSION, "deposit tnt");
			return;
		}

		@SuppressWarnings("unused")
		int testNumber = -1;

		try {
			testNumber = Integer.parseInt(args.get(0));
		} catch (NumberFormatException e) {
			fme.msg(TL.COMMAND_TNT_INVALID_NUM);
			return;
		}

		int amount = Integer.parseInt(args.get(0));

		if (amount < 0) {
			fme.msg(TL.COMMAND_TNT_POSITIVE);
			return;
		}

		Inventory inv = me.getInventory();

		int invTnt = 0;

		for (int i = 0; i <= inv.getSize(); i++) {
			if (inv.getItem(i) == null) {
				continue;
			}
			if (inv.getItem(i).getType() == Material.TNT) {
				invTnt += inv.getItem(i).getAmount();
			}
		}

		if (amount > invTnt) {
			fme.msg(TL.COMMAND_TNT_DEPOSIT_NOTENOUGH);
			return;
		}

		ItemStack tnt = new ItemStack(Material.TNT, amount);

		if (!myFaction.hasTNTBankUpgrade()) {
			if (fme.getFaction().getTnt() + amount > 17280) {
				msg(TL.COMMAND_TNT_EXCEEDLIMIT);
				return;
			}
		} else {
			if (fme.getFaction().getTnt() + amount > 34560) {
				msg(TL.COMMAND_TNT_EXCEEDLIMIT);
				return;
			}
		}

		removeFromInventory(me.getInventory(), tnt);
		me.updateInventory();

		int maxtnt = 17520;
		
		if(myFaction.hasTNTBankUpgrade()){
			maxtnt = maxtnt * 2;
		}
		
		fme.getFaction().addTnt(amount);
		fme.sendMessage(ChatColor.GREEN + "Your TNT bank now contains x" + myFaction.getTnt() + "/" + maxtnt + " TNT.");
		return;

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

	public int getEmptySlots(Player p) {
		PlayerInventory inventory = p.getInventory();
		ItemStack[] cont = inventory.getContents();
		int i = 0;
		for (ItemStack item : cont)
			if (item != null && item.getType() != Material.AIR) {
				i++;
			}
		return 36 - i;
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_TNT_DESCRIPTION;
	}
}