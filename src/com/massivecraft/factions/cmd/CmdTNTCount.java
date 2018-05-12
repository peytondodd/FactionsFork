package com.massivecraft.factions.cmd;

import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdTNTCount extends FCommand {

	public CmdTNTCount() {
		super();
		this.aliases.add("tntcount");

		this.permission = Permission.COUNT_TNT.node;
		this.disableOnLock = true;

		senderMustBePlayer = true;
		senderMustBeMember = false;
		senderMustBeModerator = true;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}

	@Override
	public void perform() {
		Access access = myFaction.getAccess(fme, PermissableAction.COUNTTNT);
		// This statement allows us to check if they've specifically denied it,
		// or default to
		// the old setting of allowing moderators to set warps.
		if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MEMBER))) {
			fme.msg(TL.GENERIC_NOPERMISSION, "count tnt");
			return;
		}

		int maxtnt = 17520;
		
		if(myFaction.hasTNTBankUpgrade()){
			maxtnt = maxtnt * 2;
		}
		
		fme.sendMessage(ChatColor.GREEN + "Your TNT bank currently contains x" + myFaction.getTnt() + "/" + maxtnt + " TNT.");
	}
	
	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_TNTCOUNT_DESCRIPTION;
	}
}
