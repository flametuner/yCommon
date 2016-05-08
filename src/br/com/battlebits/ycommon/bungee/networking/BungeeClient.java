package br.com.battlebits.ycommon.bungee.networking;

import java.net.Socket;

import br.com.battlebits.ycommon.bungee.networking.client.CommonClient;

public class BungeeClient extends CommonClient {

	public BungeeClient(Socket socket) throws Exception {
		super(socket);
		setPacketHandler(new BungeePacketHandler(this));
	}

}
