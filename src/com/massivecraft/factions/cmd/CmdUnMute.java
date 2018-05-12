package com.massivecraft.factions.cmd;

import org.bukkit.ChatColor;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdUnMute extends FCommand {

	public CmdUnMute() {
		this.aliases.add("unmute");

		this.requiredArgs.add("player");

		this.permission = Permission.UNMUTE.node;
		this.disableOnLock = true;

		senderMustBePlayer = true;
		senderMustBeMember = true;
		senderMustBeModerator = false;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}

	@Override
	public void perform() {
        Access access = myFaction.getAccess(fme, PermissableAction.UNMUTE);
        // This statement allows us to check if they've specifically denied it, or default to
        // the old setting of allowing moderators to set warps.
        if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
            fme.msg(TL.GENERIC_NOPERMISSION, "unmute");
            return;
        }
        
		Faction faction = fme.getFaction();

		FPlayer fp = this.argAsBestFPlayerMatch(0);

		if (fp == null) {
			return;
		}

		Faction fac = fp.getFaction();

		if (!fac.equals(faction)) {
			fme.sendMessage(ChatColor.RED + "This player is not in your faction.");
			return;
		}

		if (fp.getRole().isAtLeast(Role.COLEADER)) {
			fme.sendMessage(ChatColor.RED + "You are not allowed to mute or unmute this player.");
			return;
		}

		if (faction.isMuted(fp)) {
			faction.setMuted(fp, false);

			fp.removeNewMute(faction.getTag());

			if (fp.isOnline())
				fp.sendMessage(ChatColor.GREEN + "You have been unmuted from faction chat.");

			fme.sendMessage(ChatColor.GREEN + "You have unmuted the player " + fp.getName() + ".");
		} else {
			fme.sendMessage(ChatColor.RED + "That player is not currently muted.");
			return;
		}
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_UNMUTE_DESCRIPTION;
	}
}
