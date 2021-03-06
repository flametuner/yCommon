package br.com.battlebits.ycommon.bukkit.tagmanager;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitCommon;
import br.com.battlebits.ycommon.bukkit.BukkitMain;

public class TagManager extends BukkitCommon {
	public TagManager(BukkitMain main) {
		super(main);
	}

	@Override
	public void onEnable() {
		registerListener(new TagListener(this));
	}

	public void removePlayerTag(Player p) {
		BukkitMain.getPlugin().getBattleBoard().leaveCurrentTeamForOnlinePlayers(p);
	}

	@Override
	public void onDisable() {
		for (Player player : getServer().getOnlinePlayers()) {
			removePlayerTag(player);
		}
	}
}
