package com.ordpus.util.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ordpus.Dice;
import com.ordpus.util.StdOut;
import com.ordpus.util.ast.DAst;
import com.ordpus.util.info.Group;
import com.ordpus.util.info.User;

import lombok.var;

public class CommandReader {

	private static final String path = "com.ordpus.util.command.Command";

	public static CommandWrapper read(User user, Group group, String command) {
		if((command.charAt(0) == '.' || command.charAt(0) == '。') && (command.charAt(1) == 'r' || command.charAt(1) == 'R')) return readDice(user, group, command.substring(2));
		else if(command.charAt(0) == '/') return readCommand(user, group, command.substring(1));
		return null;
	}

	private static CommandWrapper readDice(User user, Group group, String command) {
		int i = 0, s = 0;
		command = command.toLowerCase();
		if(command.length() == 0) command = "d";
		if(command.charAt(0) == 'h') s++;
		while(i < command.length() && isText(command.charAt(i++))) {}
		String dice = command.substring(s, i);
		var result = DAst.of(group, user, dice);
		return new CommandWrapper("dice", "* " + Dice.CQ.getStrangerInfo(user.id).getNick() + (i < command.length() - 1 ? "由于 " + command.substring(i) : "") + " 骰出了\n" + dice + " = " + result.getContent()).add("hidden", s == 1);
	}

	public static void main(String[] args) {
		StdOut.println(read(User.of(1), new Group(), "/mute"));
	}

	private static CommandWrapper readCommand(User user, Group group, String command) {
		command = command.replaceAll("  ", " ");
		int i = 0;
		while(i < command.length() && command.charAt(i++) != ' ') {}
		CommandWrapper error = new CommandWrapper("", "");
		try {
			String[] commands = command.split(" ");
			String className;
			if(commands.length == 1) className = "Basic";
			else className = Character.toUpperCase(command.charAt(0)) + (command.length() > 1 ? command.substring(1, i - 1) : "");
			Class<?> com = Class.forName(path + className);
			Method exec = com.getMethod("execute", User.class, Group.class, String[].class);
			error = (CommandWrapper) exec.invoke(com.getField("instance").get(null), user, group, commands);
		} catch(ClassNotFoundException e) {
			error.content += "no such command ";
			error.type = "false";
			e.printStackTrace();
		} catch(NoSuchMethodException e) {
			error.content += command.substring(1, i - 1) + " is not a command ";
			error.type = "false";
			e.printStackTrace();
		} catch(SecurityException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
		} catch(InvocationTargetException e) {
			e.printStackTrace();
		} catch(NoSuchFieldException e) {
			e.printStackTrace();
		}
		return error;
	}

	private static boolean isText(char ch) {
		return ch == 'd' || ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '#' || Character.isDigit(ch) || ch == '(' || ch == ')';
	}

}
