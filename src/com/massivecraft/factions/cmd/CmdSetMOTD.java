package com.massivecraft.factions.cmd;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdSetMOTD extends FCommand {

    public CmdSetMOTD() {
        this.aliases.add("setmotd");

        this.requiredArgs.add("message");

        this.permission = Permission.SETMOTD.node;
        this.disableOnLock = true;

        this.errorOnToManyArgs = false;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeCoLeader = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
		Access access = myFaction.getAccess(fme, PermissableAction.SETMOTD);
		// This statement allows us to check if they've specifically denied it,
		// or default to
		// the old setting of allowing moderators to set warps.
		if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
			fme.msg(TL.GENERIC_NOPERMISSION, "set motd");
			return;
		}
		
        String message = StringUtils.join(args, " ");
		
		myFaction.setMOTDMessage(message);

		fme.sendMessage(ChatColor.GREEN + "You have set the faction MOTD to:");
		fme.sendMessage(message.replaceAll("(&([a-f0-9]))", "\u00A7$2").replaceAll("&l", "\u00A7l").replaceAll("&o", "\u00A7o")
				.replaceAll("&k", "\u00A7k").replaceAll("&r", "\u00A7r").replaceAll("&n", "\u00A7n").replaceAll("&m", "\u00A7m"));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETMOTD_DESCRIPTION;
    }
}
