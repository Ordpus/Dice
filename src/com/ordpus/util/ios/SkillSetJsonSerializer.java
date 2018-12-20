package com.ordpus.util.ios;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ordpus.character.enums.skills.SkillSet;
import com.ordpus.util.helper.MathUtil;

public class SkillSetJsonSerializer implements JsonDeserializer<SkillSet>, JsonSerializer<SkillSet> {

	@Override
	public JsonElement serialize(SkillSet elem, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.addProperty("prefix", elem.getPrefix());
		if(!MathUtil.isInt(elem.getDEFAULT(), 1)) result.addProperty("default", elem.getDEFAULT());
		return result;
	}

	@Override
	public SkillSet deserialize(JsonElement elem, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = (JsonObject) elem;
		elem = obj.get("default");
		double def = elem == null ? 1 : elem.getAsDouble();
		String prefix = obj.get("prefix").getAsString();
		SkillSet result = new SkillSet(prefix, def);
		SkillSet.skillSets.put(prefix, result);
		return result;
	}

}
