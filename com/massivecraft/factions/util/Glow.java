package com.massivecraft.factions.util;

import java.lang.reflect.Field;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class Glow extends EnchantmentWrapper {
	private static Enchantment glow;

	private Glow() {
		super(255);
	}

	@SuppressWarnings("deprecation")
	public static Enchantment getGlow() {
		if (glow != null) {
			return glow;
		}
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		glow = new Glow();
		if (Enchantment.getById(255) == null) {
			Enchantment.registerEnchantment(glow);
		}
		return glow;
	}

	public int getMaxLevel() {
		return 10;
	}

	public int getStartLevel() {
		return 1;
	}

	public EnchantmentTarget getItemTarget() {
		return null;
	}

	public boolean canEnchantItem(ItemStack item) {
		return true;
	}

	public String getName() {
		return "Glow";
	}

	public boolean conflictsWith(Enchantment other) {
		return false;
	}
}
