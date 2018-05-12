package com.massivecraft.factions.cmd;

import java.util.Collections;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.util.TL;

public class FCmdRoot extends FCommand {

	public CmdAdmin cmdAdmin = new CmdAdmin();
	public CmdAutoClaim cmdAutoClaim = new CmdAutoClaim();
	public CmdBoom cmdBoom = new CmdBoom();
	public CmdBypass cmdBypass = new CmdBypass();
	public CmdChat cmdChat = new CmdChat();
	public CmdChatSpy cmdChatSpy = new CmdChatSpy();
	public CmdClaim cmdClaim = new CmdClaim();
	public CmdConfig cmdConfig = new CmdConfig();
	public CmdCreate cmdCreate = new CmdCreate();
	public CmdDeinvite cmdDeinvite = new CmdDeinvite();
	public CmdDescription cmdDescription = new CmdDescription();
	public CmdDisband cmdDisband = new CmdDisband();
	public CmdFly cmdFly = new CmdFly();
	public CmdHelp cmdHelp = new CmdHelp();
	public CmdHome cmdHome = new CmdHome();
	public CmdInvite cmdInvite = new CmdInvite();
	public CmdJoin cmdJoin = new CmdJoin();
	public CmdKick cmdKick = new CmdKick();
	public CmdLeave cmdLeave = new CmdLeave();
	public CmdList cmdList = new CmdList();
	public CmdLock cmdLock = new CmdLock();
	public CmdMap cmdMap = new CmdMap();
	public CmdMod cmdMod = new CmdMod();
	public CmdMoney cmdMoney = new CmdMoney();
	public CmdOpen cmdOpen = new CmdOpen();
	public CmdOwner cmdOwner = new CmdOwner();
	public CmdOwnerList cmdOwnerList = new CmdOwnerList();
	public CmdPeaceful cmdPeaceful = new CmdPeaceful();
	public CmdPermanent cmdPermanent = new CmdPermanent();
	public CmdPermanentPower cmdPermanentPower = new CmdPermanentPower();
	public CmdPowerBoost cmdPowerBoost = new CmdPowerBoost();
	public CmdPower cmdPower = new CmdPower();
	public CmdRelationAlly cmdRelationAlly = new CmdRelationAlly();
	public CmdRelationEnemy cmdRelationEnemy = new CmdRelationEnemy();
	public CmdRelationNeutral cmdRelationNeutral = new CmdRelationNeutral();
	public CmdRelationTruce cmdRelationTruce = new CmdRelationTruce();
	public CmdReload cmdReload = new CmdReload();
	public CmdSafeunclaimall cmdSafeunclaimall = new CmdSafeunclaimall();
	public CmdSaveAll cmdSaveAll = new CmdSaveAll();
	public CmdSethome cmdSethome = new CmdSethome();
	public CmdShow cmdShow = new CmdShow();
	public CmdStatus cmdStatus = new CmdStatus();
	public CmdStuck cmdStuck = new CmdStuck();
	public CmdTag cmdTag = new CmdTag();
	public CmdTitle cmdTitle = new CmdTitle();
	public CmdToggleAllianceChat cmdToggleAllianceChat = new CmdToggleAllianceChat();
	public CmdUnclaim cmdUnclaim = new CmdUnclaim();
	public CmdUnclaimall cmdUnclaimall = new CmdUnclaimall();
	public CmdVersion cmdVersion = new CmdVersion();
	public CmdWarunclaimall cmdWarunclaimall = new CmdWarunclaimall();
	public CmdSB cmdSB = new CmdSB();
	public CmdShowInvites cmdShowInvites = new CmdShowInvites();
	public CmdAnnounce cmdAnnounce = new CmdAnnounce();
	public CmdSeeChunk cmdSeeChunk = new CmdSeeChunk();
	public CmdConvert cmdConvert = new CmdConvert();
	public CmdFWarp cmdFWarp = new CmdFWarp();
	public CmdSetFWarp cmdSetFWarp = new CmdSetFWarp();
	public CmdDelFWarp cmdDelFWarp = new CmdDelFWarp();
	public CmdModifyPower cmdModifyPower = new CmdModifyPower();
	public CmdLogins cmdLogins = new CmdLogins();
	public CmdClaimLine cmdClaimLine = new CmdClaimLine();
	public CmdTop cmdTop = new CmdTop();
	public CmdAHome cmdAHome = new CmdAHome();
	public CmdPerm cmdPerm = new CmdPerm();
	public CmdPromote cmdPromote = new CmdPromote();
	public CmdDemote cmdDemote = new CmdDemote();
	public CmdSetDefaultRole cmdSetDefaultRole = new CmdSetDefaultRole();
	public CmdMapHeight cmdMapHeight = new CmdMapHeight();
	public CmdClaimAt cmdClaimAt = new CmdClaimAt();
	public CmdBan cmdban = new CmdBan();
	public CmdUnban cmdUnban = new CmdUnban();
	public CmdBanlist cmdbanlist = new CmdBanlist();
	public CmdCoLeader cmdCoLeader = new CmdCoLeader();
	public CmdStealth cmdStealth = new CmdStealth();
	public CmdInspect cmdInspect = new CmdInspect();
	public CmdValue cmdValue = new CmdValue();
	public CmdUpgrades cmdUpgrade = new CmdUpgrades();
	public CmdChest cmdChest = new CmdChest();
	public CmdChestLogs cmdChestLogs = new CmdChestLogs();
	public CmdMission cmdMissions = new CmdMission();
	public CmdTNTFill cmdTNTFill = new CmdTNTFill();
	public CmdTNTBankDeposit cmdDepositTNT = new CmdTNTBankDeposit();
	public CmdTNTCount cmdTNTCount = new CmdTNTCount();
	public CmdOptions cmdOptions = new CmdOptions();
	public CmdSetMOTD cmdSetMOTD = new CmdSetMOTD();
	public CmdSetAlarmTime cmdSetAlarmTime = new CmdSetAlarmTime();
	public CmdSetWallCheck cmdSetWallCheck = new CmdSetWallCheck();
	public CmdAlarm cmdAlarm = new CmdAlarm();
	public CmdCheck cmdCheck = new CmdCheck();
	public CmdMute cmdMute = new CmdMute();
	public CmdUnMute cmdUnMute = new CmdUnMute();
	public CmdMuteList cmdMuteList = new CmdMuteList();
	public CmdHistory cmdHistory = new CmdHistory();
	public CmdCheckLogs cmdCheckLogs = new CmdCheckLogs();

