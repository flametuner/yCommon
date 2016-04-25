package br.com.battlebits.ycommon.bungee.commands.register;

import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.utils.DateUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanCommand extends CommandClass {

	@Command(name = "ban", usage = "/<command> <player> <reason>", aliases = { "banir" }, groupToUse = Group.TRIAL)
	public void ban(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		final Language language = lang;
		final String banPrefix = Translate.getTranslation(lang, "ban-prefix") + " ";
		if (args.length < 2) {
			sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(lang, "ban-usage")));
			return;
		}
		BungeeCord.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			public void run() {
				UUID uuid = BattlebitsAPI.getUUIDOf(args[0]);
				if (uuid == null) {
					sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "player-not-exist")));
					return;
				}
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
				if (player == null) {
					if (sender instanceof ProxiedPlayer) {
						if (BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getServerGroup().equals(Group.TRIAL)) {
							sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "trial-ban-offline")));
							return;
						}
					}
					try {
						player = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(uuid);
					} catch (Exception e) {
						e.printStackTrace();
						sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "cant-request-offline")));
						return;
					}
					if (player == null) {
						sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "player-never-joined")));
						return;
					}
				}
				Ban actualBan = player.getBanHistory().getActualBan();
				if (actualBan != null && !actualBan.isUnbanned() && actualBan.isPermanent()) {
					sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "already-banned")));
					return;
				}
				if (player.isStaff()) {
					Group group = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getServerGroup();
					if (group != Group.DONO && group != Group.ADMIN) {
						sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "ban-staff")));
						return;
					}
				}
				StringBuilder builder = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					String espaco = " ";
					if (i >= args.length - 1)
						espaco = "";
					builder.append(args[i] + espaco);
				}
				Ban ban = null;
				String playerIp = "";
				if (player.isOnline() && player.getIpAddress() != null && player.getIpAddress().getHostString() != null) {
					playerIp = player.getIpAddress().getHostString();
				} else {
					playerIp = "OFFLINE";
				}
				if (cmdArgs.isPlayer()) {
					ProxiedPlayer bannedBy = cmdArgs.getPlayer();
					ban = new Ban(uuid, bannedBy.getName(), bannedBy.getUniqueId(), playerIp, player.getServerConnected(), builder.toString());
					bannedBy = null;
				} else {
					ban = new Ban(uuid, "CONSOLE", playerIp, player.getServerConnected(), builder.toString());
				}
				BungeeMain.getPlugin().getBanManager().ban(player, ban);
			}
		});
	}

	@Command(name = "tempban", usage = "/<command> <player> <time> <reason>", aliases = { "tempbanir" }, groupToUse = Group.TRIAL)
	public void tempban(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		final Language language = lang;
		final String banPrefix = Translate.getTranslation(lang, "ban-prefix") + " ";
		if (args.length < 3) {
			sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(lang, "tempban-usage")));
			return;
		}
		BungeeCord.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			public void run() {
				UUID uuid = BattlebitsAPI.getUUIDOf(args[0]);
				if (uuid == null) {
					sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "player-not-exist")));
					return;
				}
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
				if (player == null) {
					if (sender instanceof ProxiedPlayer) {
						if (BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getServerGroup().equals(Group.TRIAL)) {
							sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "trial-ban-offline")));
							return;
						}
					}
					try {
						player = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(uuid);
					} catch (Exception e) {
						e.printStackTrace();
						sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "cant-request-offline")));
						return;
					}
					if (player == null) {
						sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "player-never-joined")));
						return;
					}
				}
				Ban actualBan = player.getBanHistory().getActualBan();
				if (actualBan != null && !actualBan.isUnbanned() && actualBan.isPermanent()) {
					sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "already-banned")));
					return;
				}
				if (player.isStaff()) {
					Group group = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getServerGroup();
					if (group != Group.DONO && group != Group.ADMIN) {
						sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "ban-staff")));
						return;
					}
				}
				long expiresCheck;
				try {
					expiresCheck = DateUtils.parseDateDiff(args[1], true);
				} catch (Exception e1) {
					sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "invalid-format")));
					return;
				}
				StringBuilder builder = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					String espaco = " ";
					if (i >= args.length - 1)
						espaco = "";
					builder.append(args[i] + espaco);
				}
				Ban ban = null;
				String playerIp = "";
				if (player.isOnline() && player.getIpAddress() != null && player.getIpAddress().getHostString() != null) {
					playerIp = player.getIpAddress().getHostString();
				} else {
					playerIp = "OFFLINE";
				}
				if (cmdArgs.isPlayer()) {
					ProxiedPlayer bannedBy = cmdArgs.getPlayer();
					ban = new Ban(uuid, bannedBy.getName(), bannedBy.getUniqueId(), playerIp, player.getServerConnected(), builder.toString(), expiresCheck);
					bannedBy = null;
				} else {
					ban = new Ban(uuid, "CONSOLE", playerIp, player.getServerConnected(), builder.toString(), expiresCheck);
				}
				BungeeMain.getPlugin().getBanManager().ban(player, ban);
			}
		});
	}

	@Command(name = "unban", usage = "/<command> <player>", aliases = { "desbanir" }, groupToUse = Group.MANAGER)
	public void unban(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		final Language language = lang;
		final String banPrefix = Translate.getTranslation(lang, "unban-prefix") + " ";
		if (args.length != 1) {
			sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(lang, "unban-usage")));
			return;
		}
		BungeeCord.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			public void run() {
				UUID uuid = BattlebitsAPI.getUUIDOf(args[0]);
				if (uuid == null) {
					sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "player-not-exist")));
					return;
				}
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid);
				if (player == null) {
					try {
						player = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(uuid);
					} catch (Exception e) {
						e.printStackTrace();
						sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "cant-request-offline")));
						return;
					}
					if (player == null) {
						sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "player-never-joined")));
						return;
					}
				}
				Ban actualBan = player.getBanHistory().getActualBan();
				if (actualBan == null) {
					sender.sendMessage(TextComponent.fromLegacyText(banPrefix + Translate.getTranslation(language, "player-not-banned")));
					return;
				}
				BattlePlayer unbannedBy = null;
				if (cmdArgs.isPlayer()) {
					unbannedBy = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId());
				}
				BungeeMain.getPlugin().getBanManager().unban(unbannedBy, player, actualBan);
			}
		});
	}

}
