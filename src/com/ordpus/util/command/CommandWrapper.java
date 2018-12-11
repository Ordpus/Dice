package com.ordpus.util.command;

import java.util.HashMap;
import java.util.Map;

import lombok.ToString;

@ToString
public class CommandWrapper {

	public String type;
	public String content;
	public Map<String, Object> args;

	public CommandWrapper(String type, String content) {
		this.type = type;
		this.content = content;
	}

	public CommandWrapper add(String name, Object obj) {
		if(args == null) args = new HashMap<>();
		args.put(name, obj);
		return this;
	}

	public Object get(String name) {
		if(args == null) return null;
		return args.get(name);
	}

}
