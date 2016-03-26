package br.com.battlebits.ycommon.bukkit.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.UUID;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bungee.networking.CommonServer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountRequest;

public class PacketSender {

	public static void sendPacket(CommonPacket packet) throws Exception {
		sendPacket(CommonServer.ADDRESS, CommonServer.PORT, packet);
	}

	public static void sendPacketReturn(CommonPacket packet, CommonHandler handler) throws Exception {
		sendPacketReturn(CommonServer.ADDRESS, CommonServer.PORT, packet, handler);
	}

	public static void sendPacketReturn(String hostName, int port, CommonPacket packet, CommonHandler handler) throws Exception {
		Socket socket = new Socket(hostName, port);
		BattlebitsAPI.debug("SOCKET>CONNECT");
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		outputStream.writeByte(packet.id());
		packet.write(outputStream);
		outputStream.flush();
		BattlebitsAPI.debug("SOCKET>OUT>" + packet.getClass().getSimpleName());
		byte ID = inputStream.readByte();
		CommonPacket PACKET = CommonPacket.get(ID);
		if (PACKET != null) {
			PACKET.read(inputStream);
			PACKET.handle(handler);
			BattlebitsAPI.debug("SOCKET>INP>" + PACKET.getClass().getSimpleName());
		}
		BattlebitsAPI.debug("SOCKET>MESSAGE RECEIVED");
		outputStream.close();
		inputStream.close();
		socket.close();
		BattlebitsAPI.debug("SOCKET>CLOSE");
		outputStream = null;
		inputStream = null;
		socket = null;
	}

	public static void sendPacket(String hostName, int port, CommonPacket packet) throws Exception {
		Socket socket = new Socket(hostName, port);
		BattlebitsAPI.debug("SOCKET>CONNECT");
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		outputStream.writeByte(packet.id());
		packet.write(outputStream);
		outputStream.flush();
		BattlebitsAPI.debug("SOCKET>OUT>" + packet.getClass().getSimpleName());
		outputStream.close();
		socket.close();
		BattlebitsAPI.debug("SOCKET>CLOSE");
		outputStream = null;
		socket = null;
	}

	public static BukkitPlayer getOfflinePlayer(UUID uuid) throws Exception {
		Socket socket = new Socket(CommonServer.ADDRESS, CommonServer.PORT);
		CommonPacket packet = new CPacketAccountRequest(uuid);
		BattlebitsAPI.debug("SOCKET>CONNECT");
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		outputStream.writeByte(packet.id());
		packet.write(outputStream);
		outputStream.flush();
		BattlebitsAPI.debug("SOCKET>OUT>" + packet.getClass().getSimpleName());
		byte ID = inputStream.readByte();
		CPacketAccountLoad PACKET = (CPacketAccountLoad) CommonPacket.get(ID);
		if (PACKET != null) {
			PACKET.read(inputStream);
			BattlebitsAPI.debug("SOCKET>INP>" + PACKET.getClass().getSimpleName());
		}
		BattlebitsAPI.debug("SOCKET>MESSAGE RECEIVED");
		outputStream.close();
		inputStream.close();
		socket.close();
		BattlebitsAPI.debug("SOCKET>CLOSE");
		outputStream = null;
		inputStream = null;
		socket = null;
		packet = null;
		return BukkitMain.getGson().fromJson(PACKET.getJson(), BukkitPlayer.class);
	}
}