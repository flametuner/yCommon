package br.com.battlebits.ycommon.bungee.managers;

import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import br.com.battlebits.ycommon.common.utils.DateUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanManager {
	private Cache<InetSocketAddress, Ban> banCache;

	public BanManager() {
		banCache = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.MINUTES).build(new CacheLoader<InetSocketAddress, Ban>() {
			@Override
			public Ban load(InetSocketAddress name) throws Exception {
				return null;
			}
		});
	}

	public void ban(BattlePlayer player, Ban ban) {
		player.getBanHistory().getBanHistory().add(ban);
		for (ProxiedPlayer proxiedP : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer pl = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedP.getUniqueId());
			String banSuccess = "";
			if (ban.isPermanent()) {
				banSuccess = Translate.getTranslation(pl.getLanguage(), "ban-prefix") + " " + Translate.getTranslation(pl.getLanguage(), "ban-perm-success");
			} else {
				banSuccess = Translate.getTranslation(pl.getLanguage(), "ban-prefix") + " " + Translate.getTranslation(pl.getLanguage(), "ban-temp-success");
			}
			banSuccess = banSuccess.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
			banSuccess = banSuccess.replace("%banned-By%", ban.getBannedBy());
			banSuccess = banSuccess.replace("%reason%", ban.getReason());
			banSuccess = banSuccess.replace("%duration%", DateUtils.formatDifference(pl.getLanguage(), ban.getDuration() / 1000));
			if (pl.hasGroupPermission(Group.TRIAL)) {
				proxiedP.sendMessage(TextComponent.fromLegacyText(banSuccess));
			}
		}
		if (!player.isOnline())
			BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
		ProxiedPlayer pPlayer = BungeeMain.getPlugin().getProxy().getPlayer(player.getUuid());
		if (pPlayer != null)
			pPlayer.disconnect(getBanKickMessageBase(ban, player.getLanguage()));
		if (player.getIpAddress() != null)
			banCache.put(player.getIpAddress(), ban);
	}

	public void unban(BattlePlayer bannedByPlayer, BattlePlayer player, Ban ban) {
		if (bannedByPlayer != null)
			ban.unban(bannedByPlayer);
		else
			ban.unban();
		for (ProxiedPlayer proxiedP : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer pl = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedP.getUniqueId());
			String unbanSuccess = Translate.getTranslation(pl.getLanguage(), "unban-prefix") + " " + Translate.getTranslation(pl.getLanguage(), "unban-success");
			unbanSuccess = unbanSuccess.replace("%player%", player.getUserName() + "(" + player.getUuid().toString().replace("-", "") + ")");
			if (pl.hasGroupPermission(Group.TRIAL)) {
				proxiedP.sendMessage(TextComponent.fromLegacyText(unbanSuccess));
			}
		}
		BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
	}

	public Ban getIpBan(InetSocketAddress address) {
		return banCache.asMap().get(address);
	}

	public static String getBanKickMessage(Ban ban, Language lang) {
		String reason = "";
		if (ban.isPermanent()) {
			reason = Translate.getTranslation(lang, "banned-permanent");
		} else {
			reason = Translate.getTranslation(lang, "banned-temp");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ban.getBanTime());
		reason = reason.replace("%day%", calendar.getTime().toString());
		reason = reason.replace("%banned-By%", ban.getBannedBy());
		reason = reason.replace("%reason%", ban.getReason());
		reason = reason.replace("%duration%", DateUtils.formatDifference(lang, (ban.getExpire() - System.currentTimeMillis()) / 1000));
		reason = reason.replace("%forum%", BattlebitsAPI.FORUM_WEBSITE);
		reason = reason.replace("%store%", BattlebitsAPI.STORE);
		return reason;
	}

	public static BaseComponent[] getBanKickMessageBase(Ban ban, Language lang) {
		return TextComponent.fromLegacyText(getBanKickMessage(ban, lang));
	}
}
