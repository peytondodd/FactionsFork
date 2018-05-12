package com.massivecraft.factions.cmd;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.benzimmer123.fupgrades.UpgradeInventory;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdUpgrades extends FCommand{

    public CmdUpgrades() {
        this.aliases.add("upgrades");
        this.aliases.add("upgrade");

        this.permission = Permission.UPGRADE.node;
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

		if (faction == null || faction.getId().equalsIgnoreCase("-1") || faction.getId().equalsIgnoreCase("-2")
				|| faction.getId().equalsIgnoreCase("0")){
			fme.sendMessage(ChatColor.RED + "You need a faction to use this command!");
			return;
		}
		
		Player p = fme.getPlayer();
		new UpgradeInventory().openUpgrades(p);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_UPGRADE_DESCRIPTION;
    }

}
