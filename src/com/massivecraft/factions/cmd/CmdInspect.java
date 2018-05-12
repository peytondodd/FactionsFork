package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdInspect extends FCommand {

	public CmdInspect() {
		this.aliases.add("inspect");

		this.permission = Permission.INSPECT.node;

		senderMustBePlayer = true;
		senderMustBeMember = true;
		senderMustBeModerator = false;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;
	}

	public void perform() {
        Access access = myFaction.getAccess(fme, PermissableAction.INSPECT);
        // This statement allows us to check if they've specifically denied it, or default to
        // the old setting of allowing moderators to set warps.
        if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
            fme.msg(TL.GENERIC_NOPERMISSION, "inspect");
            return;
        }

		fme.setInspecting(!fme.isInspecting());
		fme.msg(fme.isInspecting() ?  TL.COMMAND_INSPECT_ENABLE :  TL.COMMAND_INSPECT_DISABLE);
	}

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_INSPECT_DESCRIPTION;
    }
}