	public FCmdRoot() {
		super();
		this.aliases.addAll(Conf.baseCommandAliases);
		this.aliases.removeAll(Collections.<String> singletonList(null));
		this.allowNoSlashAccess = Conf.allowNoSlashCommand;

		// this.requiredArgs.add("");
		// this.optionalArgs.put("","")

		senderMustBePlayer = false;
		senderMustBeMember = false;
		senderMustBeModerator = false;
		senderMustBeCoLeader = false;
		senderMustBeLeader = false;

		this.disableOnLock = false;

		this.setHelpShort("The faction base command");
		this.helpLong.add(p.txt.parseTags("<i>This command contains all faction stuff."));

		// this.subCommands.add(p.cmdHelp);
	
		this.addSubCommand(this.cmdCheckLogs);
		this.addSubCommand(this.cmdHistory);
		this.addSubCommand(this.cmdMuteList);
		this.addSubCommand(this.cmdMute);
		this.addSubCommand(this.cmdUnMute);
		this.addSubCommand(this.cmdAlarm);
		this.addSubCommand(this.cmdCheck);
		this.addSubCommand(this.cmdSetAlarmTime);
		this.addSubCommand(this.cmdSetWallCheck);
		this.addSubCommand(this.cmdSetMOTD);
		this.addSubCommand(this.cmdAdmin);
		this.addSubCommand(this.cmdAutoClaim);
		this.addSubCommand(this.cmdBoom);
		this.addSubCommand(this.cmdBypass);
		this.addSubCommand(this.cmdChat);
		this.addSubCommand(this.cmdToggleAllianceChat);
		this.addSubCommand(this.cmdChatSpy);
		this.addSubCommand(this.cmdClaim);
		this.addSubCommand(this.cmdConfig);
		this.addSubCommand(this.cmdCreate);
		this.addSubCommand(this.cmdChest);
		this.addSubCommand(this.cmdTNTFill);
		this.addSubCommand(this.cmdDepositTNT);
		this.addSubCommand(this.cmdTNTCount);
		this.addSubCommand(this.cmdChestLogs);
		this.addSubCommand(this.cmdDeinvite);
		this.addSubCommand(this.cmdDescription);
		this.addSubCommand(this.cmdDisband);
		this.addSubCommand(this.cmdHelp);
		this.addSubCommand(this.cmdHome);
		this.addSubCommand(this.cmdInvite);
		this.addSubCommand(this.cmdInspect);
		this.addSubCommand(this.cmdJoin);
		this.addSubCommand(this.cmdKick);
		this.addSubCommand(this.cmdUpgrade);
		this.addSubCommand(this.cmdValue);
		this.addSubCommand(this.cmdLeave);
		this.addSubCommand(this.cmdList);
		this.addSubCommand(this.cmdLock);
		this.addSubCommand(this.cmdMap);
		this.addSubCommand(this.cmdMod);
		this.addSubCommand(this.cmdCoLeader);
		this.addSubCommand(this.cmdOpen);
		this.addSubCommand(this.cmdOwner);
		this.addSubCommand(this.cmdOwnerList);
		this.addSubCommand(this.cmdPeaceful);
		this.addSubCommand(this.cmdPermanent);
		this.addSubCommand(this.cmdPermanentPower);
		this.addSubCommand(this.cmdPower);
		this.addSubCommand(this.cmdPowerBoost);
		this.addSubCommand(this.cmdRelationAlly);
		this.addSubCommand(this.cmdRelationEnemy);
		this.addSubCommand(this.cmdRelationNeutral);
		this.addSubCommand(this.cmdRelationTruce);
		this.addSubCommand(this.cmdReload);
		this.addSubCommand(this.cmdSafeunclaimall);
		this.addSubCommand(this.cmdSaveAll);
		this.addSubCommand(this.cmdSethome);
		this.addSubCommand(this.cmdShow);
		this.addSubCommand(this.cmdStealth);
		this.addSubCommand(this.cmdStatus);
		this.addSubCommand(this.cmdStuck);
		this.addSubCommand(this.cmdTag);
		this.addSubCommand(this.cmdOptions);
		this.addSubCommand(this.cmdTitle);
		this.addSubCommand(this.cmdUnclaim);
		this.addSubCommand(this.cmdUnclaimall);
		this.addSubCommand(this.cmdVersion);
		this.addSubCommand(this.cmdWarunclaimall);
		this.addSubCommand(this.cmdSB);
		this.addSubCommand(this.cmdShowInvites);
		this.addSubCommand(this.cmdAnnounce);
		this.addSubCommand(this.cmdSeeChunk);
		this.addSubCommand(this.cmdConvert);
		this.addSubCommand(this.cmdFWarp);
		this.addSubCommand(this.cmdMissions);
		this.addSubCommand(this.cmdSetFWarp);
		this.addSubCommand(this.cmdDelFWarp);
		this.addSubCommand(this.cmdModifyPower);
		this.addSubCommand(this.cmdLogins);
		this.addSubCommand(this.cmdClaimLine);
		this.addSubCommand(this.cmdAHome);
		this.addSubCommand(this.cmdPerm);
		this.addSubCommand(this.cmdPromote);
		this.addSubCommand(this.cmdDemote);
		this.addSubCommand(this.cmdSetDefaultRole);
		this.addSubCommand(this.cmdMapHeight);
		this.addSubCommand(this.cmdClaimAt);
		this.addSubCommand(this.cmdban);
		this.addSubCommand(this.cmdUnban);
		this.addSubCommand(this.cmdbanlist);
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("FactionsTop")) {
			P.p.log(Level.INFO, "Found FactionsTop plugin. Disabling our own /f top command.");
		} else {
			this.addSubCommand(this.cmdTop);
		}

		if (P.p.getConfig().getBoolean("enable-faction-flight", false)) {
			this.addSubCommand(this.cmdFly);
			P.p.log(Level.INFO, "Enabling /f fly command");
		} else {
			P.p.log(Level.WARNING, "Faction flight set to false in config.yml. Not enabling /f fly command.");
		}

	}

	@Override
	public void perform() {
		this.commandChain.add(this);
		this.cmdHelp.execute(this.sender, this.args, this.commandChain);
	}

	@Override
	public TL getUsageTranslation() {
		return TL.GENERIC_PLACEHOLDER;
	}

}
