package com.ordpus.character.enums.skillsets.skills;

import java.util.Map;

import com.ordpus.util.helper.Localisation;

public class ArtAndCraftSkill extends Skill {

	public static final double DEFAULT = 5;

	public ArtAndCraftSkill(String id, double value, Localisation loc, Map<String, Skill> map) {
		super("art&craft" + id, value, loc, map);
	}

	public ArtAndCraftSkill(String id, Localisation loc, Map<String, Skill> map) {
		super("art&craft" + id, DEFAULT, loc, map);
	}

}
