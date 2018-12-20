package com.ordpus.main;

import com.ordpus.util.helper.Localisation;

public class Def {

	public String id;
	public String description;

	public Def(String id, Localisation loc) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id;
	}

}
