package br.com.battlebits.ycommon.common.permissions.enums;

import br.com.battlebits.ycommon.common.permissions.groups.GroupInterface;
import br.com.battlebits.ycommon.common.permissions.groups.ModeratorGroup;
import br.com.battlebits.ycommon.common.permissions.groups.OwnerGroup;
import br.com.battlebits.ycommon.common.permissions.groups.SimpleGroup;
import br.com.battlebits.ycommon.common.permissions.groups.StreamerGroup;

public enum Group {

	NORMAL, //
	LIGHT, //
	PREMIUM, //
	ULTIMATE, //
	YOUTUBER, //
	BUILDER, //
	STAFF, //
	HELPER, //
	DEV, //
	YOUTUBERPLUS(new StreamerGroup()), //
	TRIAL(new ModeratorGroup()), //
	MOD(new ModeratorGroup()), //
	MODPLUS(new StreamerGroup()), //
	MANAGER(new StreamerGroup()), //
	ADMIN(new StreamerGroup()), //
	DONO(new OwnerGroup());

	private GroupInterface group;

	private Group() {
		this(new SimpleGroup());
	}

	private Group(GroupInterface group) {
		this.group = group;
	}

	public GroupInterface getGroup() {
		return group;
	}

}
