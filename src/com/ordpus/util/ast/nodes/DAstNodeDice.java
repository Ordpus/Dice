package com.ordpus.util.ast.nodes;

import com.ordpus.util.ast.DAst;
import com.ordpus.util.info.DiceWrapper;

public class DAstNodeDice extends DAstNode {

	public DiceWrapper dice;

	public DAstNodeDice(int count, double size) {
		dice = new DiceWrapper(count, size);
	}

	public DAstNodeDice(DiceWrapper wrapper) {
		dice = new DiceWrapper(wrapper.count, wrapper.size);
	}

	public DAstNodeDice(String str) {
		dice = DAst.parseDice(str);
	}

	@Override
	protected String element() {
		return "dice:" + dice;
	}

}
