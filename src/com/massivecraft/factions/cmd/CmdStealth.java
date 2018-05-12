package com.massivecraft.factions.cmd;

import org.bukkit.ChatColor;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdStealth extends FCommand {

	public CmdStealth() {
		this.aliases.add("stealth");

		this.permission = Permission.STEALTH.node;
		this.disableOnLock = true;

		senderMustBePlayer = true;
		senderMustBeMember = false;
		senderMustBeModerator = true;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}

	@Override
	public void perform() {
		Faction faction = fme.getFaction();

		if (faction == null || faction.getId().equalsIgnoreCase("0") || faction.getId().equalsIgnoreCase("none")
				|| faction.getId().equalsIgnoreCase("safezone") || faction.getId().equalsIgnoreCase("warzone")) {
			fme.sendMessage(ChatColor.RED + "You need a faction to use this command!");
			return;
		}

		fme.setStealth(!fme.isStealth());
		fme.msg(fme.isStealth() ? TL.COMMAND_STEALTH_ENABLE : TL.COMMAND_STEALTH_DISABLE);
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_STEALTH_DESCRIPTION;
	}
}
