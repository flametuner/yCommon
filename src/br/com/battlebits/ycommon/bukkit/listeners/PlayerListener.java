package br.com.battlebits.ycommon.bukkit.listeners;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.api.vanish.VanishAPI;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent.UpdateType;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketKeepAlive;

public class PlayerListener implements Listener {

	// [CLAN] RANK Nick (LigaSymbol) >>
	// [TEMPO] DONO GustavoInacio (*) >>

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPreProcessCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().toLowerCase().startsWith("/me ")) {
			event.getPlayer().sendMessage(ChatColor.RED + "Voce nao pode utilizar o comando 'me'");
			event.setCancelled(true);
		}
		if (event.getMessage().split(" ")[0].contains(":")) {
			event.getPlayer().sendMessage(ChatColor.RED + "Voce nao pode enviar comando que possuem ':' (dois pontos)");
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		VanishAPI.getInstance().updateVanishToPlayer(event.getPlayer());
	}

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		Iterator<BattlePlayer> players = BattlebitsAPI.getAccountCommon().getPlayers().iterator();
		while (players.hasNext()) {
			BattlePlayer player = players.next();
			if (Bukkit.getPlayer(player.getUuid()) == null) {
				if (player.isCacheExpired()) {
					players.remove();
					BattlebitsAPI.debug("REMOVENDO BATTLEPLAYER " + player.getUserName() + " DO CACHE");
				}
			}
		}
	}

	@EventHandler
	public void onKeepAlive(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		if (BukkitMain.getPlugin().getClient().keepAlive >= 30) {
			BukkitMain.getPlugin().getClient().sendPacket(new CPacketKeepAlive());
		}
		--BukkitMain.getPlugin().getClient().keepAlive;
	}

}
