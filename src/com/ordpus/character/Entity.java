package com.ordpus.character;

import com.ordpus.character.enums.Race;

public class Entity {

	public Entity(String name) {
		this.name = name;
	}

	public String name;
	public Race race;
	public int age, gender;
	public double HP, MP, STR, CON, SIZ, DEX, APP, INT, POW, EDU, MOV;

}
