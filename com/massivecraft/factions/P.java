package com.massivecraft.factions;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.benzimmer123.factionchest.ChestListener;
import com.benzimmer123.falarms.AlarmTask;
import com.benzimmer123.foptions.OptionsListener;
import com.benzimmer123.fupgrades.CropGrowth;
import com.benzimmer123.fupgrades.DamageEffects;
import com.benzimmer123.fupgrades.ExpDrops;
import com.benzimmer123.fupgrades.SpawnerRate;
import com.benzimmer123.fupgrades.UpgradeListener;
import com.benzimmer123.inspect.Inspect;
import com.benzimmer123.missions.BlazeKilling;
import com.benzimmer123.missions.GlassBreak;
import com.benzimmer123.missions.Miner;
import com.benzimmer123.missions.MissionClickListener;
import com.benzimmer123.missions.MissionManager;
import com.benzimmer123.missions.SugarCane;
import com.massivecraft.factions.cmd.CmdAutoHelp;
import com.massivecraft.factions.cmd.FCmdRoot;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.integration.Worldguard;
import com.massivecraft.factions.integration.dynmap.EngineDynmap;
import com.massivecraft.factions.listeners.FactionsBlockListener;
import com.massivecraft.factions.listeners.FactionsChatListener;
import com.massivecraft.factions.listeners.FactionsEntityListener;
import com.massivecraft.factions.listeners.FactionsExploitListener;
import com.massivecraft.factions.listeners.FactionsPlayerListener;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.util.AutoLeaveTask;
import com.massivecraft.factions.util.ClipPlaceholderAPIManager;
import com.massivecraft.factions.util.EnumTypeAdapter;
import com.massivecraft.factions.util.FlightDisableUtil;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.util.MapFLocToStringSetTypeAdapter;
import com.massivecraft.factions.util.MyLocationTypeAdapter;
import com.massivecraft.factions.util.PermissionsMapTypeAdapter;
import com.massivecraft.factions.zcore.MPlugin;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.Permissable;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TextUtil;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

public class P extends MPlugin {

	// Our single plugin instance.
	// Single 4 life.
	public static P p;
	public static Permission perms = null;

	// Persistence related
	private boolean locked = false;

	public boolean getLocked() {
		return this.locked;
	}

	public void setLocked(boolean val) {
		this.locked = val;
		this.setAutoSave(val);
	}

	private Integer AutoLeaveTask = null;
	
	// Commands
	public FCmdRoot cmdBase;
	public CmdAutoHelp cmdAutoHelp;

	private boolean hookedPlayervaults;
	private ClipPlaceholderAPIManager clipPlaceholderAPIManager;
	private boolean mvdwPlaceholderAPIManager = false;

	public P() {
		p = this;
	}

	@Override
	public void onEnable() {
		if (!preEnable()) {
			return;
		}

		this.loadSuccessful = false;
		saveDefaultConfig();

		// Load Conf from disk
		Conf.load();
		Essentials.setup();
		hookedPlayervaults = setupPlayervaults();
		FPlayers.getInstance().load();
		Factions.getInstance().load();

		for (FPlayer fPlayer : FPlayers.getInstance().getAllFPlayers()) {
			Faction faction = Factions.getInstance().getFactionById(fPlayer.getFactionId());
			if (faction == null) {
				log("Invalid faction id on " + fPlayer.getName() + ":" + fPlayer.getFactionId());
				fPlayer.resetFactionData(false);
				continue;
			}
			faction.addFPlayer(fPlayer);
		}

		Board.getInstance().load();
		Board.getInstance().clean();

		// Add Base Commands
		this.cmdBase = new FCmdRoot();
		this.cmdAutoHelp = new CmdAutoHelp();
		this.getBaseCommands().add(cmdBase);

		Econ.setup();
		setupPermissions();

		if (Conf.worldGuardChecking || Conf.worldGuardBuildPriority) {
			Worldguard.init(this);
		}

		EngineDynmap.getInstance().init();

		// start up task which runs the autoLeaveAfterDaysOfInactivity routine
		startAutoLeaveTask(false);

		// Register Event Handlers
		getServer().getPluginManager().registerEvents(new FactionsPlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new FactionsChatListener(this), this);
		getServer().getPluginManager().registerEvents(new FactionsEntityListener(this), this);
		getServer().getPluginManager().registerEvents(new FactionsExploitListener(), this);
		getServer().getPluginManager().registerEvents(new FactionsBlockListener(this), this);
		getServer().getPluginManager().registerEvents(new Inspect(), this);
		getServer().getPluginManager().registerEvents(new CropGrowth(), this);
		getServer().getPluginManager().registerEvents(new DamageEffects(), this);
		getServer().getPluginManager().registerEvents(new ExpDrops(), this);
		getServer().getPluginManager().registerEvents(new SpawnerRate(), this);
		getServer().getPluginManager().registerEvents(new UpgradeListener(), this);
		getServer().getPluginManager().registerEvents(new OptionsListener(this), this);
		getServer().getPluginManager().registerEvents(new ChestListener(this), this);
		getServer().getPluginManager().registerEvents(new MissionClickListener(), this);
		getServer().getPluginManager().registerEvents(new BlazeKilling(), this);
		getServer().getPluginManager().registerEvents(new GlassBreak(), this);
		getServer().getPluginManager().registerEvents(new SugarCane(), this);
		getServer().getPluginManager().registerEvents(new Miner(), this);
		getServer().getPluginManager().registerEvents(new Safety(this), this);

		this.getCommand(this.refCommand).setExecutor(this);

		int delay = getConfig().getInt("f-fly.radius-check", 1) * 20;
		new FlightDisableUtil().runTaskTimer(this, 0, delay);
		new AlarmTask(this).call();
		log(Level.INFO, "Enabling enemy radius check for f fly every 5 seconds", delay);
		MissionManager.get().load();
		new Safety(this).callTask();

		for (Player online : Bukkit.getOnlinePlayers()) {
			FPlayer player = FPlayers.getInstance().getByPlayer(online);

			if (player.isFlying()) {
				new FlightDisableUtil().getFlying().add(player);
			}
		}

		setupPlaceholderAPI();
		postEnable();
		this.loadSuccessful = true;
	}

