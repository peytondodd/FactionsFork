package com.benzimmer123.factionchest;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.massivecraft.factions.Faction;

public class ChestHolder implements InventoryHolder{

	Inventory inv;
    Faction fac;

    public ChestHolder(Faction target) {
       this.fac = target;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public Faction getFaction() {
        return fac;
    }
    
}
