package br.com.battlebits.ycommon.bukkit.event.account.update;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.event.PlayerCancellableEvent;
import br.com.battlebits.ycommon.bukkit.tagmanager.Tag;

public class PlayerSwitchTagEvent extends PlayerCancellableEvent {

	private Tag oldTag;
	private Tag newTag;

	public PlayerSwitchTagEvent(Player p, Tag oldTag, Tag newTag) {
		super(p);
		this.oldTag = oldTag;
		this.newTag = newTag;
	}

	public Tag getNewTag() {
		return newTag;
	}

	public Tag getOldTag() {
		return oldTag;
	}

}
