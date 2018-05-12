package com.massivecraft.factions.struct;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.fperms.Permissable;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum Role implements Permissable {
    LEADER(4, TL.ROLE_LEADER),
    COLEADER(3, TL.ROLE_COLEADER),
    MODERATOR(2, TL.ROLE_MODERATOR),
    MEMBER(1, TL.ROLE_MEMBER),
    RECRUIT(0, TL.ROLE_RECRUIT);

    public final int value;
    public final String nicename;
    public final TL translation;


    Role(final int value, final TL translation) {
        this.value = value;
        this.nicename = translation.toString();
        this.translation = translation;
    }

    public boolean isAtLeast(Role role) {
        return this.value >= role.value;
    }

    public boolean isAtMost(Role role) {
        return this.value <= role.value;
    }

    public static Role getRelative(Role role, int relative) {
        return Role.getByValue(role.value + relative);
    }

    public static Role getByValue(int value) {
        switch (value) {
            case 0:
                return RECRUIT;
            case 1:
                return MEMBER;
            case 2:
                return MODERATOR;
            case 3:
                return COLEADER;
            case 4:
                return LEADER;
        }

        return null;
    }

    public static Role fromString(String check) {
        switch (check.toLowerCase()) {
            case "leader":
                return LEADER;
            case "coleader":
                return COLEADER;
            case "mod":
            case "moderator":
                return MODERATOR;
            case "normal":
            case "member":
                return MEMBER;
            case "recruit":
            case "rec":
                return RECRUIT;
        }

        return null;
    }

    @Override
    public String toString() {
        return this.nicename;
    }

    public TL getTranslation() {
        return translation;
    }

    public String getPrefix() {
        if (this == Role.LEADER) {
            return Conf.prefixLeader;
        }
        
        if (this == Role.COLEADER) {
            return Conf.prefixCoLeader;
        }

        if (this == Role.MODERATOR) {
            return Conf.prefixMod;
        }

        if (this == Role.MEMBER) {
            return Conf.prefixNormal;
        }

        if (this == Role.RECRUIT) {
            return Conf.prefixRecruit;
        }

        return "";
    }

    // Utility method to build items for F Perm GUI
    @Override
    public ItemStack buildItem() {
        final ConfigurationSection RELATION_CONFIG = P.p.getConfig().getConfigurationSection("fperm-gui.relation");

        String displayName = replacePlaceholders(RELATION_CONFIG.getString("placeholder-item.name", ""));
        List<String> lore = new ArrayList<>();

        Material material = Material.matchMaterial(RELATION_CONFIG.getString("materials." + name().toLowerCase(), "STAINED_CLAY"));
        if (material == null) {
            return null;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        for (String loreLine : RELATION_CONFIG.getStringList("placeholder-item.lore")) {
            lore.add(replacePlaceholders(loreLine));
        }

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public String replacePlaceholders(String string) {
        string = ChatColor.translateAlternateColorCodes('&', string);

        String permissableName = nicename.substring(0, 1).toUpperCase() + nicename.substring(1);

        string = string.replace("{relation-color}", ChatColor.GREEN.toString());
        string = string.replace("{relation}", permissableName);

        return string;
    }

}
