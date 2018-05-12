package com.massivecraft.factions.zcore.persist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.benzimmer123.missions.EndReason;
import com.benzimmer123.missions.Mission;
import com.benzimmer123.missions.MissionData;
import com.benzimmer123.missions.MissionManager;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.P;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.LandClaimEvent;
import com.massivecraft.factions.fancymessage.FancyMessage;
import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.integration.Worldguard;
import com.massivecraft.factions.scoreboards.FScoreboard;
import com.massivecraft.factions.scoreboards.sidebar.FInfoSidebar;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.FlightDisableUtil;
import com.massivecraft.factions.util.RelationUtil;
import com.massivecraft.factions.util.WarmUpUtil;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

/**
 * Logged in players always have exactly one FPlayer instance. Logged out
 * players may or may not have an FPlayer instance. They will always have one if
 * they are part of a faction. This is because only players with a faction are
 * saved to disk (in order to not waste disk space).
 * <p/>
 * The FPlayer is linked to a minecraft player using the player name.
 * <p/>
 * The same instance is always returned for the same player. This means you can
 * use the == operator. No .equals method necessary.
 */

public abstract class MemoryFPlayer implements FPlayer {
	protected String factionId;
	protected Role role;
	protected String title;
	protected double power;
	protected double powerBoost;
	protected long lastPowerUpdateTime;
	protected long lastLoginTime;
	protected ChatMode chatMode;
	protected boolean ignoreAllianceChat = false;
	protected String id;
	protected String name;
	protected boolean monitorJoins;
	protected boolean spyingChat = false;
	protected boolean showScoreboard = true;
	protected WarmUpUtil.Warmup warmup;
	protected int warmupTask;
	protected boolean isAdminBypassing = false;
	protected int kills, deaths;
	protected boolean willAutoLeave = true;
	protected int mapHeight = 8; // default to old value
	protected boolean isFlying = false;
	protected boolean isStealth = false;
	protected boolean isInspecting = false;
	protected long lastInspectionTime = System.currentTimeMillis() - 3000L;
	protected long commandCooldown = System.currentTimeMillis();
	private MissionData missionData;
	private HashSet<String> previousFactions = new HashSet<>();
	private HashSet<String> previousBans = new HashSet<>();
	private HashSet<String> previousMutes = new HashSet<>();
	private HashMap<String, Long> missionEndTime = new HashMap<>();
	private HashMap<String, Integer> finishedMission = new HashMap<>();
	private HashMap<String, Integer> missionLevel = new HashMap<>();
	protected transient FLocation lastStoodAt = new FLocation();
	protected transient boolean mapAutoUpdating;
	protected transient Faction autoClaimFor;
	protected transient boolean autoSafeZoneEnabled;
	protected transient boolean autoWarZoneEnabled;
	protected transient boolean loginPvpDisabled;
	protected transient long lastFrostwalkerMessage;
	protected transient boolean shouldTakeFallDamage = true;

	public HashSet<String> getPreviousFactions() {
		return this.previousFactions;
	}
	
	public HashSet<String> getPreviousMutes() {
		return this.previousMutes;
	}
	
	public HashSet<String> getPreviousBans() {
		return this.previousBans;
	}
	
	public void addNewBan(String fac){
		previousBans.add(fac);
	}
	
	public void addNewMute(String fac){
		previousMutes.add(fac);
	}
	
	public void addNewFaction(String fac){
		previousFactions.add(fac);
	}
	
	public void removeNewBan(String fac){
		previousBans.remove(fac);
	}
	
	public void removeNewMute(String fac){
		previousMutes.remove(fac);
	}
	
	public void setMissionData(MissionData missionData) {
		this.missionData = missionData;
	}

	public MissionData getMissionData() {
		return this.missionData;
	}

	public Mission getActiveMission() {
		if (this.missionData == null)
			return null;

		return MissionManager.get().getMissionByName(getMissionData().getActiveMission());
	}

	public void upgradeLevel(String missionName) {
		if (!missionLevel.containsKey(missionName))
			return;

		missionLevel.put(missionName, missionLevel.get(missionName) + 1);
	}

	public int getLevel(Mission mission) {
		if (this.missionLevel == null)
			setMissionLevels(new HashMap<String, Integer>());

		if (missionLevel.containsKey(mission.getMissionName()))
			return missionLevel.get(mission.getMissionName());

		missionLevel.put(mission.getMissionName(), 1);

		return missionLevel.get(mission.getMissionName());
	}

	public int getAmountNeeded(Mission mission) {
		int level = getLevel(mission);
		int amount = 0;
		int increaseAmount = 0;

		if (mission.getMissionName().equalsIgnoreCase("GlassMiner")) {
			amount = Conf.glassMinedStartingAmount;
			increaseAmount = Conf.glassMinedIncreaseAmount;
		} else if (mission.getMissionName().equalsIgnoreCase("Miner")) {
			amount = Conf.blocksMinedStartingAmount;
			increaseAmount = Conf.blocksMinedIncreaseAmount;
		} else if (mission.getMissionName().equalsIgnoreCase("BlazeMurderer")) {
			amount = Conf.blazeKilledStartingAmount;
			increaseAmount = Conf.blazeKilledIncreaseAmount;
		} else if (mission.getMissionName().equalsIgnoreCase("Harvester")) {
			amount = Conf.sugarCaneStartingAmount;
			increaseAmount = Conf.sugarCaneIncreaseAmount;
		}

		int total = 0;

		if (level > 1)
			total = (level - 1) * increaseAmount;

		total = total + amount;

		return total;
	}

