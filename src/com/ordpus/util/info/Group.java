package com.ordpus.util.info;

import java.util.Map;

public class Group {

	public static Map<Long, Group> group;

	public long id;

	public GroupSetting setting = new GroupSetting();

	public Group() {}

	private Group(long id) {
		this.id = id;
		group.put(id, this);
	}

	public static Group of(long id) {
		if(!group.containsKey(id)) return new Group(id);
		return group.get(id);
	}

}
