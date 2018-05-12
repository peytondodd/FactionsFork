package com.massivecraft.factions.cmd;

import com.benzimmer123.foptions.OptionsManager;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdOptions extends FCommand {

	public CmdOptions() {
		super();
		this.aliases.add("options");
		this.aliases.add("option");

		this.permission = Permission.OPTIONS.node;
		this.disableOnLock = false;

		senderMustBePlayer = true;
		senderMustBeMember = true;
		senderMustBeModerator = true;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}

	@Override
	public void perform() {
		new OptionsManager().openOptions(me);
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_OPTIONS_DESCRIPTION;
	}

}