	public int getReward(Mission mission) {
		int level = getLevel(mission);
		int reward = 0;
		int increaseReward = 0;

		if (mission.getMissionName().equalsIgnoreCase("GlassMiner")) {
			reward = Conf.glassMinedStartingReward;
			increaseReward = Conf.glassMinedIncreaseReward;
		} else if (mission.getMissionName().equalsIgnoreCase("Miner")) {
			reward = Conf.blocksMinedStartingReward;
			increaseReward = Conf.blocksMinedIncreaseReward;
		} else if (mission.getMissionName().equalsIgnoreCase("BlazeMurderer")) {
			reward = Conf.blazeKilledStartingReward;
			increaseReward = Conf.blazeKilledIncreaseReward;
		} else if (mission.getMissionName().equalsIgnoreCase("Harvester")) {
			reward = Conf.sugarCaneStartingReward;
			increaseReward = Conf.sugarCaneIncreaseReward;
		}

		int total = 0;

		if (level > 1)
			total = (level - 1) * increaseReward;

		total = total + reward;

		return total;
	}

	public void endMission(EndReason endReason) {
		if (getActiveMission() == null)
			return;

		if (this.missionData == null)
			return;

		if (this.missionEndTime.containsKey(this.missionData.getActiveMission())) {
			this.missionEndTime.remove(this.missionData.getActiveMission());
		}

		this.missionEndTime.put(this.missionData.getActiveMission(), Long.valueOf(System.currentTimeMillis()));

		if (endReason == EndReason.TIME) {
			sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "The " + ChatColor.RED + this.missionData.getActiveMission() + ChatColor.GRAY
					+ " mission was ended due to time.");
		} else if (endReason == EndReason.COMPLETE) {
			sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "You have completed the " + ChatColor.RED + this.missionData.getActiveMission()
					+ ChatColor.GRAY + " mission! You can claim this by doing /f mission claim!");

