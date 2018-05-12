package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.WarmUpUtil;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdFly extends FCommand {

    public CmdFly() {
        super();
        this.aliases.add("fly");

        this.optionalArgs.put("on/off", "flip");

        this.permission = Permission.FLY.node;
        this.senderMustBeMember = true;
        this.senderMustBeModerator = false;
    }

    @Override
    public void perform() {
        Access access = myFaction.getAccess(fme, PermissableAction.FLY);
        // This statement allows us to check if they've specifically denied it, or default to
        // the old setting of allowing moderators to set warps.
        if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
            fme.msg(TL.GENERIC_NOPERMISSION, "fly");
            return;
        }
    	
        if (args.size() == 0) {
            if (!fme.canFlyAtLocation() && !fme.isFlying()) {
                Faction factionAtLocation = Board.getInstance().getFactionAt(fme.getLastStoodAt());
                fme.msg(TL.COMMAND_FLY_NO_ACCESS, factionAtLocation.getTag(fme));
                return;
            }

            toggleFlight(!fme.isFlying());
        } else if (args.size() == 1) {
            if (!fme.canFlyAtLocation() && argAsBool(0)) {
                Faction factionAtLocation = Board.getInstance().getFactionAt(fme.getLastStoodAt());
                fme.msg(TL.COMMAND_FLY_NO_ACCESS, factionAtLocation.getTag(fme));
                return;
            }

            toggleFlight(argAsBool(0));
        }
    }

    private void toggleFlight(final boolean toggle) {
        if (!toggle) {
            fme.setFlying(false);
            return;
        }
        
        this.doWarmUp(WarmUpUtil.Warmup.FLIGHT, TL.WARMUPS_NOTIFY_FLIGHT, "Fly", new Runnable() {
            @Override
            public void run() {
                fme.setFlying(true);
            }
        }, this.p.getConfig().getLong("warmups.f-fly", 0));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FLY_DESCRIPTION;
    }

}
