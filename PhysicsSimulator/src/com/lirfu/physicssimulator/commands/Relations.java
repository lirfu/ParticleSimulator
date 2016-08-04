package com.lirfu.physicssimulator.commands;

import com.lirfu.physicssimulator.Command;
import com.lirfu.physicssimulator.Simulator;

public class Relations extends Command {
	public Relations() {
		super("RELATIONS", "Draw force relations between particles! Example: relations on/off");
	}
	
	@Override
	public String execute(Simulator s, String input) {
		input = input.toUpperCase();
		String out = "";
		
		synchronized (s) {
			if (input.contains("ON")) {
				s.drawRelations = true;
				out = "Relations are ON!";
			} else if (input.contains("OFF")) {
				s.drawRelations = false;
				out = "Relations are OFF!";
			}
		}
		
		return out;
	}
	
}