	private void setupPlaceholderAPI() {
		Plugin clip = getServer().getPluginManager().getPlugin("PlaceholderAPI");
		if (clip != null && clip.isEnabled()) {
			this.clipPlaceholderAPIManager = new ClipPlaceholderAPIManager();
			log(Level.INFO, "Found Clip's PlaceholderAPI. Adding hooks.");
		}

		Plugin mvdw = getServer().getPluginManager().getPlugin("MVdWPlaceholderAPI");
		if (mvdw != null && mvdw.isEnabled()) {
			this.mvdwPlaceholderAPIManager = true;
			log(Level.INFO, "Found MVdWPlaceholderAPI. Adding hooks.");
		}
	}

	public boolean isClipPlaceholderAPIHooked() {
		return this.clipPlaceholderAPIManager != null;
	}

	public boolean isMVdWPlaceholderAPIHooked() {
		return this.mvdwPlaceholderAPIManager;
	}

	private boolean setupPermissions() {
		try {
			RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
			if (rsp != null) {
				perms = rsp.getProvider();
			}
		} catch (NoClassDefFoundError ex) {
			return false;
		}
		return perms != null;
	}

	private boolean setupPlayervaults() {
		Plugin plugin = getServer().getPluginManager().getPlugin("PlayerVaults");
		return plugin != null && plugin.isEnabled();
	}

