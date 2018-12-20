package com.ordpus.character.enums.skills;

import java.util.HashMap;
import java.util.Map;

import com.ordpus.util.helper.Localisation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SkillSet {

	public static Localisation loc = null;
	@Getter
	protected String prefix = "";
	@Getter
	protected double DEFAULT = 1;

	public static Map<String, Skill> skills = new HashMap<>();
	public static Map<String, SkillSet> skillSets = new HashMap<>();

	public static Skill newSkill(String id, double value, int age, boolean preset, long registerId) {
		return new Skill(id, value, age, preset, registerId, loc, skills);
	}

	@Override
	public String toString() {
		return prefix;
	}

}
