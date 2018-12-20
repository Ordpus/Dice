package com.ordpus.character.enums.skillsets;

import com.ordpus.character.enums.skillsets.skills.ArtAndCraftSkill;

public class ArtAndCraftSkillSet extends SkillSet {

	public static ArtAndCraftSkill acting = newSkill("acting"), painting = newSkill("painting"), photography = newSkill("photography"),
			counterfeiting = newSkill("counterfeiting"), literature = newSkill("literature"), calligraphy = newSkill("calligraphy");

	public static ArtAndCraftSkill newSkill(String id, double value) {
		return new ArtAndCraftSkill(id, value, SkillSet.loc, SkillSet.map);
	}

	public static ArtAndCraftSkill newSkill(String id) {
		return new ArtAndCraftSkill(id, SkillSet.loc, SkillSet.map);
	}

}
