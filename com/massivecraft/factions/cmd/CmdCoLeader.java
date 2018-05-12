package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.TL;

public class CmdCoLeader extends FCommand{

	public CmdCoLeader() {
		super();
		this.aliases.add("coleader");

		this.optionalArgs.put("player name", "name");

		this.permission = Permission.COLEADER.node;
		this.disableOnLock = true;

		senderMustBePlayer = false;
		senderMustBeMember = true;
		senderMustBeModerator = false;
		senderMustBeCoLeader = false;
		senderMustBeLeader = true;
	}

	@Override
	public void perform() {
		FPlayer you = this.argAsBestFPlayerMatch(0);

		if (you == null) {
			return;
		}

		boolean permAny = Permission.MOD_ANY.has(sender, false);
		Faction targetFaction = you.getFaction();

		if (targetFaction != myFaction && !permAny) {
			msg(TL.COMMAND_COLEADER_NOTMEMBER, you.describeTo(fme, true));
			return;
		}

		if (fme != null && fme.getRole() != Role.LEADER && !permAny) {
			msg(TL.COMMAND_COLEADER_NOTADMIN);
			return;
		}

		if (you == fme && !permAny) {
			msg(TL.COMMAND_COLEADER_SELF);
			return;
		}

		if (you.getRole() == Role.LEADER || you.getRole() == Role.COLEADER) {
			msg(TL.COMMAND_COLEADER_TARGETISADMIN);
			return;
		}

		if (you.getRole() == Role.COLEADER) {
			// Revoke
			you.setRole(Role.MODERATOR);
			targetFaction.msg(TL.COMMAND_COLEADER_REVOKED, you.describeTo(targetFaction, true));
			msg(TL.COMMAND_COLEADER_REVOKES, you.describeTo(fme, true));
		} else {
			// Give
			you.setRole(Role.COLEADER);
			targetFaction.msg(TL.COMMAND_COLEADER_PROMOTED, you.describeTo(targetFaction, true));
			msg(TL.COMMAND_COLEADER_PROMOTES, you.describeTo(fme, true));
		}
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_COLEADER_DESCRIPTION;
	}
}
