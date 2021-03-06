package br.com.battlebits.ycommon.common.banmanager.constructors;

import java.util.UUID;

import br.com.battlebits.ycommon.common.account.BattlePlayer;

public class Mute {

	private String mutedBy;
	private String mutedIp;

	private String server;

	private UUID mutedByUUID;
	private long muteTime;
	private String reason;

	private boolean unmuted;
	private String unmutedBy;
	private UUID unmutedByUUID;
	private long unmuteTime;

	private long expire;
	private long duration;

	public Mute(String mutedBy, String mutedIp, String server, String reason, long duration) {
		this(mutedBy, null, mutedIp, server, reason, duration);
	}

	public Mute(String mutedBy, UUID mutedByUuid, String mutedIp, String server, String reason, long expire) {
		this(mutedBy, mutedIp, server, mutedByUuid, System.currentTimeMillis(), reason, false, null, null, -1, expire, expire - System.currentTimeMillis());
	}

	public Mute(String mutedBy, String mutedIp, String server, String reason) {
		this(mutedBy, null, mutedIp, server, reason);
	}

	public Mute(String mutedBy, UUID mutedByUuid, String mutedIp, String server, String reason) {
		this(mutedBy, mutedIp, server, mutedByUuid, System.currentTimeMillis(), reason, false, null, null, -1, -1, -1);
	}

	public Mute(String mutedBy, String mutedIp, String server, UUID mutedByUUID, long muteTime, String reason, boolean unmuted, String unmutedBy, UUID unmutedByUUID, long unmuteTime, long expire, long duration) {
		this.mutedBy = mutedBy;
		this.mutedIp = mutedIp;
		this.mutedByUUID = mutedByUUID;
		this.muteTime = muteTime;
		this.reason = reason;
		this.server = server;
		this.unmuted = unmuted;
		this.unmutedBy = unmutedBy;
		this.unmutedByUUID = unmutedByUUID;
		this.unmuteTime = unmuteTime;
		this.expire = expire;
		this.duration = duration;
	}

	public String getMutedBy() {
		return mutedBy;
	}

	public String getMutedIp() {
		return mutedIp;
	}

	public UUID getMutedByUUID() {
		return mutedByUUID;
	}

	public long getMuteTime() {
		return muteTime;
	}

	public String getReason() {
		return reason;
	}

	public String getServer() {
		return server;
	}

	public boolean isUnmuted() {
		return unmuted;
	}

	public String getUnmutedBy() {
		return unmutedBy;
	}

	public UUID getUnmutedByUUID() {
		return unmutedByUUID;
	}

	public long getUnmuteTime() {
		return unmuteTime;
	}

	public long getExpire() {
		return expire;
	}

	public long getDuration() {
		return duration;
	}

	public boolean hasExpired() {
		return expire != -1 && expire < System.currentTimeMillis();
	}

	public boolean isPermanent() {
		return expire == -1;
	}

	public void unmute() {
		this.unmuted = true;
		this.unmutedBy = "CONSOLE";
		this.unmuteTime = System.currentTimeMillis();
	}

	public void unmute(BattlePlayer unmutePlayer) {
		unmute(unmutePlayer.getUuid(), unmutePlayer.getUserName());
	}

	public void unmute(UUID unmuteUuid, String unmuteName) {
		this.unmuted = true;
		this.unmutedBy = unmuteName;
		this.unmutedByUUID = unmuteUuid;
		this.unmuteTime = System.currentTimeMillis();
	}

}
