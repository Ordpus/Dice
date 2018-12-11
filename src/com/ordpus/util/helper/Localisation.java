package com.ordpus.util.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.var;

public class Localisation {

	private Map<String, String> localMap = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	public Localisation(File... files) {
		var gson = new Gson();
		for(File file : files) {
			try {
				localMap.putAll((Map<? extends String, ? extends String>) gson.fromJson(new FileReader(file), new TypeToken<Map<String, String>>() {}.getType()));
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public String local(String code) {
		String result = localMap.get(code);
		return result == null ? code : result;
	}

}
