package com.ordpus.util.ios;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ordpus.character.enums.skills.Skill;
import com.ordpus.character.enums.skills.SkillSet;
import com.ordpus.util.helper.MathUtil;

public class SkillsJsonSerializer implements JsonDeserializer<Skill>, JsonSerializer<Skill> {

	@Override
	public Skill deserialize(JsonElement elem, Type type, JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject obj = (JsonObject) elem;
		String id = obj.get("id").getAsString();
		JsonElement val = obj.get("value");
		double value = val == null ? 1 : val.getAsDouble();
		boolean preset = obj.get("preset") == null;
		val = obj.get("register_id");
		long registerID = val == null ? 0 : val.getAsLong();
		val = obj.get("age");
		int age = val == null ? 0 : val.getAsInt();
		return SkillSet.newSkill(id, value, age, preset, registerID);
	}

	@Override
	public JsonElement serialize(Skill elem, Type type, JsonSerializationContext arg2) {
		JsonObject result = new JsonObject();
		result.addProperty("id", elem.id);
		if(!MathUtil.isInt(elem.value, 1)) result.addProperty("value", elem.value);
		if(!elem.preset) {
			result.addProperty("preset", elem.preset);
			result.addProperty("register_id", elem.registerID);
		}
		if(elem.age != 0) result.addProperty("age", elem.age);
		return result;
	}

}
