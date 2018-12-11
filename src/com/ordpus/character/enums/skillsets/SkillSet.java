package com.ordpus.character.enums.skillsets;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ordpus.character.enums.skillsets.skills.Skill;
import com.ordpus.util.helper.Localisation;

import lombok.var;

public class SkillSet {

	public static Localisation loc = new Localisation(new File("resources\\lang\\skills\\zh_CN.lang"));

	public static void main(String[] args) {
		var f = new File("resources\\lang\\skills\\zh_CN.lang");
		try {
			Map<String, String> a = new HashMap<String, String>();
			a.put("accounting", "会计");
			a.put("anthropology", "人类学");
			a.put("appraise", "估价");
			a.put("art&craft.acting", "技艺:表演");
			a.put("art&craft.acting", "技艺:表演");
			a.put("valuation", "估价");
			a.put("listening", "聆听");
			var out = new BufferedOutputStream(new FileOutputStream(f));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Skill> map = new HashMap<String, Skill>();
	public static Skill brawling = newSkill("fighting.brawling"), dodge = newSkill("dodge"), spot_hidden = newSkill("spot_hidden", 25),
			stealth = newSkill("stealth", 20);

	public static Skill newSkill(String id, double value) {
		return new Skill(id, value, SkillSet.loc, SkillSet.map);
	}

	public static Skill newSkill(String id) {
		return newSkill(id, 1);
	}
}
