package br.com.battlebits.ycommon.bukkit.accounts;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.event.account.update.PlayerChangeLeagueEvent;
import br.com.battlebits.ycommon.bukkit.event.account.update.PlayerChangeTagEvent;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.account.game.GameStatus;
import br.com.battlebits.ycommon.common.banmanager.history.BanHistory;
import br.com.battlebits.ycommon.common.enums.Liga;
import br.com.battlebits.ycommon.common.friends.Friend;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.friends.request.Request;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountConfiguration;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeAccount;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeLiga;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeTag;
import br.com.battlebits.ycommon.common.party.Party;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BukkitPlayer extends BattlePlayer {

	private UUID lastTellUUID;
	private ArrayList<Tag> tags;

	public BukkitPlayer() {
	}

	@Override
	public boolean setTag(Tag tag) {
		return setTag(tag, false);
	}

	public boolean setTag(Tag tag, boolean forcetag) {
		if (!tags.contains(tag) && !forcetag) {
			tag = getDefaultTag();
		}
		PlayerChangeTagEvent event = new PlayerChangeTagEvent(getBukkitPlayer(), getTag(), tag, forcetag);
		BukkitMain.getPlugin().getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			if (!forcetag)
				if (tag != getTag())
					BukkitMain.getPlugin().getClient().sendPacket(new CPacketChangeTag(getUuid(), tag));
			super.setTag(tag);
		}
		return !event.isCancelled();
	}

	public Tag getDefaultTag() {
		return tags.get(0);
	}

	@Override
	public void setFakeName(String fakeName) {
		super.setFakeName(fakeName);
	}

	@Override
	public void setFichas(int fichas) {
		super.setFichas(fichas);
		sendCPacketChangeAccount();
	}

	@Override
	public void setMoney(int money) {
		super.setMoney(money);
		sendCPacketChangeAccount();
	}

	@Override
	public void setXp(int xp) {
		super.setXp(xp);
		sendCPacketChangeAccount();
		boolean upLiga = false;
		if (getXp() >= getLiga().getMaxXp()) {
			upLiga = true;
			xp = getXp() - getLiga().getMaxXp();
		}
		if (upLiga) {
			setLiga(getLiga().getNextLiga());
			setXp(xp);
		}
	}

	@Override
	public void setLiga(Liga liga) {
		PlayerChangeLeagueEvent event = new PlayerChangeLeagueEvent(getBukkitPlayer(), this, getLiga(), liga);
		BukkitMain.getPlugin().getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			super.setLiga(liga);
			BukkitMain.getPlugin().getClient().sendPacket(new CPacketChangeLiga(getUuid(), getLiga()));
		}
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
	public void setClan(String clanName) {
		super.setClan(clanName);
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
	public void setLanguage(Language language) {
		super.setLanguage(language);
	}

	@Override
	public void updateGameStatus(GameStatus gameStatus) {
		super.updateGameStatus(gameStatus);
	}

	@Override
	public void updateBanHistory(BanHistory banHistory) {
		super.updateBanHistory(banHistory);
	}

	@Override
	public void sendMessage(String tagId, String translateId, Map<String, String> replaces) {
		if(getBukkitPlayer() == null)
			return;
		String tag = tagId != null ? Translate.getTranslation(getLanguage(), tagId) + " " : "";
		String message = Translate.getTranslation(getLanguage(), translateId);
		if (replaces != null)
			for (Entry<String, String> entry : replaces.entrySet()) {
				message = message.replace(entry.getKey(), entry.getValue());
			}
		getBukkitPlayer().sendMessage(tag + message);
	}

	public void injectBukkitClass() {
		super.setConfiguration(new BukkitConfiguration(this));
		super.setGameStatus(new BukkitGameStatus(this));
	}

	public void loadTags() {
		tags = new ArrayList<>();
		for (Tag t : Tag.values()) {
			if (t == Tag.TORNEIO)
				if (getTorneio() != null && getTorneio() == BattlebitsAPI.getDefaultTorneio()) {
					tags.add(t);
					continue;
				}
			if ((t.isExclusive() && (t.getGroupToUse() == getServerGroup() || getServerGroup().ordinal() >= Group.ADMIN.ordinal())) || (!t.isExclusive() && getServerGroup().ordinal() >= t.getGroupToUse().ordinal())) {
				tags.add(t);
			}
		}
	}

	public void updateConfiguration() {
		BukkitMain.getPlugin().getClient().sendPacket(new CPacketAccountConfiguration(getUuid(), getConfiguration()));
	}

	public void sendCPacketChangeAccount() {
		BukkitMain.getPlugin().getClient().sendPacket(new CPacketChangeAccount(this));
	}

	public UUID getLastTellUUID() {
		return lastTellUUID;
	}

	public void setLastTellUUID(UUID lastTellUUID) {
		this.lastTellUUID = lastTellUUID;
	}

	public boolean hasLastTell() {
		return this.lastTellUUID != null;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public Player getBukkitPlayer() {
		return Bukkit.getPlayer(getUuid());
	}

}