	@Override
	public GsonBuilder getGsonBuilder() {
		Type mapFLocToStringSetType = new TypeToken<Map<FLocation, Set<String>>>() {
		}.getType();

		Type accessTypeAdatper = new TypeToken<Map<Permissable, Map<PermissableAction, Access>>>() {
		}.getType();

		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
				.registerTypeAdapter(accessTypeAdatper, new PermissionsMapTypeAdapter())
				.registerTypeAdapter(LazyLocation.class, new MyLocationTypeAdapter())
				.registerTypeAdapter(mapFLocToStringSetType, new MapFLocToStringSetTypeAdapter())
				.registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY);
	}

	@Override
	public void onDisable() {
		// only save data if plugin actually completely loaded successfully
		if (this.loadSuccessful) {
			Conf.save();
		}
		if (AutoLeaveTask != null) {
			this.getServer().getScheduler().cancelTask(AutoLeaveTask);
			AutoLeaveTask = null;
		}

		super.onDisable();
	}

	public void startAutoLeaveTask(boolean restartIfRunning) {
		if (AutoLeaveTask != null) {
			if (!restartIfRunning) {
				return;
			}
			this.getServer().getScheduler().cancelTask(AutoLeaveTask);
		}

		if (Conf.autoLeaveRoutineRunsEveryXMinutes > 0.0) {
			long ticks = (long) (20 * 60 * Conf.autoLeaveRoutineRunsEveryXMinutes);
			AutoLeaveTask = getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutoLeaveTask(), ticks, ticks);
		}
	}

	@Override
	public void postAutoSave() {
		// Board.getInstance().forceSave(); Not sure why this was there as it's
		// called after the board is already saved.
		Conf.save();
	}

	@Override
	public boolean logPlayerCommands() {
		return Conf.logPlayerCommands;
	}

	@Override
	public boolean handleCommand(CommandSender sender, String commandString, boolean testOnly) {
		return sender instanceof Player && FactionsPlayerListener.preventCommand(commandString, (Player) sender)
				|| super.handleCommand(sender, commandString, testOnly);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		if (split.length == 0) {
			return handleCommand(sender, "/f help", false);
		}

		// otherwise, needs to be handled; presumably another plugin directly
		// ran the command
		String cmd = Conf.baseCommandAliases.isEmpty() ? "/f" : "/" + Conf.baseCommandAliases.get(0);
		return handleCommand(sender, cmd + " " + TextUtil.implode(Arrays.asList(split), " "), false);
	}

	// -------------------------------------------- //
	// Functions for other plugins to hook into
	// -------------------------------------------- //

	// This value will be updated whenever new hooks are added
	public int hookSupportVersion() {
		return 3;
	}

	// If another plugin is handling insertion of chat tags, this should be used
	// to notify Factions
	public void handleFactionTagExternally(boolean notByFactions) {
		Conf.chatTagHandledByAnotherPlugin = notByFactions;
	}

	// Simply put, should this chat event be left for Factions to handle? For
	// now, that means players with Faction Chat
	// enabled or use of the Factions f command without a slash; combination of
	// isPlayerFactionChatting() and isFactionsCommand()

	public boolean shouldLetFactionsHandleThisChat(AsyncPlayerChatEvent event) {
		return event != null && (isPlayerFactionChatting(event.getPlayer()) || isFactionsCommand(event.getMessage()));
	}

	// Does player have Faction Chat enabled? If so, chat plugins should
	// preferably not do channels,
	// local chat, or anything else which targets individual recipients, so
	// Faction Chat can be done
	public boolean isPlayerFactionChatting(Player player) {
		if (player == null) {
			return false;
		}
		FPlayer me = FPlayers.getInstance().getByPlayer(player);

		return me != null && me.getChatMode().isAtLeast(ChatMode.ALLIANCE);
	}

	// Is this chat message actually a Factions command, and thus should be left
	// alone by other plugins?

	// TODO: GET THIS BACK AND WORKING

	public boolean isFactionsCommand(String check) {
		return !(check == null || check.isEmpty()) && this.handleCommand(null, check, true);
	}

	// Get a player's faction tag (faction name), mainly for usage by chat
	// plugins for local/channel chat
	public String getPlayerFactionTag(Player player) {
		return getPlayerFactionTagRelation(player, null);
	}

	// Same as above, but with relation (enemy/neutral/ally) coloring
	// potentially added to the tag
	public String getPlayerFactionTagRelation(Player speaker, Player listener) {
		String tag = "~";

		if (speaker == null) {
			return tag;
		}

		FPlayer me = FPlayers.getInstance().getByPlayer(speaker);
		if (me == null) {
			return tag;
		}

		// if listener isn't set, or config option is disabled, give back
		// uncolored tag
		if (listener == null || !Conf.chatTagRelationColored) {
			tag = me.getChatTag().trim();
		} else {
			FPlayer you = FPlayers.getInstance().getByPlayer(listener);
			if (you == null) {
				tag = me.getChatTag().trim();
			} else // everything checks out, give the colored tag
			{
				tag = me.getChatTag(you).trim();
			}
		}
		if (tag.isEmpty()) {
			tag = "~";
		}

		return tag;
	}

	// Get a player's title within their faction, mainly for usage by chat
	// plugins for local/channel chat
	public String getPlayerTitle(Player player) {
		if (player == null) {
			return "";
		}

		FPlayer me = FPlayers.getInstance().getByPlayer(player);
		if (me == null) {
			return "";
		}

		return me.getTitle().trim();
	}

	// Get a list of all faction tags (names)
	public Set<String> getFactionTags() {
		return Factions.getInstance().getFactionTags();
	}

	// Get a list of all players in the specified faction
	public Set<String> getPlayersInFaction(String factionTag) {
		Set<String> players = new HashSet<>();
		Faction faction = Factions.getInstance().getByTag(factionTag);
		if (faction != null) {
			for (FPlayer fplayer : faction.getFPlayers()) {
				players.add(fplayer.getName());
			}
		}
		return players;
	}

	// Get a list of all online players in the specified faction
	public Set<String> getOnlinePlayersInFaction(String factionTag) {
		Set<String> players = new HashSet<>();
		Faction faction = Factions.getInstance().getByTag(factionTag);
		if (faction != null) {
			for (FPlayer fplayer : faction.getFPlayersWhereOnline(true)) {
				players.add(fplayer.getName());
			}
		}
		return players;
	}

	public boolean isHookedPlayervaults() {
		return hookedPlayervaults;
	}

	public String getPrimaryGroup(OfflinePlayer player) {
		return perms == null || !perms.hasGroupSupport() ? " " : perms.getPrimaryGroup(Bukkit.getWorlds().get(0).toString(), player);
	}

	public void debug(Level level, String s) {
		if (getConfig().getBoolean("debug", false)) {
			getLogger().log(level, s);
		}
	}

	public void debug(String s) {
		debug(Level.INFO, s);
	}
}