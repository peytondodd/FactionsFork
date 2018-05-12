package com.massivecraft.factions.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdMuteList extends FCommand {

	public CmdMuteList() {
		super();
		this.aliases.add("mutelist");

		this.permission = Permission.MUTE.node;
		this.disableOnLock = true;

		senderMustBePlayer = true;
		senderMustBeMember = true;
		senderMustBeModerator = false;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}

	@Override
	public void perform() {
        Access access = myFaction.getAccess(fme, PermissableAction.MUTELIST);
        // This statement allows us to check if they've specifically denied it, or default to
        // the old setting of allowing moderators to set warps.
        if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
            fme.msg(TL.GENERIC_NOPERMISSION, "mutelist");
            return;
        }
        
		Faction target = myFaction;

		int mutes = target.totalMutes();

		List<String> lines = new ArrayList<>();
		lines.add(ChatColor.GOLD + "There are " + ChatColor.RED + mutes + ChatColor.GOLD + " mutes for " + target.getTag(myFaction));

		for (FPlayer fp : target.getMuted()) {
			lines.add(ChatColor.GREEN + "- " + fp.getName());
		}

		for (String s : lines) {
			fme.sendMessage(s);
		}
	}

	@Override
	public TL getUsageTranslation() {
		return TL.COMMAND_MUTELIST_DESCRIPTION;
	}
}
