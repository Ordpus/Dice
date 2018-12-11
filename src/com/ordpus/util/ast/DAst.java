package com.ordpus.util.ast;

import java.text.DecimalFormat;

import org.apache.commons.math3.util.FastMath;

import com.ordpus.util.StdOut;
import com.ordpus.util.ast.nodes.DAstNode;
import com.ordpus.util.ast.nodes.DAstNodeDice;
import com.ordpus.util.ast.nodes.DAstNodeOp;
import com.ordpus.util.helper.MathUtil;
import com.ordpus.util.info.DiceWrapper;
import com.ordpus.util.info.Group;
import com.ordpus.util.info.User;

import lombok.Getter;

public class DAst {

	private DAstNode root;
	@Getter
	private String content = "";
	@Getter
	private double result;

	public Group group;
	public User user;

	public DAst(Group group, User user) {
		this.group = group;
		this.user = user;
	}

	public static DAst of(Group group, User user, String str) {
		str = str.replaceAll(" ", "").toLowerCase();
		char prev = 0, curr = 0, succ = 0;
		boolean inD = false;
		int brac = 0;
		for(int i = 0; i < str.length(); ++i) {
			curr = str.charAt(i);
			if(i != 0) prev = str.charAt(i - 1);
			if(i < str.length() - 1) succ = str.charAt(i + 1);
			else succ = 0;
			if(curr == '#') {
				if((!Character.isDigit(prev) && prev != 'd' && prev != '(') || (succ != 'd' && !Character.isDigit(succ) && succ != '(')) return new DAst(group, user);
				inD = false;
			} else if(curr == 'd') {
				if(inD || (!Character.isDigit(prev) && !isOp(prev) && prev != '(' && prev != 0 && prev != '#') || (!Character.isDigit(succ) && !isOp(succ) && succ != ')' && succ != 0 && succ != '#' && succ != '(')) return new DAst(group, user);
				if(!Character.isDigit(prev)) str = str.substring(0, i) + user.setting.defaultDice.count + str.substring(i++);
				if(!Character.isDigit(succ)) str = str.substring(0, i + 1) + user.setting.defaultDice.getSize() + str.substring(++i);
				inD = true;
			} else if(curr == '(') {
				if(succ == 0 || (!isOp(prev) && prev != '#' && prev != 0 && prev != '(' && !Character.isDigit(prev) && prev != 'd') || (!Character.isDigit(succ) && succ != 'd' && succ != '(')) return new DAst(group, user);
				if(Character.isDigit(prev) || prev == 'd') str = str.substring(0, i) + '*' + str.substring(i++);
				inD = false;
				brac++;
			} else if(curr == ')') {
				if((prev != ')' && !Character.isDigit(prev)) || (!isOp(succ) && succ != ')' && succ != 0)) return new DAst(group, user);
				brac--;
				inD = false;
			} else if(isOp(curr)) {
				if(prev == '#' || succ == '#' || succ == 0 || prev == '(' || succ == ')') return new DAst(group, user);
				inD = false;
			}
		}
		if(brac != 0) return new DAst(group, user);
		return parse(group, user, str);
	}

	public static void main(String[] args) {
		String str = "50*3+1";
		DAst root = of(new Group(), new User(), str);
		StdOut.println(root);
	}

	public double parse() {
		if(root == null) return 0;
		result = parseExpression(root);
		if(MathUtil.isInt(result)) content = Long.toString(FastMath.round(result));
		else content = new DecimalFormat("#.###").format(result);
		return result;
	}

	public double parseExpression(String str) {
		root = of(group, user, str).root;
		result = parseExpression(root);
		if(MathUtil.isInt(result)) content = Long.toString(FastMath.round(result));
		else content = Double.toString(result);
		return result;
	}

	private double parseExpression(DAstNode node) {
		if(node instanceof DAstNodeDice) return ((DAstNodeDice) node).dice.roll();
		else if(node instanceof DAstNodeOp) return operator(((DAstNodeOp) node).op, parseExpression(node.e1), parseExpression(node.e2));
		else return 0;
	}

