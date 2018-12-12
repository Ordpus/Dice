package com.ordpus.util.ast;

import org.apache.commons.math3.util.FastMath;

import com.ordpus.util.StdOut;
import com.ordpus.util.ast.nodes.DAstNode;
import com.ordpus.util.ast.nodes.DAstNodeBracket;
import com.ordpus.util.ast.nodes.DAstNodeDice;
import com.ordpus.util.ast.nodes.DAstNodeOp;
import com.ordpus.util.ast.nodes.DAstNodeSharp;
import com.ordpus.util.helper.ArraysUtil;
import com.ordpus.util.helper.MathUtil;
import com.ordpus.util.info.DiceWrapper;
import com.ordpus.util.info.Group;
import com.ordpus.util.info.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.var;

public class DAst {

	private DAstNode root;
	@Getter
	private StringBuilder content = new StringBuilder();
	@Getter
	private DAstWrapper result;

	public Group group;
	public User user;

	public DAst(Group group, User user) {
		this.group = group;
		this.user = user;
	}

	public static DAst of(Group group, User user, String str) {
		str = str.replaceAll(" ", "").toLowerCase();
		char prev = 0, curr = 0, succ = 0;
		boolean inD = false, inSharp = false;
		int brac = 0;
		for(int i = 0; i < str.length(); ++i) {
			curr = str.charAt(i);
			if(i != 0) prev = str.charAt(i - 1);
			if(i < str.length() - 1) succ = str.charAt(i + 1);
			else succ = 0;
			if(curr == '#') {
				if((!Character.isDigit(prev) && prev != 'd' && prev != '(') || (succ != 'd' && !Character.isDigit(succ) && succ != '(') || inSharp) return new DAst(group, user);
				inD = false;
				inSharp = true;
			} else if(curr == 'd') {
				if(inD || (!Character.isDigit(prev) && !isOp(prev) && prev != '(' && prev != 0 && prev != '#') || (!Character.isDigit(succ) && !isOp(succ) && succ != ')' && succ != 0 && succ != '#' && succ != '(')) return new DAst(group, user);
				if(!Character.isDigit(prev)) str = str.substring(0, i) + user.setting.defaultDice.count + str.substring(i++);
				if(!Character.isDigit(succ) && succ != '(') str = str.substring(0, i + 1) + user.setting.defaultDice.getSize() + str.substring(++i);
				inD = true;
			} else if(curr == '(') {
				if(succ == 0 || (!isOp(prev) && prev != '#' && prev != 0 && prev != '(' && !Character.isDigit(prev) && prev != 'd') || (!Character.isDigit(succ) && succ != 'd' && succ != '(')) return new DAst(group, user);
				if(Character.isDigit(prev) || prev == '(') str = str.substring(0, i) + '*' + str.substring(i++);
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
		StdOut.println(str);
		return parse(group, user, str);
	}

	public static void main(String[] args) {
		String str = "2#4";
		DAst root = of(new Group(), new User(), str);
		StdOut.println(root);
	}

	public DAstWrapper parse() {
		if(root == null) return new DAstWrapper(0);
		result = parseExpression(root, false);
		if(result.result != null) {
			String res = '{' + ArraysUtil.oneDArrayToStr(result.result, ", ", "") + '}';
			if(!res.equals(content.toString())) content.append(" = ").append(res);
		} else {
			String res = MathUtil.num2Str(result.res);
			if(!res.equals(content.toString())) content.append(" = ").append(res);
		}
		return result;
	}

	public DAstWrapper parseExpression(String str) {
		root = of(group, user, str).root;
		return parse();
	}

	private DAstWrapper parseExpression(DAstNode node, boolean inSharp) {
		if(node instanceof DAstNodeDice) {
			DAstNodeDice dice = (DAstNodeDice) node;
			Object[] roll = dice.dice.roll();
			if(!inSharp) {
				checkPrevOp(node, content);
				if(dice.dice.count > 1) content.append('(');
				content.append(roll[0]);
				if(dice.dice.count > 1) content.append(')');
			}
			return new DAstWrapper((double) roll[2]);
		} else if(node instanceof DAstNodeOp) {
			char op = ((DAstNodeOp) node).op;
			DAstWrapper e1 = parseExpression(node.e1, inSharp);
			DAstWrapper e2 = parseExpression(node.e2, inSharp);
			if(e1.result != null) return adjustPos(e1, e2, op);
			else if(e2.result != null) return adjustPos(e2, e1, op);
			return new DAstWrapper(MathUtil.operator(op, e1.res, e2.res));
		} else if(node instanceof DAstNodeSharp) {
			int count = (int) FastMath.round(parseExpression(node.e1, true).res);
			double res = 0;
			result = new DAstWrapper(new double[count], res);
			checkPrevOp(node, content);
			content.append('{');
			for(int i = 0; i < count; ++i) {
				double temp = parseExpression(node.e2, true).res;
				content.append(MathUtil.num2Str(temp));
				if(i != count - 1) content.append(", ");
				res += temp;
				result.result[i] = temp;
			}
			content.append('}');
			return result;
		} else if(node instanceof DAstNodeBracket) {
			if(!inSharp) {
				checkPrevOp(node, content);
				content.append('(');
			}
			var res = parseExpression(node.e1, inSharp);
			content.append(')');
			return res;
		} else return new DAstWrapper(0);
	}

	private static DAst parse(Group group, User user, String str) {
		DAst result = new DAst(group, user);
		result.root = getParent(result.parseExpression(group, user, result.root, str, 0));
		result.parse();
		return result;
	}

	private DAstNode parseExpression(Group group, User user, DAstNode node, String str, int i) {
		if(i > str.length() - 1) return node;
		char ch = str.charAt(i);
		String parse = "";
		if(ch == '#') {
			parse = getNextBlock(str, i);
			DAstNode curr = new DAstNodeSharp(), right = getParent(parseExpression(group, user, null, parse, 0));
			curr.e2 = right;
			right.parent = curr;
			nodeTransfer(node, curr, ch);
			if(str.charAt(i + 1) == '(') i += 2;
			return parseExpression(group, user, curr, str, i + parse.length() + 1);
		} else if(isOp(ch)) {
			DAstNode curr = new DAstNodeOp(ch);
			nodeTransfer(node, curr, ch);
			return parseExpression(group, user, curr, str, i + 1);
		} else if(Character.isDigit(ch) || ch == 'd') {
			parse = getNextDice(str, i);
			int end = i + parse.length() - 1;
			DAstNodeDice curr;
			if(end == str.length() - 1 || str.charAt(end) != 'd' || str.charAt(end + 1) != '(') curr = new DAstNodeDice(parseDice(parse));
			else {
				curr = new DAstNodeDice(parseDice(parse + "0"));
				parse = getNextBlock(str, i + end + 1);
				curr.dice.size = parseExpression(parseExpression(group, user, null, parse, 0), false).res;
				content = new StringBuilder();
				end += parse.length() + 3;
			}
			if(node != null) {
				curr.parent = node;
				node.e2 = curr;
			}
			return parseExpression(group, user, curr, str, end + 1);
		} else if(ch == '(') {
			parse = getNextBlock(str, i);
			DAstNode curr = new DAstNodeBracket(), right = getParent(parseExpression(group, user, null, parse, 0));
			curr.e1 = right;
			right.parent = curr;
			if(node != null) {
				node.e2 = curr;
				curr.parent = node;
			}
			return parseExpression(group, user, curr, str, i + parse.length() + 2);
		} else return null;
	}

	public void clear() {
		root = null;
	}

	private static DAstNode getParent(DAstNode node) {
		if(node.parent != null) return getParent(node.parent);
		return node;
	}

	private static boolean isOp(char ch) {
		return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '#';
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
		else if(ch == '#') return 3;
		else return 2;
	}

	private static void nodeTransfer(DAstNode node, DAstNode curr, char ch) {
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
	}

	private static void checkPrevOp(DAstNode node, StringBuilder content) {
		if(node.parent instanceof DAstNodeOp && node.parent.e2 == node) content.append(' ').append(((DAstNodeOp) node.parent).op).append(' ');
	}

	private static DAstWrapper adjustPos(DAstWrapper e1, DAstWrapper e2, char op) {
		double[] arr = ArraysUtil.op(e1.result, e2.res, op);
		return new DAstWrapper(arr, ArraysUtil.sum(arr));
	}

	@Override
	public String toString() {
		return "{\n" + "  content:  " + content + result.toString() + "\n  tree:  " + root + "\n}";
	}

}

@AllArgsConstructor
class DAstWrapper {
	double[] result;
	double res;

	DAstWrapper(double res) {
		this.res = res;
	}

	@Override
	public String toString() {
		return "\n  result: {" + (result != null ? ArraysUtil.oneDArrayToStrHor(result) : "null") + "}\n  res:  " + ArraysUtil.sum(result);
	}
}
