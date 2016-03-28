package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketBanPlayer extends CommonPacket {

	private String banJson;
	
	public CPacketBanPlayer() {
	}
	
	public CPacketBanPlayer(String banJson) {
		this.banJson = banJson;
	}
	
	public String getBanJson() {
		return banJson;
	}
	
	@Override
	public void read(DataInputStream in) throws Exception {
		this.banJson = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(banJson);
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleBanPlayer(this);
	}

}
