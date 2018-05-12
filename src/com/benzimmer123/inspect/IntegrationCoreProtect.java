package com.benzimmer123.inspect;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class IntegrationCoreProtect {

	private static IntegrationCoreProtect i = new IntegrationCoreProtect();

	public static IntegrationCoreProtect get() {
		return i;
	}

	public static CoreProtectAPI getCoreProtect() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");

		// Check that CoreProtect is loaded
		if (plugin == null || !(plugin instanceof CoreProtect)) {
			return null;
		}

		// Check that the API is enabled
		CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
		if (CoreProtect.isEnabled() == false) {
			return null;
		}

		return CoreProtect;
	}
}
