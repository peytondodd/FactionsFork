package com.massivecraft.factions.cmd;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdCheckLogs extends FCommand {

	public CmdCheckLogs() {
		this.aliases.add("checklogs");

		this.optionalArgs.put("page", "1");

		this.permission = Permission.CHECKLOGS.node;
		this.senderMustBeMember = true;
		this.senderMustBeModerator = true;
	}

	@Override
	public void perform() {
		Access access = myFaction.getAccess(fme, PermissableAction.CHECKLOGS);

		if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
			fme.msg(TL.GENERIC_NOPERMISSION, "view fac logs");
			return;
		}

		int pagenumber = this.argAsInt(0, 1);

		final int pageheight = 10;

		ArrayList<String> factionList = myFaction.getLogs();

		int pagecount = (factionList.size() / pageheight) + 1;
		if (pagenumber > pagecount)
			pagenumber = pagecount;
		else if (pagenumber < 1)
			pagenumber = 1;
		int start = (pagenumber - 1) * pageheight;
		int end = start + pageheight;
		if (end > factionList.size())
			end = factionList.size();

		fme.sendMessage(ChatColor.GRAY + "---------------- " + ChatColor.BLUE + "Faction Logs " + ChatColor.GRAY + "(" + ChatColor.BLUE + pagenumber
				+ ChatColor.GRAY + "/" + ChatColor.BLUE + pagecount + ChatColor.GRAY + ") -----------------");

		for (String lines : factionList.subList(start, end)) {
			fme.sendMessage(lines.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
		}
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_CHECKLOGS_DESCRIPTION;
	}
}
