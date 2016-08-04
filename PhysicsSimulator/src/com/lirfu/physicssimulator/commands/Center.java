package com.lirfu.physicssimulator.commands;

import com.lirfu.physicssimulator.Command;
import com.lirfu.physicssimulator.Simulator;

public class Center extends Command {
	public Center() {
		super("CENTER", "Tracks the center of the whole system. Example: center on/off");
	}
	
	@Override
	public String execute(Simulator s, String input) {
		input = input.toUpperCase();
		String out = "";
		
		synchronized (s) {
			if (input.contains("ON")) {
				s.trackCenter = true;
				out = "Tracking system center is ON!";
			} else if (input.contains("OFF")) {
				s.trackCenter = false;
				out = "Tracking system center is OFF!";
			}
		}
		
		return out;
	}
}
