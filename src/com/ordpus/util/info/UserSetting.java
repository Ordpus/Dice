package com.ordpus.util.info;

import java.util.HashMap;
import java.util.Map;

import com.ordpus.Dice;
import com.ordpus.character.Entity;

public class UserSetting {

	public long user;

	public DiceWrapper defaultDice = new DiceWrapper(1, 100);

	private Map<Long, Entity> defaultGroupCharacter = new HashMap<>();

	public boolean simple = false;

	public Entity getCharacter(long id) {
		Entity e = defaultGroupCharacter.get(id);
		if(e == null) {
			e = new Entity(Dice.CQ.getGroupMemberInfo(id, user).getCard());
			defaultGroupCharacter.put(id, e);
		}
		return e;
	}

}
