package com.ordpus.util.command;

import com.ordpus.util.info.Group;
import com.ordpus.util.info.User;

public class CommandBasic extends CommandBase {

	static {
		instance = new CommandBasic();
	}

	@Override
	public CommandWrapper execute(User user, Group group, String[] command) {
		if(command.length > 1) return null;
		if(command[0].equals("mute")) return new CommandWrapper("mute", mute(group));
		return null;
	}

	private String mute(Group group) {
		boolean mute = group.setting.muted;
		group.setting.muted = !mute;
		if(mute) return "Back on";
		else return "Muted";

	}

}
