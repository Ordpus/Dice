package com.ordpus.util.command;

import com.ordpus.util.info.Group;
import com.ordpus.util.info.User;

public abstract class CommandBase {

	public static CommandBase instance;

	public abstract CommandWrapper execute(User user, Group group, String[] command);

}
