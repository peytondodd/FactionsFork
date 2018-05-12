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

public class CmdAlarm extends FCommand {

	public CmdAlarm() {
		this.aliases.add("alarm");

		this.permission = Permission.ALARM.node;

		senderMustBePlayer = true;
		senderMustBeMember = true;
		senderMustBeModerator = false;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}
	
	HashMap<Faction, Integer> alarmTimes = AlarmTask.alarmTimes;
	HashMap<Faction, Integer> wallCheckTimes = AlarmTask.wallCheckTimes;

	public void perform() {
        Access access = myFaction.getAccess(fme, PermissableAction.ALARM);
        // This statement allows us to check if they've specifically denied it, or default to
        // the old setting of allowing moderators to set warps.
        if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
            fme.msg(TL.GENERIC_NOPERMISSION, "check");
            return;
        }

		alarmTimes.put(myFaction, 1);
		wallCheckTimes.remove(myFaction);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		myFaction.addLogs(ChatColor.BLUE + me.getName() + ChatColor.GRAY + " executed " + ChatColor.BLUE + "/f alarm " + ChatColor.GRAY + "on "
				+ ChatColor.BLUE + dtf.format(now));
		
		fme.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + fme.getName() + " has sounded your faction alarm!");
	}

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_ALARM_DESCRIPTION;
    }
}
