package com.massivecraft.factions.cmd;

import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdSetAlarmTime extends FCommand {

    public CmdSetAlarmTime() {
        this.aliases.add("setalarm");

        this.requiredArgs.add("seconds");

        this.permission = Permission.SETALARM.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = true;
        senderMustBeModerator = false;
        senderMustBeCoLeader = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
		Access access = myFaction.getAccess(fme, PermissableAction.SETALARM);
		// This statement allows us to check if they've specifically denied it,
		// or default to
		// the old setting of allowing moderators to set warps.
		if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
			fme.msg(TL.GENERIC_NOPERMISSION, "set alarm time");
			return;
		}
		
        int alarmTime = this.argAsInt(0, 1);
		
		myFaction.setAlarmTime(alarmTime);
		
		fme.sendMessage(ChatColor.GREEN + "You have set your faction alarm time to " + alarmTime + " seconds.");
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETALARM_DESCRIPTION;
    }
}
