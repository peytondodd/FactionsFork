package com.massivecraft.factions.cmd;

import org.bukkit.ChatColor;

import com.benzimmer123.factionchest.ChestManager;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdChest extends FCommand {

    public CmdChest() {
        this.aliases.add("chest");

		this.optionalArgs.put("chest", "1");

        this.permission = Permission.CHEST.node;
        this.senderMustBeMember = true;
        this.senderMustBeModerator = false;
    }

    @Override
    public void perform() {
        Access access = myFaction.getAccess(fme, PermissableAction.CHEST);
        // This statement allows us to check if they've specifically denied it, or default to
        // the old setting of allowing moderators to set warps.
        if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
            fme.msg(TL.GENERIC_NOPERMISSION, "access chest");
            return;
        }
        
		int chestNumber = this.argAsInt(0, 1);
		
		if(chestNumber != 2 && chestNumber != 1){
			fme.msg(ChatColor.RED + "You can only access faction chest 1 or 2.");
			return;
		}
		
		if(chestNumber == 2){
			if(!myFaction.hasSecondChest()){
				fme.msg(ChatColor.RED + "You do not have access to a second chest.");
				return;
			}
		}
        
        new ChestManager(p).openChest(fme.getPlayer(), myFaction, chestNumber);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CHEST_DESCRIPTION;
    }
}
