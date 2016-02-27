package br.com.battlebits.ycommon.bukkit.accounts;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.tagmanager.Tag;
import br.com.battlebits.ycommon.bukkit.tagmanager.TagManager;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.account.battlecraft.BattlecraftStatus;
import br.com.battlebits.ycommon.common.account.game.GameStatus;
import br.com.battlebits.ycommon.common.account.hungergames.HGStatus;
import br.com.battlebits.ycommon.common.banmanager.history.BanHistory;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.enums.Liga;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.friends.Friend;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.friends.request.Request;
import br.com.battlebits.ycommon.common.party.Party;
import br.com.battlebits.ycommon.common.payment.constructors.Expire;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BukkitPlayer extends BattlePlayer {

	private Tag tag;

	public BukkitPlayer(String userName, UUID uuid, String fakeName, int fichas, int money, int xp, Liga liga, InetSocketAddress ipAddress, String lastIpAddress, InetSocketAddress hostname, long onlineTime, long lastLoggedIn, long firstTimePlaying, boolean ignoreAll, Map<ServerType, Group> groups, Map<Group, Expire> ranks, Map<UUID, Friend> friends, Map<UUID, Request> friendRequests, Map<UUID, Blocked> blockedPlayers, Clan actualClan, Party actualParty, String skype, boolean skypeFriendOnly, String twitter, String youtubeChannel, String steam, String countryCode, Language language, HGStatus hungerGamesStatus, BattlecraftStatus battlecraftStatus, GameStatus gameStatus, BanHistory banHistory, List<String> nameHistory) {
		super(userName, uuid, fakeName, fichas, money, xp, liga, ipAddress, lastIpAddress, hostname, onlineTime, lastLoggedIn, firstTimePlaying, ignoreAll, groups, ranks, friends, friendRequests, blockedPlayers, actualClan, actualParty, skype, skypeFriendOnly, twitter, youtubeChannel, steam, countryCode, language, hungerGamesStatus, battlecraftStatus, gameStatus, banHistory, nameHistory);
		tag = TagManager.getPlayerDefaultTag(this);
	}

	public Group getServerGroup() {
		Group group = Group.NORMAL;
		if (!getGroups().isEmpty()) {
			if (getGroups().containsKey(BukkitMain.getServerType())) {
				group = getGroups().get(BukkitMain.getServerType());
			} else if (getGroups().containsKey(ServerType.NETWORK)) {
				group = getGroups().get(ServerType.NETWORK);
			} else {
				group = Group.ULTIMATE;
			}
		} else if (!getRanks().isEmpty()) {
			Collection<Expire> expires = getRanks().values();
			Expire expire = null;
			for (Expire expireRank : expires) {
				if (expireRank == null) {
					expire = expireRank;
				} else if (expireRank.getRankType().ordinal() > expire.getRankType().ordinal()) {
					expire = expireRank;
				}
			}
			if (expire != null)
				group = Group.valueOf(expire.getRankType().name());
		}
		return group;
	}

	public Tag getPrefix() {
		return tag;
	}

	@Override
	public void setFakeName(String fakeName) {
		super.setFakeName(fakeName);
	}

	@Override
	public void setFichas(int fichas) {
		super.setFichas(fichas);
	}

	@Override
	public void setMoney(int money) {
		super.setMoney(money);
	}

	@Override
	public void setXp(int xp) {
		super.setXp(xp);
	}

	@Override
	public void updateGroup(Map<ServerType, Group> groups) {
		super.updateGroup(groups);
	}

	@Override
	public void updateRanks(Map<Group, Expire> ranks) {
		super.updateRanks(ranks);
	}

	@Override
	public void updateFriends(Map<UUID, Friend> friends) {
		super.updateFriends(friends);
	}

	@Override
	public void updateFriendRequests(Map<UUID, Request> friendRequests) {
		super.updateFriendRequests(friendRequests);
	}

	@Override
	public void updateBlockedPlayers(Map<UUID, Blocked> blockedPlayers) {
		super.updateBlockedPlayers(blockedPlayers);
	}

	@Override
	public void setActualClan(Clan actualClan) {
		super.setActualClan(actualClan);
	}

	@Override
	public void setActualParty(Party actualParty) {
		super.setActualParty(actualParty);
	}

	@Override
	public void setSkype(String skype) {
		super.setSkype(skype);
	}

	@Override
	public void setSkypeFriendOnly(boolean skypeFriendOnly) {
		super.setSkypeFriendOnly(skypeFriendOnly);
	}

	@Override
	public void setTwitter(String twitter) {
		super.setTwitter(twitter);
	}

	@Override
	public void setYoutubeChannel(String youtubeChannel) {
		super.setYoutubeChannel(youtubeChannel);
	}

	@Override
	public void setSteam(String steam) {
		super.setSteam(steam);
	}

	@Override
	public void setIgnoreAll(boolean ignoreAll) {
		super.setIgnoreAll(ignoreAll);
	}

	@Override
	public void setLanguage(Language language) {
		super.setLanguage(language);
	}

	@Override
	public void updateHungerGamesStatus(HGStatus hungerGamesStatus) {
		super.updateHungerGamesStatus(hungerGamesStatus);
	}

	@Override
	public void updateBattlecraftStatus(BattlecraftStatus battlecraftStatus) {
		super.updateBattlecraftStatus(battlecraftStatus);
	}

	@Override
	public void updateGameStatus(GameStatus gameStatus) {
		super.updateGameStatus(gameStatus);
	}

	@Override
	public void updateBanHistory(BanHistory banHistory) {
		super.updateBanHistory(banHistory);
	}

}