	private static DAst parse(Group group, User user, String str) {
		DAst result = new DAst(group, user);
		result.root = getParent(parseExpression(group, user, result.root, str, 0));
		result.parse();
		return result;
	}

	private static DAstNode parseExpression(Group group, User user, DAstNode node, String str, int i) {
		if(i > str.length() - 1) return node;
		char ch = str.charAt(i);
		String parse = "";
		if(isOp(ch)) {
			DAstNode curr = new DAstNodeOp(ch);
			if(node.parent instanceof DAstNodeOp) {
				DAstNodeOp prev = (DAstNodeOp) node.parent;
				if(opLevel(ch) > opLevel(prev.op)) {
					DAstNode e = prev.e2;
					prev.e2 = curr;
					curr.parent = prev;
					curr.e1 = e;
				} else {
					DAstNode peek = getParent(node);
					peek.parent = curr;
					curr.e1 = peek;
				}
			} else {
				node.parent = curr;
				curr.e1 = node;
			}
			return parseExpression(group, user, curr, str, i + 1);
		} else if(Character.isDigit(ch) || ch == 'd') {
			parse = getNextDice(str, i);
			DAstNode curr = new DAstNodeDice(parseDice(parse));
			if(node != null) {
				curr.parent = node;
				node.e2 = curr;
			}
			return parseExpression(group, user, curr, str, i + parse.length());
		} else if(ch == '#') {
			parse = getNextBlock(str, i);
		} else if(ch == '(') {
			parse = getNextBlock(str, i);
		}
		return null;
	}

	private static DAstNode getParent(DAstNode node) {
		if(node.parent != null) return getParent(node.parent);
		return node;
	}

	public void clear() {
		root = null;
	}

	private static boolean isOp(char ch) {
		return ch == '+' || ch == '-' || ch == '*' || ch == '/';
	}

	private static String getNextDice(String str, int i) {
		int mark = i;
		while(mark < str.length() && (Character.isDigit(str.charAt(mark)) || str.charAt(mark) == 'd')) {
			mark++;
		}
		return str.substring(i, mark);
	}

	public static DiceWrapper parseDice(String str) {
		int i = str.indexOf('d');
		if(i == -1) return new DiceWrapper(0, Double.parseDouble(str));
		else return new DiceWrapper(Integer.parseInt(str.substring(0, i)), Double.parseDouble(str.substring(i + 1)));
	}

	private static String getNextBlock(String str, int i) {
		int mark = i + 1;
		int brac = 0;
		char type = 0;
		if(str.charAt(i) == '#') {
			type = 0;
			if(str.charAt(i + 1) == '(') type = 2;
		} else if(str.charAt(i) == '(') {
			type = 1;
			brac++;
		}
		for(mark += 0; mark < str.length(); ++mark) {
			if(str.charAt(mark) == '(') brac++;
			else if(str.charAt(mark) == ')') {
				brac--;
				if(brac == 0) {
					if(type == 1) return str.substring(i + 1, mark);
					else if(type == 2) return str.substring(i + 2, mark);
				}
			} else if(isOp(str.charAt(mark)) && brac == 0 && type == 0) return str.substring(i + 1, mark);
		}
		return str.substring(i + 1);
	}

	private static int opLevel(char ch) {
		if(!isOp(ch)) return 0;
		if(ch == '+' || ch == '-') return 1;
		else return 2;
	}

	private static double operator(char op, double a, double b) {
		switch(op) {
			case '+':
				return a + b;
			case '-':
				return a - b;
			case '*':
				return a * b;
			case '/':
				return a / b;
			default:
				return 0;
		}
	}

	@Override
	public String toString() {
		return "{\n" + " content:  " + content + "\n result:  " + result + "\n tree:  " + root;
	}

}
