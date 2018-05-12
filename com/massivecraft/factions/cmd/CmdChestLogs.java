package com.massivecraft.factions.cmd;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.benzimmer123.factionchest.ChestManager;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdChestLogs extends FCommand {

	public CmdChestLogs() {
		this.aliases.add("chestlogs");

		this.requiredArgs.add("chest");
		this.optionalArgs.put("page", "1");

		this.permission = Permission.CHESTLOGS.node;
		this.senderMustBeMember = true;
		this.senderMustBeModerator = true;
	}

	@Override
	public void perform() {
		Access access = myFaction.getAccess(fme, PermissableAction.CHESTLOGS);
		// This statement allows us to check if they've specifically denied it,
		// or default to
		// the old setting of allowing moderators to set warps.
		if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
			fme.msg(TL.GENERIC_NOPERMISSION, "view chest logs");
			return;
		}

		int chestNumber = this.argAsInt(0, 1);

		if (chestNumber != 2 && chestNumber != 1) {
			fme.msg(ChatColor.RED + "You can only view faction chest logs for chest 1 or 2.");
			return;
		}

		int pagenumber = this.argAsInt(1, 1);

		final int pageheight = 10;

		ArrayList<String> factionList = new ChestManager(p).readFileAsList(myFaction, chestNumber);

		int pagecount = (factionList.size() / pageheight) + 1;
		if (pagenumber > pagecount)
			pagenumber = pagecount;
		else if (pagenumber < 1)
			pagenumber = 1;
		int start = (pagenumber - 1) * pageheight;
		int end = start + pageheight;
		if (end > factionList.size())
			end = factionList.size();

		fme.sendMessage(ChatColor.GRAY + "------------ " + ChatColor.BLUE + "Faction ChestLogs " + ChatColor.GRAY + "(" + ChatColor.BLUE + pagenumber
				+ ChatColor.GRAY + "/" + ChatColor.BLUE + pagecount + ChatColor.GRAY + ") Chest: " + ChatColor.BLUE + chestNumber + ChatColor.GRAY
				+ " ------------");

		for (String lines : factionList.subList(start, end)) {
			fme.sendMessage(lines.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
		}
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_CHESTLOGS_DESCRIPTION;
	}
}
