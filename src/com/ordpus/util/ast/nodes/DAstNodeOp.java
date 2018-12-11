package com.ordpus.util.ast.nodes;

public class DAstNodeOp extends DAstNode {

	public char op;

	public DAstNodeOp(char op) {
		this.op = op;
	}

	@Override
	protected String element() {
		return "op:  " + op;
	}

}