			if (this.finishedMission.containsKey(this.missionData.getActiveMission()))
				this.finishedMission.put(this.missionData.getActiveMission(),
						Integer.valueOf(((Integer) this.finishedMission.get(this.missionData.getActiveMission())).intValue() + 1));
			else {
				this.finishedMission.put(this.missionData.getActiveMission(), Integer.valueOf(1));
			}

		} else if (endReason == EndReason.PLAYER) {
			sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "The " + ChatColor.RED + this.missionData.getActiveMission() + ChatColor.GRAY
					+ " mission was ended.");
		}

		this.missionData = null;
	}

	public void endMission() {
		sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.RED + "You have ended the " + ChatColor.RED + this.missionData.getActiveMission()
				+ ChatColor.GRAY + " mission!");
		endMission(EndReason.PLAYER);
	}

	public void startMission(Mission mission) {
		if (this.missionData != null)
			return;

		this.missionData = new MissionData(mission.getMissionName(), this.getPlayer(), 0, 0, 0, 0);
	}

	public Long getEndTime(String mission) {
		if (this.missionEndTime == null) {
			setMissionEndTime(new HashMap<String, Long>());
		}

		if (!this.missionEndTime.containsKey(mission)) {
			return Long.valueOf(0L);
		}

		if (604800L - (System.currentTimeMillis() - ((Long) this.missionEndTime.get(mission)).longValue()) / 1000L <= 0L) {
			this.missionEndTime.remove(mission);
			return Long.valueOf(0L);
		}

		return (Long) this.missionEndTime.get(mission);
	}

	public HashMap<String, Long> getMissionEndTime() {
		return this.missionEndTime;
	}

	public void setMissionEndTime(HashMap<String, Long> playerMissionEndTime) {
		this.missionEndTime = playerMissionEndTime;
	}

	public void setMissionLevels(HashMap<String, Integer> playerMissionEndTime) {
		this.missionLevel = playerMissionEndTime;
	}

	public void setFinishedMission(HashMap<String, Integer> finishedMission) {
		this.finishedMission = finishedMission;
	}

	public HashMap<String, Integer> getFinishedMission() {
		return this.finishedMission;
	}

	public boolean hasRedeemableMissions() {
		return !this.finishedMission.isEmpty();
	}

	@SuppressWarnings("deprecation")
	public void claimRewards() {
		for (String s : this.finishedMission.keySet()) {
			Mission mission = MissionManager.get().getMissionByName(s);

			if (mission != null) {
				Econ.econ.depositPlayer(this.getPlayer().getName(), mission.getMoneyReward(this));
				sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "$" + mission.getMoneyReward(this) + " has been added to your account.");
				upgradeLevel(s);
			}
		}

		this.finishedMission.clear();

		sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "You have redeemed all your rewards from your missions!");
	}

	public void login() {
		this.kills = getPlayer().getStatistic(Statistic.PLAYER_KILLS);
		this.deaths = getPlayer().getStatistic(Statistic.DEATHS);
	}

	public void logout() {
		this.kills = getPlayer().getStatistic(Statistic.PLAYER_KILLS);
		this.deaths = getPlayer().getStatistic(Statistic.DEATHS);
	}

	public Faction getFaction() {
		if (this.factionId == null) {
			this.factionId = "0";
		}
		return Factions.getInstance().getFactionById(this.factionId);
	}

	public String getFactionId() {
		return this.factionId;
	}

	public boolean hasFaction() {
		return !factionId.equals("0");
	}

	public void setFaction(Faction faction) {
		Faction oldFaction = this.getFaction();
		if (oldFaction != null) {
			oldFaction.removeFPlayer(this);
		}
		faction.addFPlayer(this);
		this.factionId = faction.getId();
	}

	public void setMonitorJoins(boolean monitor) {
		this.monitorJoins = monitor;
	}

	public boolean isMonitoringJoins() {
		return this.monitorJoins;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getCooldown() {
		int seconds = (int) ((this.commandCooldown - System.currentTimeMillis()) / 1000);
		return seconds;
	}

	public void setCooldown(long cooldown) {
		this.commandCooldown = cooldown;
	}

	public boolean cooldownEnded() {
		if (commandCooldown <= System.currentTimeMillis())
			return true;
		return false;
	}

	public double getPowerBoost() {
		return this.powerBoost;
	}

	public void setPowerBoost(double powerBoost) {
		this.powerBoost = powerBoost;
	}

	public boolean willAutoLeave() {
		return this.willAutoLeave;
	}

	public void setAutoLeave(boolean willLeave) {
		this.willAutoLeave = willLeave;
		P.p.debug(name + " set autoLeave to " + willLeave);
	}

	public boolean isInspecting() {
		return this.isInspecting;
	}

	public void setInspecting(boolean inspecting) {
		this.isInspecting = inspecting;
	}

	public long getLastInspectionTime() {
		return this.lastInspectionTime;
	}

	public void setLastInspectionTime(long lastInspectionTime) {
		this.lastInspectionTime = lastInspectionTime;
	}

	public long getLastFrostwalkerMessage() {
		return this.lastFrostwalkerMessage;
	}

	public void setLastFrostwalkerMessage() {
		this.lastFrostwalkerMessage = System.currentTimeMillis();
	}

	public Faction getAutoClaimFor() {
		return autoClaimFor;
	}

	public void setAutoClaimFor(Faction faction) {
		this.autoClaimFor = faction;
		if (this.autoClaimFor != null) {
			// TODO: merge these into same autoclaim
			this.autoSafeZoneEnabled = false;
			this.autoWarZoneEnabled = false;
		}
	}

	public boolean isAutoSafeClaimEnabled() {
		return autoSafeZoneEnabled;
	}

	public void setIsAutoSafeClaimEnabled(boolean enabled) {
		this.autoSafeZoneEnabled = enabled;
		if (enabled) {
			this.autoClaimFor = null;
			this.autoWarZoneEnabled = false;
		}
	}

	public boolean isAutoWarClaimEnabled() {
		return autoWarZoneEnabled;
	}

	public void setIsAutoWarClaimEnabled(boolean enabled) {
		this.autoWarZoneEnabled = enabled;
		if (enabled) {
			this.autoClaimFor = null;
			this.autoSafeZoneEnabled = false;
		}
	}

	public boolean isAdminBypassing() {
		return this.isAdminBypassing;
	}

	public boolean isVanished() {
		return Essentials.isVanished(getPlayer());
	}

	public void setIsAdminBypassing(boolean val) {
		this.isAdminBypassing = val;
	}

	public void setChatMode(ChatMode chatMode) {
		this.chatMode = chatMode;
	}

	public ChatMode getChatMode() {
		if (this.factionId.equals("0") || !Conf.factionOnlyChat) {
			this.chatMode = ChatMode.PUBLIC;
		}
		return chatMode;
	}

	public void setIgnoreAllianceChat(boolean ignore) {
		this.ignoreAllianceChat = ignore;
	}

	public boolean isIgnoreAllianceChat() {
		return ignoreAllianceChat;
	}

	public void setSpyingChat(boolean chatSpying) {
		this.spyingChat = chatSpying;
	}

	public boolean isSpyingChat() {
		return spyingChat;
	}

	// FIELD: account
	public String getAccountId() {
		return this.getId();
	}

	public MemoryFPlayer() {
	}

	public MemoryFPlayer(String id) {
		this.id = id;
		this.resetFactionData(false);
		this.power = Conf.powerPlayerStarting;
		this.lastPowerUpdateTime = System.currentTimeMillis();
		this.lastLoginTime = System.currentTimeMillis();
		this.mapAutoUpdating = false;
		this.autoClaimFor = null;
		this.autoSafeZoneEnabled = false;
		this.autoWarZoneEnabled = false;
		this.loginPvpDisabled = Conf.noPVPDamageToOthersForXSecondsAfterLogin > 0;
		this.powerBoost = 0.0;
		this.showScoreboard = P.p.getConfig().getBoolean("scoreboard.default-enabled", false);
		this.kills = 0;
		this.deaths = 0;
		this.mapHeight = Conf.mapHeight;
		setMissionData(this.missionData);
		setMissionEndTime(this.missionEndTime);
		setFinishedMission(this.finishedMission);
		setMissionLevels(this.missionLevel);

		if (!Conf.newPlayerStartingFactionID.equals("0") && Factions.getInstance().isValidFactionId(Conf.newPlayerStartingFactionID)) {
			this.factionId = Conf.newPlayerStartingFactionID;
		}
	}

	public MemoryFPlayer(MemoryFPlayer other) {
		this.factionId = other.factionId;
		this.id = other.id;
		this.power = other.power;
		this.lastLoginTime = other.lastLoginTime;
		this.mapAutoUpdating = other.mapAutoUpdating;
		this.autoClaimFor = other.autoClaimFor;
		this.autoSafeZoneEnabled = other.autoSafeZoneEnabled;
		this.autoWarZoneEnabled = other.autoWarZoneEnabled;
		this.loginPvpDisabled = other.loginPvpDisabled;
		this.powerBoost = other.powerBoost;
		this.role = other.role;
		this.title = other.title;
		this.chatMode = other.chatMode;
		this.spyingChat = other.spyingChat;
		this.lastStoodAt = other.lastStoodAt;
		this.isAdminBypassing = other.isAdminBypassing;
		this.showScoreboard = P.p.getConfig().getBoolean("scoreboard.default-enabled", true);
		this.kills = other.kills;
		this.deaths = other.deaths;
		this.mapHeight = Conf.mapHeight;
		setMissionData(this.missionData);
		setMissionEndTime(this.missionEndTime);
		setFinishedMission(this.finishedMission);
		setMissionLevels(this.missionLevel);
	}

	public void resetFactionData(boolean doSpoutUpdate) {
		// clean up any territory ownership in old faction, if there is one
		if (factionId != null && Factions.getInstance().isValidFactionId(this.getFactionId())) {
			Faction currentFaction = this.getFaction();
			currentFaction.removeFPlayer(this);
			if (currentFaction.isNormal()) {
				currentFaction.clearClaimOwnership(this);
			}
		}

		this.factionId = "0"; // The default neutral faction
		this.chatMode = ChatMode.PUBLIC;
		this.role = Role.MEMBER;
		this.title = "";
		this.autoClaimFor = null;
	}

	public void resetFactionData() {
		this.resetFactionData(true);
	}

	// -------------------------------------------- //
	// Getters And Setters
	// -------------------------------------------- //

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		losePowerFromBeingOffline();
		this.lastLoginTime = lastLoginTime;
		this.lastPowerUpdateTime = lastLoginTime;
		if (Conf.noPVPDamageToOthersForXSecondsAfterLogin > 0) {
			this.loginPvpDisabled = true;
		}
	}

	public boolean isMapAutoUpdating() {
		return mapAutoUpdating;
	}

	public void setMapAutoUpdating(boolean mapAutoUpdating) {
		this.mapAutoUpdating = mapAutoUpdating;
	}

	public boolean hasLoginPvpDisabled() {
		if (!loginPvpDisabled) {
			return false;
		}
		if (this.lastLoginTime + (Conf.noPVPDamageToOthersForXSecondsAfterLogin * 1000) < System.currentTimeMillis()) {
			this.loginPvpDisabled = false;
			return false;
		}
		return true;
	}

	public FLocation getLastStoodAt() {
		return this.lastStoodAt;
	}

	public void setLastStoodAt(FLocation flocation) {
		this.lastStoodAt = flocation;
	}

	// ----------------------------------------------//
	// Title, Name, Faction Tag and Chat
	// ----------------------------------------------//

	// Base:

	public String getTitle() {
		return this.hasFaction() ? title : TL.NOFACTION_PREFIX.toString();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		if (this.name == null) {
			// Older versions of FactionsUUID don't save the name,
			// so `name` will be null the first time it's retrieved
			// after updating
			OfflinePlayer offline = Bukkit.getOfflinePlayer(UUID.fromString(getId()));
			this.name = offline.getName() != null ? offline.getName() : getId();
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return this.hasFaction() ? this.getFaction().getTag() : "";
	}

	// Base concatenations:

	public String getNameAndSomething(String something) {
		String ret = this.role.getPrefix();
		if (something.length() > 0) {
			ret += something + " ";
		}
		ret += this.getName();
		return ret;
	}

	public String getNameAndTitle() {
		return this.getNameAndSomething(this.getTitle());
	}

	public String getNameAndTag() {
		return this.getNameAndSomething(this.getTag());
	}

	// Colored concatenations:
	// These are used in information messages

	public String getNameAndTitle(Faction faction) {
		return this.getColorTo(faction) + this.getNameAndTitle();
	}

	public String getNameAndTitle(MemoryFPlayer fplayer) {
		return this.getColorTo(fplayer) + this.getNameAndTitle();
	}

	// Chat Tag:
	// These are injected into the format of global chat messages.

	public String getChatTag() {
		return this.hasFaction() ? String.format(Conf.chatTagFormat, this.role.getPrefix() + this.getTag()) : TL.NOFACTION_PREFIX.toString();
	}

	// Colored Chat Tag
	public String getChatTag(Faction faction) {
		return this.hasFaction() ? this.getRelationTo(faction).getColor() + getChatTag() : "";
	}

	public String getChatTag(MemoryFPlayer fplayer) {
		return this.hasFaction() ? this.getColorTo(fplayer) + getChatTag() : "";
	}

	public int getKills() {
		return isOnline() ? getPlayer().getStatistic(Statistic.PLAYER_KILLS) : this.kills;
	}

	public int getDeaths() {
		return isOnline() ? getPlayer().getStatistic(Statistic.DEATHS) : this.deaths;

	}

	// -------------------------------
	// Relation and relation colors
	// -------------------------------

	@Override
	public String describeTo(RelationParticipator that, boolean ucfirst) {
		return RelationUtil.describeThatToMe(this, that, ucfirst);
	}

	@Override
	public String describeTo(RelationParticipator that) {
		return RelationUtil.describeThatToMe(this, that);
	}

	@Override
	public Relation getRelationTo(RelationParticipator rp) {
		return RelationUtil.getRelationTo(this, rp);
	}

	@Override
	public Relation getRelationTo(RelationParticipator rp, boolean ignorePeaceful) {
		return RelationUtil.getRelationTo(this, rp, ignorePeaceful);
	}

	public Relation getRelationToLocation() {
		return Board.getInstance().getFactionAt(new FLocation(this)).getRelationTo(this);
	}

	@Override
	public ChatColor getColorTo(RelationParticipator rp) {
		return RelationUtil.getColorOfThatToMe(this, rp);
	}

	// ----------------------------------------------//
	// Health
	// ----------------------------------------------//
	public void heal(int amnt) {
		Player player = this.getPlayer();
		if (player == null) {
			return;
		}
		player.setHealth(player.getHealth() + amnt);
	}

	// ----------------------------------------------//
	// Power
	// ----------------------------------------------//
	public double getPower() {
		this.updatePower();
		return this.power;
	}

	public void alterPower(double delta) {
		this.power += delta;
		if (this.power > this.getPowerMax()) {
			this.power = this.getPowerMax();
		} else if (this.power < this.getPowerMin()) {
			this.power = this.getPowerMin();
		}
	}

	public double getPowerMax() {
		return Conf.powerPlayerMax + this.powerBoost;
	}

	public double getPowerMin() {
		return Conf.powerPlayerMin + this.powerBoost;
	}

	public int getPowerRounded() {
		return (int) Math.round(this.getPower());
	}

	public int getPowerMaxRounded() {
		return (int) Math.round(this.getPowerMax());
	}

	public int getPowerMinRounded() {
		return (int) Math.round(this.getPowerMin());
	}

	public void updatePower() {
		if (this.isOffline()) {
			losePowerFromBeingOffline();
			if (!Conf.powerRegenOffline) {
				return;
			}
		} else if (hasFaction() && getFaction().isPowerFrozen()) {
			return; // Don't let power regen if faction power is frozen.
		}
		long now = System.currentTimeMillis();
		long millisPassed = now - this.lastPowerUpdateTime;
		this.lastPowerUpdateTime = now;

		Player thisPlayer = this.getPlayer();
		if (thisPlayer != null && thisPlayer.isDead()) {
			return; // don't let dead players regain power until they respawn
		}

		int millisPerMinute = 60 * 1000;
		this.alterPower(millisPassed * Conf.powerPerMinute / millisPerMinute);
	}

	public void losePowerFromBeingOffline() {
		if (Conf.powerOfflineLossPerDay > 0.0 && this.power > Conf.powerOfflineLossLimit) {
			long now = System.currentTimeMillis();
			long millisPassed = now - this.lastPowerUpdateTime;
			this.lastPowerUpdateTime = now;

			double loss = millisPassed * Conf.powerOfflineLossPerDay / (24 * 60 * 60 * 1000);
			if (this.power - loss < Conf.powerOfflineLossLimit) {
				loss = this.power;
			}
			this.alterPower(-loss);
		}
	}

	public void onDeath() {
		this.updatePower();
		this.alterPower(-Conf.powerPerDeath);
		if (hasFaction()) {
			getFaction().setLastDeath(System.currentTimeMillis());
		}
	}

	// ----------------------------------------------//
	// Territory
	// ----------------------------------------------//
	public boolean isInOwnTerritory() {
		return Board.getInstance().getFactionAt(new FLocation(this)) == this.getFaction();
	}

	public boolean isInOthersTerritory() {
		Faction factionHere = Board.getInstance().getFactionAt(new FLocation(this));
		return factionHere != null && factionHere.isNormal() && factionHere != this.getFaction();
	}

	public boolean isInAllyTerritory() {
		return Board.getInstance().getFactionAt(new FLocation(this)).getRelationTo(this).isAlly();
	}

	public boolean isInNeutralTerritory() {
		return Board.getInstance().getFactionAt(new FLocation(this)).getRelationTo(this).isNeutral();
	}

	public boolean isInEnemyTerritory() {
		return Board.getInstance().getFactionAt(new FLocation(this)).getRelationTo(this).isEnemy();
	}

	public void sendFactionHereMessage(Faction from) {
		Faction toShow = Board.getInstance().getFactionAt(getLastStoodAt());
		boolean showChat = true;
		if (showInfoBoard(toShow)) {
			FScoreboard.get(this).setTemporarySidebar(new FInfoSidebar(toShow));
			showChat = P.p.getConfig().getBoolean("scoreboard.also-send-chat", true);
		}
		if (showChat) {
			this.sendMessage(P.p.txt.parse(TL.FACTION_LEAVE.format(from.getTag(this), toShow.getTag(this))));
		}
	}

	/**
	 * Check if the scoreboard should be shown. Simple method to be used by
	 * above method.
	 *
	 * @param toShow
	 *            Faction to be shown.
	 * @return true if should show, otherwise false.
	 */
	public boolean showInfoBoard(Faction toShow) {
		return showScoreboard && !toShow.isWarZone() && !toShow.isWilderness() && !toShow.isSafeZone()
				&& P.p.getConfig().contains("scoreboard.finfo") && P.p.getConfig().getBoolean("scoreboard.finfo-enabled", false)
				&& FScoreboard.get(this) != null;
	}

	@Override
	public boolean showScoreboard() {
		return this.showScoreboard;
	}

	@Override
	public void setShowScoreboard(boolean show) {
		this.showScoreboard = show;
	}

	// -------------------------------
	// Actions
	// -------------------------------

	public void leave(boolean makePay) {
		Faction myFaction = this.getFaction();
		makePay = makePay && Econ.shouldBeUsed() && !this.isAdminBypassing();

		if (myFaction == null) {
			resetFactionData();
			return;
		}

		boolean perm = myFaction.isPermanent();

		if (!perm && this.getRole() == Role.LEADER && myFaction.getFPlayers().size() > 1) {
			msg(TL.LEAVE_PASSADMIN);
			return;
		}

		if (!Conf.canLeaveWithNegativePower && this.getPower() < 0) {
			msg(TL.LEAVE_NEGATIVEPOWER);
			return;
		}

		// if economy is enabled and they're not on the bypass list, make sure
		// they can pay
		if (makePay && !Econ.hasAtLeast(this, Conf.econCostLeave, TL.LEAVE_TOLEAVE.toString())) {
			return;
		}

		FPlayerLeaveEvent leaveEvent = new FPlayerLeaveEvent(this, myFaction, FPlayerLeaveEvent.PlayerLeaveReason.LEAVE);
		Bukkit.getServer().getPluginManager().callEvent(leaveEvent);
		if (leaveEvent.isCancelled()) {
			return;
		}

		// then make 'em pay (if applicable)
		if (makePay && !Econ.modifyMoney(this, -Conf.econCostLeave, TL.LEAVE_TOLEAVE.toString(), TL.LEAVE_FORLEAVE.toString())) {
			return;
		}

		// Am I the last one in the faction?
		if (myFaction.getFPlayers().size() == 1) {
			// Transfer all money
			if (Econ.shouldBeUsed()) {
				Econ.transferMoney(this, myFaction, this, Econ.getBalance(myFaction.getAccountId()));
			}
		}

		if (myFaction.isNormal()) {
			for (FPlayer fplayer : myFaction.getFPlayersWhereOnline(true)) {
				fplayer.msg(TL.LEAVE_LEFT, this.describeTo(fplayer, true), myFaction.describeTo(fplayer));
			}

			if (Conf.logFactionLeave) {
				P.p.log(TL.LEAVE_LEFT.format(this.getName(), myFaction.getTag()));
			}
		}

		myFaction.removeAnnouncements(this);
		this.resetFactionData();

		if (myFaction.isNormal() && !perm && myFaction.getFPlayers().isEmpty()) {
			// Remove this faction
			for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
				fplayer.msg(TL.LEAVE_DISBANDED, myFaction.describeTo(fplayer, true));
			}

			Factions.getInstance().removeFaction(myFaction.getId());
			if (Conf.logFactionDisband) {
				P.p.log(TL.LEAVE_DISBANDEDLOG.format(myFaction.getTag(), myFaction.getId(), this.getName()));
			}
		}
	}

	public boolean isStealth() {
		return isStealth;
	}

	public void setStealth(boolean stealth) {
		this.isStealth = stealth;
	}

	public boolean isFlying() {
		return isFlying;
	}

	public void setFlying(boolean fly) {
		setFFlying(fly, false, false);
	}

	public void setFFlying(boolean fly, boolean damage, boolean enemy) {
		Player player = getPlayer();
		if (player != null) {
			player.setAllowFlight(fly);
			player.setFlying(fly);
		}

		if (!damage && !enemy) {
			msg(TL.COMMAND_FLY_CHANGE, fly ? "enabled" : "disabled");
		} else if (damage) {
			msg(TL.COMMAND_FLY_DAMAGE);
		} else {
			msg(TL.COMMAND_FLY_ENEMY_DISABLE);
		}

		// If leaving fly mode, don't let them take fall damage for x seconds.
		if (!fly) {
			int cooldown = P.p.getConfig().getInt("f-fly.falldamage-cooldown", 3);

			// If the value is 0 or lower, make them take fall damage.
			// Otherwise, start a timer and have this cancel after a few
			// seconds.
			// Short task so we're just doing it in method. Not clean but eh.
			if (cooldown > 0) {
				setTakeFallDamage(false);
				Bukkit.getScheduler().runTaskLater(P.p, new Runnable() {
					@Override
					public void run() {
						setTakeFallDamage(true);
					}
				}, 20L * cooldown);
			}

			new FlightDisableUtil().getFlying().remove(this);
		} else {
			new FlightDisableUtil().getFlying().add(this);
		}

		isFlying = fly;
	}

	public boolean canFlyAtLocation() {
		return canFlyAtLocation(lastStoodAt);
	}

	public boolean canFlyAtLocation(FLocation location) {
		Faction faction = Board.getInstance().getFactionAt(location);
		if (faction.isWilderness() || faction.isSafeZone() || faction.isWarZone()) {
			return false;
		}

		// Admins can always fly in their territory.
		// admin bypass (ops) can fly as well.
		if (isAdminBypassing || (faction == getFaction() && getRole().isAtLeast(Role.LEADER))) {
			return true;
		}

		Access access = faction.getAccess(this, PermissableAction.FLY);

		if (access == null || access == Access.UNDEFINED) {

			// If access is null or undefined, we'll default to the conf.json
			switch (faction.getRelationTo(getFaction())) {
			case ENEMY:
				return Conf.defaultFlyPermEnemy;
			case ALLY:
				return Conf.defaultFlyPermAlly;
			case NEUTRAL:
				return Conf.defaultFlyPermNeutral;
			case TRUCE:
				return Conf.defaultFlyPermTruce;
			case MEMBER:
				return Conf.defaultFlyPermMember;
			default:
				return false; // should never reach.
			}

		}

		return access == Access.ALLOW;
	}

	public boolean shouldTakeFallDamage() {
		return this.shouldTakeFallDamage;
	}

	public void setTakeFallDamage(boolean fallDamage) {
		this.shouldTakeFallDamage = fallDamage;
	}

	public void setTitle(CommandSender sender, String title) {
		// Check if the setter has it.
		if (sender.hasPermission(Permission.TITLE_COLOR.node)) {
			title = ChatColor.translateAlternateColorCodes('&', title);
		}

		this.title = title;
	}

	public boolean canClaimForFaction(Faction forFaction) {
		return this.isAdminBypassing() || !forFaction.isWilderness() && (forFaction == this.getFaction() && this.getRole().isAtLeast(Role.MODERATOR))
				|| (forFaction.isSafeZone() && Permission.MANAGE_SAFE_ZONE.has(getPlayer()))
				|| (forFaction.isWarZone() && Permission.MANAGE_WAR_ZONE.has(getPlayer()));
	}

	public boolean canClaimForFactionAtLocation(Faction forFaction, Location location, boolean notifyFailure) {
		return canClaimForFactionAtLocation(forFaction, new FLocation(location), notifyFailure);
	}

	public boolean canClaimForFactionAtLocation(Faction forFaction, FLocation flocation, boolean notifyFailure) {
		String error = null;
		Faction myFaction = getFaction();
		Faction currentFaction = Board.getInstance().getFactionAt(flocation);
		int ownedLand = forFaction.getLandRounded();
		int factionBuffer = P.p.getConfig().getInt("hcf.buffer-zone", 0);
		int worldBuffer = P.p.getConfig().getInt("world-border.buffer", 0);

		if (Conf.worldGuardChecking && Worldguard.checkForRegionsInChunk(flocation)) {
			// Checks for WorldGuard regions in the chunk attempting to be
			// claimed
			error = P.p.txt.parse(TL.CLAIM_PROTECTED.toString());
		} else if (Conf.worldsNoClaiming.contains(flocation.getWorldName())) {
			error = P.p.txt.parse(TL.CLAIM_DISABLED.toString());
		} else if (this.isAdminBypassing()) {
			return true;
		} else if (forFaction.isSafeZone() && Permission.MANAGE_SAFE_ZONE.has(getPlayer())) {
			return true;
		} else if (forFaction.isWarZone() && Permission.MANAGE_WAR_ZONE.has(getPlayer())) {
			return true;
		} else if (myFaction != forFaction) {
			error = P.p.txt.parse(TL.CLAIM_CANTCLAIM.toString(), forFaction.describeTo(this));
		} else if (forFaction == currentFaction) {
			error = P.p.txt.parse(TL.CLAIM_ALREADYOWN.toString(), forFaction.describeTo(this, true));
		} else if (this.getRole().value < Role.MODERATOR.value) {
			error = P.p.txt.parse(TL.CLAIM_MUSTBE.toString(), Role.MODERATOR.getTranslation());
		} else if (forFaction.getFPlayers().size() < Conf.claimsRequireMinFactionMembers) {
			error = P.p.txt.parse(TL.CLAIM_MEMBERS.toString(), Conf.claimsRequireMinFactionMembers);
		} else if (currentFaction.isSafeZone()) {
			error = P.p.txt.parse(TL.CLAIM_SAFEZONE.toString());
		} else if (currentFaction.isWarZone()) {
			error = P.p.txt.parse(TL.CLAIM_WARZONE.toString());
		} else if (P.p.getConfig().getBoolean("hcf.allow-overclaim", true) && ownedLand >= forFaction.getPowerRounded()) {
			error = P.p.txt.parse(TL.CLAIM_POWER.toString());
		} else if (Conf.claimedLandsMax != 0 && ownedLand >= Conf.claimedLandsMax && forFaction.isNormal()) {
			error = P.p.txt.parse(TL.CLAIM_LIMIT.toString());
		} else if (currentFaction.getRelationTo(forFaction) == Relation.ALLY) {
			error = P.p.txt.parse(TL.CLAIM_ALLY.toString());
		} else if (Conf.claimsMustBeConnected && !this.isAdminBypassing() && myFaction.getLandRoundedInWorld(flocation.getWorldName()) > 0
				&& !Board.getInstance().isConnectedLocation(flocation, myFaction)
				&& (!Conf.claimsCanBeUnconnectedIfOwnedByOtherFaction || !currentFaction.isNormal())) {
			if (Conf.claimsCanBeUnconnectedIfOwnedByOtherFaction) {
				error = P.p.txt.parse(TL.CLAIM_CONTIGIOUS.toString());
			} else {
				error = P.p.txt.parse(TL.CLAIM_FACTIONCONTIGUOUS.toString());
			}
		} else if (factionBuffer > 0 && Board.getInstance().hasFactionWithin(flocation, myFaction, factionBuffer)) {
			error = P.p.txt.parse(TL.CLAIM_TOOCLOSETOOTHERFACTION.format(factionBuffer));
		} else if (flocation.isOutsideWorldBorder(worldBuffer)) {
			if (worldBuffer > 0) {
				error = P.p.txt.parse(TL.CLAIM_OUTSIDEBORDERBUFFER.format(worldBuffer));
			} else {
				error = P.p.txt.parse(TL.CLAIM_OUTSIDEWORLDBORDER.toString());
			}
		} else if (currentFaction.isNormal()) {
			if (myFaction.isPeaceful()) {
				error = P.p.txt.parse(TL.CLAIM_PEACEFUL.toString(), currentFaction.getTag(this));
			} else if (currentFaction.isPeaceful()) {
				error = P.p.txt.parse(TL.CLAIM_PEACEFULTARGET.toString(), currentFaction.getTag(this));
			} else if (!currentFaction.hasLandInflation()) {
				// TODO more messages WARN current faction most importantly
				error = P.p.txt.parse(TL.CLAIM_THISISSPARTA.toString(), currentFaction.getTag(this));
			} else if (currentFaction.hasLandInflation() && !P.p.getConfig().getBoolean("hcf.allow-overclaim", true)) {
				// deny over claim when it normally would be allowed.
				error = P.p.txt.parse(TL.CLAIM_OVERCLAIM_DISABLED.toString());
			} else if (!Board.getInstance().isBorderLocation(flocation)) {
				error = P.p.txt.parse(TL.CLAIM_BORDER.toString());
			}
		}
		// TODO: Add more else if statements.

		if (notifyFailure && error != null) {
			msg(error);
		}
		return error == null;
	}

	public boolean attemptClaim(Faction forFaction, Location location, boolean notifyFailure) {
		return attemptClaim(forFaction, new FLocation(location), notifyFailure);
	}

	public boolean attemptClaim(Faction forFaction, FLocation flocation, boolean notifyFailure) {
		// notifyFailure is false if called by auto-claim; no need to notify on
		// every failure for it
		// return value is false on failure, true on success

		Faction currentFaction = Board.getInstance().getFactionAt(flocation);

		int ownedLand = forFaction.getLandRounded();

		if (!this.canClaimForFactionAtLocation(forFaction, flocation, notifyFailure)) {
			return false;
		}

		// if economy is enabled and they're not on the bypass list, make sure
		// they can pay
		boolean mustPay = Econ.shouldBeUsed() && !this.isAdminBypassing() && !forFaction.isSafeZone() && !forFaction.isWarZone();
		double cost = 0.0;
		EconomyParticipator payee = null;
		if (mustPay) {
			cost = Econ.calculateClaimCost(ownedLand, currentFaction.isNormal());

			if (Conf.econClaimUnconnectedFee != 0.0 && forFaction.getLandRoundedInWorld(flocation.getWorldName()) > 0
					&& !Board.getInstance().isConnectedLocation(flocation, forFaction)) {
				cost += Conf.econClaimUnconnectedFee;
			}

			if (Conf.bankEnabled && Conf.bankFactionPaysLandCosts && this.hasFaction()) {
				payee = this.getFaction();
			} else {
				payee = this;
			}

			if (!Econ.hasAtLeast(payee, cost, TL.CLAIM_TOCLAIM.toString())) {
				return false;
			}
		}

		LandClaimEvent claimEvent = new LandClaimEvent(flocation, forFaction, this);
		Bukkit.getServer().getPluginManager().callEvent(claimEvent);
		if (claimEvent.isCancelled()) {
			return false;
		}

		// then make 'em pay (if applicable)
		if (mustPay && !Econ.modifyMoney(payee, -cost, TL.CLAIM_TOCLAIM.toString(), TL.CLAIM_FORCLAIM.toString())) {
			return false;
		}

		// Was an over claim
		if (currentFaction.isNormal() && currentFaction.hasLandInflation()) {
			// Give them money for over claiming.
			Econ.modifyMoney(payee, Conf.econOverclaimRewardMultiplier, TL.CLAIM_TOOVERCLAIM.toString(), TL.CLAIM_FOROVERCLAIM.toString());
		}

		// announce success
		Set<FPlayer> informTheseFPlayers = new HashSet<>();
		informTheseFPlayers.add(this);
		informTheseFPlayers.addAll(forFaction.getFPlayersWhereOnline(true));
		for (FPlayer fp : informTheseFPlayers) {
			fp.msg(TL.CLAIM_CLAIMED, this.describeTo(fp, true), forFaction.describeTo(fp), currentFaction.describeTo(fp));
		}

		Board.getInstance().setFactionAt(forFaction, flocation);

		if (Conf.logLandClaims) {
			P.p.log(TL.CLAIM_CLAIMEDLOG.toString(), this.getName(), flocation.getCoordString(), forFaction.getTag());
		}

		return true;
	}

	public boolean shouldBeSaved() {
		return this.hasFaction()
				|| (this.getPowerRounded() != this.getPowerMaxRounded() && this.getPowerRounded() != (int) Math.round(Conf.powerPlayerStarting));
	}

	public void msg(String str, Object... args) {
		this.sendMessage(P.p.txt.parse(str, args));
	}

	public void msg(TL translation, Object... args) {
		this.msg(translation.toString(), args);
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(UUID.fromString(this.getId()));
	}

	public boolean isOnline() {
		return this.getPlayer() != null;
	}

	// make sure target player should be able to detect that this player is
	// online
	public boolean isOnlineAndVisibleTo(Player player) {
		Player target = this.getPlayer();
		return target != null && player.canSee(target);
	}

	public boolean isOffline() {
		return !isOnline();
	}

	// -------------------------------------------- //
	// Message Sending Helpers
	// -------------------------------------------- //

	public void sendMessage(String msg) {
		if (msg.contains("{null}")) {
			return; // user wants this message to not send
		}
		if (msg.contains("/n/")) {
			for (String s : msg.split("/n/")) {
				sendMessage(s);
			}
			return;
		}
		Player player = this.getPlayer();
		if (player == null) {
			return;
		}
		player.sendMessage(msg);
	}

	public void sendMessage(List<String> msgs) {
		for (String msg : msgs) {
			this.sendMessage(msg);
		}
	}

	public void sendFancyMessage(FancyMessage message) {
		Player player = getPlayer();
		if (player == null || !player.isOnGround()) {
			return;
		}

		message.send(player);
	}

	public void sendFancyMessage(List<FancyMessage> messages) {
		Player player = getPlayer();
		if (player == null || !player.isOnGround()) {
			return;
		}

		for (FancyMessage msg : messages) {
			msg.send(player);
		}
	}

	public int getMapHeight() {
		return this.mapHeight;
	}

	public void setMapHeight(int height) {
		this.mapHeight = height > Conf.mapHeight * 2 ? Conf.mapHeight * 2 : height;
	}

	public String getNameAndTitle(FPlayer fplayer) {
		return this.getColorTo(fplayer) + this.getNameAndTitle();
	}

	@Override
	public String getChatTag(FPlayer fplayer) {
		return this.hasFaction() ? this.getRelationTo(fplayer).getColor() + getChatTag() : "";
	}

	@Override
	public String getId() {
		return id;
	}

	public abstract void remove();

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void clearWarmup() {
		if (warmup != null) {
			Bukkit.getScheduler().cancelTask(warmupTask);
			this.stopWarmup();
		}
	}

	@Override
	public void stopWarmup() {
		warmup = null;
	}

	@Override
	public boolean isWarmingUp() {
		return warmup != null;
	}

	@Override
	public WarmUpUtil.Warmup getWarmupType() {
		return warmup;
	}

	@Override
	public void addWarmup(WarmUpUtil.Warmup warmup, int taskId) {
		if (this.warmup != null) {
			this.clearWarmup();
		}
		this.warmup = warmup;
		this.warmupTask = taskId;
	}
}