package com.ordpus.util.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

	public static Map<Long, User> user;

	public long id;

	public Map<String, Character> characters = new HashMap<>();

	public List<Long> kps = new ArrayList<>();

	public UserSetting setting = new UserSetting();

	public User() {}

	private User(long id) {
		this.id = id;
		user.put(id, this);
	}

	public static User of(long id) {
		if(!user.containsKey(id)) {
			User result = new User(id);
			result.setting.user = id;
			return result;
		}
		return user.get(id);
	}

}
