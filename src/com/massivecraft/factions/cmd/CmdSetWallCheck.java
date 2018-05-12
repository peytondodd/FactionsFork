package com.massivecraft.factions.cmd;

import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdSetWallCheck extends FCommand {

    public CmdSetWallCheck() {
        this.aliases.add("setwallcheck");

        this.requiredArgs.add("seconds");

        this.permission = Permission.SETWALLCHECK.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = true;
        senderMustBeModerator = false;
        senderMustBeCoLeader = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
		Access access = myFaction.getAccess(fme, PermissableAction.SETWALLCHECK);
		// This statement allows us to check if they've specifically denied it,
		// or default to
		// the old setting of allowing moderators to set warps.
		if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
			fme.msg(TL.GENERIC_NOPERMISSION, "set wallcheck time");
			return;
		}
		
        int wallCheck = this.argAsInt(0, 1);
		
		myFaction.setWallCheckTime(wallCheck);
		
		fme.sendMessage(ChatColor.GREEN + "You have set your wall check time to " + wallCheck + " seconds.");
		
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETWALLCHECK_DESCRIPTION;
    }
}
