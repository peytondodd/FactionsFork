package com.massivecraft.factions.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdHistory extends FCommand {

	public CmdHistory() {
		this.aliases.add("history");

		this.requiredArgs.add("player");

		this.permission = Permission.HISTORY.node;
		this.disableOnLock = true;

		senderMustBePlayer = true;
		senderMustBeMember = true;
		senderMustBeModerator = false;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}

	@Override
	public void perform() {
		Access access = myFaction.getAccess(fme, PermissableAction.HISTORY);
		// This statement allows us to check if they've specifically denied it,
		// or default to
		// the old setting of allowing moderators to set warps.
		if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
			fme.msg(TL.GENERIC_NOPERMISSION, "history");
			return;
		}

		Faction faction = fme.getFaction();

		FPlayer fp = this.argAsBestFPlayerMatch(0);

		if (fp == null) {
			return;
		}

		Faction fac = fp.getFaction();

		if (!fac.equals(faction)) {
			fme.sendMessage(ChatColor.RED + "This player is not in your faction.");
			return;
		}

		List<String> lines = new ArrayList<>();

		lines.add(ChatColor.GRAY + "--------------- " + ChatColor.BLUE + "Data for " + fp.getName() + ChatColor.GRAY + " ---------------");

		lines.add(ChatColor.BLUE + "Previous Factions: ");

		for (String facs : fp.getPreviousFactions()) {
			lines.add(ChatColor.GRAY + "- " + facs);
		}

		lines.add("");

		lines.add(ChatColor.BLUE + "Previous Faction Bans: ");

		for (String bans : fp.getPreviousBans()) {
			lines.add(ChatColor.GRAY + "- " + bans);
		}

		lines.add("");

		lines.add(ChatColor.BLUE + "Previous Faction Mutes: ");

		for (String mutes : fp.getPreviousMutes()) {
			lines.add(ChatColor.GRAY + "- " + mutes);
		}

		for (String s : lines) {
			fme.sendMessage(s);
		}
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_HISTORY_DESCRIPTION;
	}
}
