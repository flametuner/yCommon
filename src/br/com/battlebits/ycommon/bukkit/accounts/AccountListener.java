package br.com.battlebits.ycommon.bukkit.accounts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class AccountListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsync(AsyncPlayerPreLoginEvent event) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 57966);
		BattlebitsAPI.debug("SOCKET > CONNECT");
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		outputStream.writeUTF("Account");
		outputStream.writeUTF(event.getUniqueId().toString());
		outputStream.flush();
		String command = inputStream.readUTF();
		if (command.equals("Account")) {
			String json = inputStream.readUTF();
			BukkitPlayer battlePlayer = BukkitMain.getGson().fromJson(json, BukkitPlayer.class);
			BattlebitsAPI.getAccountCommon().loadBattlePlayer(event.getUniqueId(), battlePlayer);
			BattlebitsAPI.debug("NEW BATTLEPLAYER > " + battlePlayer.getUserName() + " (" + event.getUniqueId() + ")");
			battlePlayer = null;
			json = null;
		}
		BattlebitsAPI.debug("SOCKET > CLOSE");
		outputStream.close();
		inputStream.close();
		socket.close();
		command = null;
		outputStream = null;
		inputStream = null;
		socket = null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onRemoveAccount(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != Result.ALLOWED)
			BattlebitsAPI.getAccountCommon().unloadBattlePlayer(event.getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeave(PlayerQuitEvent event) {
		BattlebitsAPI.getAccountCommon().unloadBattlePlayer(event.getPlayer().getUniqueId());
	}
}