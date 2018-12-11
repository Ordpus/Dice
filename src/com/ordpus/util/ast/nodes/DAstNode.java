package com.ordpus.util.ast.nodes;

import com.ordpus.util.StdOut;

public abstract class DAstNode {

	public DAstNode e1;
	public DAstNode e2;
	public DAstNode parent;

	protected String element() {
		return "";
	}

	@Override
	public String toString() {
		String white = StdOut.genSpace(parents() + 1);
		String type = "{\n" + white + ' ' + "type:  " + getClass().getSimpleName();
		String elem = element().equals("") ? "" : '\n' + white + ' ' + "elem:  " + element();
		String e1 = this.e1 == null ? "" : '\n' + white + ' ' + "e1:  " + this.e1.toString();
		String e2 = this.e2 == null ? "" : '\n' + white + ' ' + "e2:  " + this.e2.toString();
		return type + elem + e1 + e2 + '\n' + white + '}';
	}

	public int parents() {
		return parents(this, 0);
	}

	protected int parents(DAstNode node, int i) {
		if(node.parent == null) return i;
		return parents(node.parent, i + 1);
	}

}
