package com.massivecraft.factions.cmd;

import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdValue extends FCommand {

	public CmdValue() {
		this.aliases.add("value");

		this.requiredArgs.add("faction");
		// this.optionalArgs.put("", "");

		this.permission = Permission.VALUE.node;
		this.disableOnLock = false;

		senderMustBePlayer = true;
		senderMustBeMember = false;
		senderMustBeModerator = false;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}

	@Override
	public void perform() {
			fme.sendMessage(ChatColor.RED + "The faction top plugin has not loaded correctly.");
			return;
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_VALUE_DESCRIPTION;
	}

}
