package com.ordpus.character.enums.skills;

import java.util.Map;

import com.ordpus.main.Def;
import com.ordpus.util.helper.Localisation;

public class Skill extends Def {

	public double value;
	public final boolean preset = true;
	public long registerID = 0;
	public int age = 0;

	public Skill(String id, double value, int age, boolean preset, long registerID, Localisation loc, Map<String, Skill> map) {
		super(id, loc);
		this.value = value;
		map.put(id, this);
		this.age = age;
		description = id + ".description";
		this.registerID = registerID;
	}

	public SkillSet getInSet() {
		return SkillSet.skillSets.get(getPrefix());
	}

	public String getPrefix() {
		int start = 0, end = id.length() - 1;
		for(int i = 0; i < id.length(); ++i) {
			if(id.charAt(i) == '.') {
				if(start == 0) start = i;
				else if(end == id.length() - 1) end = i;
			}
		}
		if(end == id.length() - 1) return "";
		return id.substring(start + 1, end);
	}

}
