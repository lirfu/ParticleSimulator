package com.lirfu.physicssimulator.commands;

import com.lirfu.physicssimulator.Command;
import com.lirfu.physicssimulator.Simulator;

public class Tray extends Command {
	public Tray() {
		super("TRAY", "Draw tray after particles, set amount of tray points allowed on screen. Example: tray on/off maxPoints");
	}
	
	@Override
	public String execute(Simulator s, String input) {
		input = input.toUpperCase();
		String out = "";
		synchronized (s) {
			if (input.contains("ON")) {
				s.drawTray = true;
				out += "Tray ON!";
			} else if (input.contains("OFF")) {
				s.drawTray = false;
				out += "Tray OFF!";
			}
		}
		
		for (String tmp : input.split(" ")) {
			try {
				int value = Integer.parseInt(tmp);
				synchronized (s) {
					s.maxTrayPoints = value;
				}
				out += " Max points: " + s.maxTrayPoints;
				break;
			} catch (NumberFormatException e) {
			}
		}
		return out;
	}
	
}
