package com.ordpus.main;

import com.ordpus.util.helper.Localisation;

public class Def {

	public String id;
	public String name;

	public Def(String id, Localisation loc) {
		this.id = id;
		name = loc.local(id);
	}

}
