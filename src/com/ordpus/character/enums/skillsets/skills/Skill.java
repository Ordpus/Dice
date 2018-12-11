package com.ordpus.character.enums.skillsets.skills;

import java.util.Map;

import com.ordpus.main.Def;
import com.ordpus.util.helper.Localisation;

public class Skill extends Def {

	public double value;

	public Skill(String id, Localisation loc, Map<String, Skill> map) {
		super("skill." + id, loc);
		value = 1;
		map.put(name, this);
	}

	public Skill(String id, double value, Localisation loc, Map<String, Skill> map) {
		super("skill." + id, loc);
		this.value = value;
		map.put(name, this);
	}

}
