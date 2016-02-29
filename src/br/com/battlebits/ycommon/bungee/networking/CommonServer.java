package br.com.battlebits.ycommon.bungee.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class CommonServer implements Runnable {

	private ServerSocket server;
	private static final int PORT = 57966;
	private static final String ADDRESS = "0.0.0.0";

	private static boolean RUNNING = false;

	public CommonServer() throws Exception {
		server = new ServerSocket();
		server.bind(new InetSocketAddress(ADDRESS, PORT));
		RUNNING = true;
		BattlebitsAPI.debug("SERVER SOCKET > LIGADO");
	}

	@Override
	public void run() {
		while (RUNNING) {
			try {
				Socket client = server.accept();

				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());

				String command = inputStream.readUTF();
				switch (command) {
				case "Account":
					String subComand = inputStream.readUTF();
					UUID uuid = UUID.fromString(inputStream.readUTF());
					switch (subComand) {
					case "Load":
						handleAccountRequest(uuid, outputStream);
						break;
					case "Update":
						break;
					default:
						break;
					}
					uuid = null;
					subComand = null;
					break;
				default:
					break;
				}
				BattlebitsAPI.debug("SOCKET > CLOSE");
				outputStream.close();
				inputStream.close();
				client.close();

				command = null;
				outputStream = null;
				inputStream = null;
				client = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			stopServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleAccountRequest(UUID uuid, DataOutputStream output) throws Exception {
		output.writeUTF("Account");
		String json = BungeeMain.getGson().toJson(BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid));
		output.writeUTF(json);
		output.flush();
	}

	public void stopServer() throws IOException {
		RUNNING = false;
		server.close();
		server = null;
	}

}
