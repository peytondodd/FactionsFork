package com.massivecraft.factions.cmd;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.bukkit.ChatColor;

import com.benzimmer123.falarms.AlarmTask;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdCheck extends FCommand {

	public CmdCheck() {
		this.aliases.add("check");

		this.permission = Permission.CHECK.node;

		senderMustBePlayer = true;
		senderMustBeMember = true;
		senderMustBeModerator = false;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}

	HashMap<Faction, Integer> alarmTimes = AlarmTask.alarmTimes;
	HashMap<Faction, Integer> wallCheckTimes = AlarmTask.wallCheckTimes;

	public void perform() {
		Access access = myFaction.getAccess(fme, PermissableAction.CHECK);

		if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
			fme.msg(TL.GENERIC_NOPERMISSION, "check");
			return;
		}

		alarmTimes.remove(myFaction);
		wallCheckTimes.remove(myFaction);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		myFaction.addLogs(ChatColor.BLUE + me.getName() + ChatColor.GRAY + " executed " + ChatColor.BLUE + "/f check " + ChatColor.GRAY + "on "
				+ ChatColor.BLUE + dtf.format(now));

		fme.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + fme.getName() + " has successfully checked your faction walls.");
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_CHECK_DESCRIPTION;
	}

}
