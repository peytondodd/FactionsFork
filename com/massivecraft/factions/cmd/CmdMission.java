package com.massivecraft.factions.cmd;

import com.benzimmer123.missions.MissionManager;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdMission extends FCommand {

    public CmdMission() {
        this.aliases.add("missions");
        this.aliases.add("mission");

        this.errorOnToManyArgs = true;

        this.permission = Permission.MISSIONS.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = false;
		senderMustBeCoLeader = false;
        senderMustBeLeader = false;
    }

    @Override
    public void perform() {
        MissionManager.get().openInventory(fme.getPlayer());
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_MISSION_DESCRIPTION;
    }
}
